package com.example.semtempo.model;

public enum Prioridade {

    ALTA(1), MEDIA(2), BAIXA(3);

    private final int peso;

    Prioridade(int peso) {
            this.peso = peso;
    }

    public int getPeso() {
            return peso;
    }

}
