package com.example.pokedex.pokemon;

import com.example.pokedex.Type.Type;
import com.example.pokedex.Type.TypeDto;
import lombok.*;

import java.util.List;

@Data
public class PokemonDto {

    private String name;
    private String description;
    private List<TypeDto> types;

}
