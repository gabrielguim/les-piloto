package model;

/**
 * Created by Lucas on 20/11/2016.
 */
public enum Prioridade {

    ALTA(3), BAIXA(1), MEDIA(2);

    private final int peso;

    Prioridade(int peso) {
            this.peso = peso;
    }

    public int retornarPeso() {
            return peso;
    }

}
