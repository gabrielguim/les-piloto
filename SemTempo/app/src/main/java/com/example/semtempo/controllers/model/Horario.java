package com.example.semtempo.controllers.model;

import java.util.Calendar;

/**
 * Created by Lucas on 20/11/2016.
 */
public class Horario {

    private int totalHorasInvestidas;
    private Calendar dataQueRealizou;
    private int semana;

    public Horario(int totalHorasInvestidas, Calendar dataQueRealizou){
        this.totalHorasInvestidas = totalHorasInvestidas;
        this.dataQueRealizou = dataQueRealizou;
        this.semana = dataQueRealizou.get(Calendar.WEEK_OF_YEAR);
    }

    public Horario(int totalHorasInvestidas, int semana){
        this.semana = semana;
        this.totalHorasInvestidas = totalHorasInvestidas;
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

    public int getSemana(){
        return semana;
    }

}
