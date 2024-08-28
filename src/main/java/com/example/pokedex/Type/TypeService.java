package com.example.pokedex.Type;

import org.springframework.stereotype.Service;


@Service
public class TypeService {

    private final TypeRepository typeRepository;

    public TypeService(TypeRepository typeRepository) {
        this.typeRepository = typeRepository;
    }

}
