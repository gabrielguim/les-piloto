package com.example.semtempo.model;

public enum Categoria {

    SEMCATEGORIA, TRABALHO, LAZER;

    @Override
    public String toString() {
        switch(this) {
            case SEMCATEGORIA: return "Não Categorizado";
            case TRABALHO: return "Trabalho";
            case LAZER: return "Lazer";
            default: throw new IllegalArgumentException();
        }
    }
}
