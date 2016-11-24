package com.example.semtempo.controllers.model;

import java.util.Calendar;

/**
 * Created by Lucas on 20/11/2016.
 */
public class Horario {

    private int totalHorasInvestidas;
    private Calendar dataQueRealizou;

    public Horario(int totalHorasInvestidas, Calendar dataQueRealizou){
        this.totalHorasInvestidas = totalHorasInvestidas;
        this.dataQueRealizou = dataQueRealizou;
    }

    public Calendar getDataQueRealizou() {
        return dataQueRealizou;
    }

    public void alterarDataQueRealizou(Calendar dataQueRealizou) {
        this.dataQueRealizou = dataQueRealizou;
    }

    public int getTotalHorasInvestidas() {
        return totalHorasInvestidas;
    }

    public void alterarTotalHorasInvestidas(int totalHorasInvestidas) {
        this.totalHorasInvestidas = totalHorasInvestidas;
    }

}
