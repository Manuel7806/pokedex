package com.example.pokedex.pokemon;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:5173/")
@RestController
@RequestMapping("/api/v1/pokemon")
public class PokemonController {

    private final PokemonService pokemonService;

    public PokemonController(PokemonService pokemonService) {
        this.pokemonService = pokemonService;
    }

    @GetMapping
    public ResponseEntity<List<PokemonDto>> getAllPokemon() {
        return pokemonService.findAll();
    }

    @PostMapping
    public ResponseEntity<PokemonDto> createNewPokemon(@RequestBody PokemonDto pokemonDto) {
        return pokemonService.save(pokemonDto);
    }

    @GetMapping("/{name}")
    public ResponseEntity<Optional<PokemonDto>> getPokemonByName(@PathVariable String name) {
        return pokemonService.findByName(name);
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<String> deletePokemon(@PathVariable Long id) {
        return pokemonService.delete(id);
    }
}
