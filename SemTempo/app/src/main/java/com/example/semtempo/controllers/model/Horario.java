package com.example.semtempo.controllers.model;

import java.util.Calendar;

public class Horario {

    private int totalHorasInvestidas;
    private Calendar dataQueRealizou;

    public Horario(int totalHorasInvestidas, Calendar dataQueRealizou){
        this.totalHorasInvestidas = totalHorasInvestidas;
        this.dataQueRealizou = dataQueRealizou;
    }

    public int getTotalHorasInvestidas() {
        return totalHorasInvestidas;
    }

    public Calendar getDataQueRealizou() {
        return dataQueRealizou;
    }

}
