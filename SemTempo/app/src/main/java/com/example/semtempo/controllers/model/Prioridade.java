package com.example.semtempo.controllers.model;

public enum Prioridade {

    ALTA(3), BAIXA(1), MEDIA(2);

    private final int peso;

    Prioridade(int peso) {
            this.peso = peso;
    }

    public int getPeso() {
            return peso;
    }

}
