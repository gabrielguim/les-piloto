package com.example.semtempo.model;

import java.text.*;
import java.util.*;

public class Horario {

    private int totalHorasInvestidas;
    private int semana;
    private int dia;
    private int mes;
    private int ano;
    private String data;

    public Horario (){}

    public Horario(int totalHorasInvestidas, Calendar dataQueRealizou){
        this.totalHorasInvestidas = totalHorasInvestidas;
        this.semana = dataQueRealizou.get(Calendar.WEEK_OF_YEAR);
        this.data = dataQueRealizou.getTime().toString();
        this.ano = dataQueRealizou.get(Calendar.YEAR);
        this.dia = dataQueRealizou.get(Calendar.DAY_OF_MONTH);
        this.mes = dataQueRealizou.get(Calendar.MONTH);
    }

    public int getSemana() { return semana; }

    public String getData(){ return data; }

    public int getTotalHorasInvestidas() {
        return totalHorasInvestidas;
    }


    public int getDia() {
        return dia;
    }

    public int getMes() {
        return mes;
    }

    public int getAno() {
        return ano;
    }
}