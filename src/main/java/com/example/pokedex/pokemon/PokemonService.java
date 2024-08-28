package com.example.pokedex.pokemon;


import com.example.pokedex.Type.Type;
import com.example.pokedex.Type.TypeDto;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Service
public class PokemonService {

    private final PokemonRepository pokemonRepository;
    private final ModelMapper modelMapper;

    public PokemonService(PokemonRepository pokemonRepository, ModelMapper modelMapper) {
        this.pokemonRepository = pokemonRepository;
        this.modelMapper = modelMapper;
    }

    /**
     * Saves a PokemonDto object to the database.
     *
     * @param pokemonDto The PokemonDto object to be saved. Comes from @RequestBody in the controller.
     * @return A new PokemonDto object after being saved to the database.
     */
    public ResponseEntity<PokemonDto> save(PokemonDto pokemonDto) {
        // Create new Pokémon object
        Pokemon pokemon = new Pokemon();

        // Set the Pokémon objects name and description
        pokemon.setName(pokemonDto.getName());
        pokemon.setDescription(pokemonDto.getDescription());

        // Convert a list of TypeDto's to a list of Type objects
        List<Type> pokemonTypes = pokemonDto.getTypes()
            .stream()
            .map(type -> modelMapper.map(type, Type.class)).toList();

        // Convert a list of Type objects to a list of TypeDto's
        List<TypeDto> typeDtos = pokemonTypes
               .stream()
               .map(type -> modelMapper.map(type, TypeDto.class))
               .toList();


        // For each Type object in the pokemonTypes List
        // set the Pokémon field in the Type class to the
        // current Pokémon
        for (Type type : pokemonTypes) {
            type.setPokemon(pokemon);
        }

        // Set the types field of the Pokémon class
        // to the List of pokemonTypes
        pokemon.setTypes(pokemonTypes);

        // Set the types field of the PokemonDto class
        // to the List of TypeDtos
        pokemonDto.setTypes(typeDtos);

        // Save Pokemon object in database
        Pokemon newPokemon = pokemonRepository.save(pokemon);

        // Create a new instance of UriComponentsBuilder
        UriComponentsBuilder ucb = UriComponentsBuilder.newInstance();

        // Set the Location header to the new Pokémon object endpoint
        URI locationOfNewPokemon = ucb
            .path("/aip/v1/pokemon/{id}")
            .buildAndExpand(newPokemon.getId())
            .toUri();

        // Return the PokemonDto object with a HttpStatus of CREATED
        return ResponseEntity.created(locationOfNewPokemon).body(modelMapper.map(pokemon, PokemonDto.class));
    }

    public ResponseEntity<String> delete(Long id) {
        pokemonRepository.deleteById(id);

        return ResponseEntity.ok().body("Deleted Pokemon with ID of: " + id);
    }

    /**
     * Get a list of all Pokémon objects.
     *
     * @return A list of all Pokémon objects in the database.
     */
    public ResponseEntity<List<PokemonDto>> findAll() {
        List<Pokemon> pokemons = pokemonRepository.findAll();

        List<PokemonDto> pokemonDtos = pokemons.stream()
            .map(pokemon -> modelMapper.map(pokemon, PokemonDto.class))
            .toList();

        return ResponseEntity.ok(pokemonDtos);
    }

    /**
     * Find Pokémon by name provided in the url. If no Pokémon is found return
     * a 404. If a Pokémon is found return a PokemonDto object of that Pokémon.
     *
     * @param name The name of the Pokémon. Passed in through the @PathVariable from the controller.
     * @return Returns a ResponseEntity of either a 404 or a 200 with the body of the PokemonDto.
     */
    public ResponseEntity<Optional<PokemonDto>> findByName(String name) {
        Optional<Pokemon> pokemonOptional = pokemonRepository.findByName(name);

        if (pokemonOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Optional<PokemonDto> pokemonDto = pokemonOptional.map(pokemon -> modelMapper.map(pokemon, PokemonDto.class));

        return ResponseEntity.ok(pokemonDto);
    }

}
