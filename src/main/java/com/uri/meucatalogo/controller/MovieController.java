package com.uri.meucatalogo.controller;

import com.uri.meucatalogo.models.Movie;
import com.uri.meucatalogo.service.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movies")
@Tag(name = "Movies", description = "Gerenciamento de filmes")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Adicionar um novo filme", description = "Cria um novo filme no catálogo")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Filme criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public Movie addMovie(@RequestBody Movie movie) {
        return movieService.saveMovie(movie);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Listar todos os filmes", description = "Retorna todos os filmes cadastrados ou filtrados por título e/ou gênero")
    public List<Movie> getAllMovies(@RequestParam(required = false) String title,
                                    @RequestParam(required = false) String gender,
                                    @RequestParam(required = false, defaultValue = "false") boolean matchAll) {
        return movieService.searchMovies(title, gender, matchAll);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Buscar filme por ID", description = "Retorna um filme pelo seu ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Filme encontrado"),
            @ApiResponse(responseCode = "404", description = "Filme não encontrado")
    })
    public Movie getMovieById(@PathVariable String id) {
        return movieService.getMovieById(id).orElse(null);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Deletar filme por ID", description = "Remove um filme pelo seu ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Filme deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Filme não encontrado")
    })
    public void deleteMovieById(@PathVariable String id) {
        movieService.deleteMovieById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Editar filme por ID", description = "Edita um filme pelo seu ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Filme editado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Filme não encontrado")
    })
    public Movie updateMovie(@PathVariable String id, @RequestBody Movie movie) {
        return movieService.updateMovie(id, movie);
    }
}
