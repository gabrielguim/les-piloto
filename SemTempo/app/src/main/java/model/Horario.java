package model;

import java.util.Calendar;

public class Horario {

    private int totalHorasInvestidas;
    private Calendar dataQueRealizou;

    public Horario(int totalHorasInvestidas, Calendar dataQueRealizou){
        this.totalHorasInvestidas = totalHorasInvestidas;
        this.dataQueRealizou = dataQueRealizou;
    }

    public Calendar retornarDataQueRealizou() {
        return dataQueRealizou;
    }

    public void alterarDataDeRealizacao(Calendar dataQueRealizou) {
        this.dataQueRealizou = dataQueRealizou;
    }

    public int retornarTotalHorasInvestidas() {
        return totalHorasInvestidas;
    }

    public void alterarTotalHorasInvestidas(int totalHorasInvestidas) {
        this.totalHorasInvestidas = totalHorasInvestidas;
    }

}
