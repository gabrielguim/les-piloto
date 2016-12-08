package com.example.semtempo.model;

import java.text.*;
import java.util.*;

public class Horario {

    private int totalHorasInvestidas;
    private String data;
    private int semana;

    public Horario(){}

    public Horario(int totalHorasInvestidas, Calendar dataQueRealizou){
        this.totalHorasInvestidas = totalHorasInvestidas;
        this.data = dataQueRealizou.getTime().toString();
        this.semana = dataQueRealizou.get(Calendar.WEEK_OF_YEAR);
    }

    public int getSemana() { return semana; }

    public String getData(){ return data; }

    public int getTotalHorasInvestidas() {
        return totalHorasInvestidas;
    }


}