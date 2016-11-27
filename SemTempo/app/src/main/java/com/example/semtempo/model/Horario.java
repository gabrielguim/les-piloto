package com.example.semtempo.model;

import java.text.*;
import java.util.*;

public class Horario {

    private int totalHorasInvestidas;
    private Calendar dataQueRealizou;
    private String data;
    private int semana;

    public Horario(int totalHorasInvestidas, Calendar dataQueRealizou){
        this.totalHorasInvestidas = totalHorasInvestidas;
        this.dataQueRealizou = dataQueRealizou;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateFormat.setCalendar(dataQueRealizou);
        data = dateFormat.format(dataQueRealizou.getTime());
        semana = dataQueRealizou.get(Calendar.WEEK_OF_YEAR);
    }

    public int getSemana() { return semana; }

    public String getData(){ return data; }

    public int getTotalHorasInvestidas() {
        return totalHorasInvestidas;
    }

    public Calendar getDataQueRealizou() {
        return dataQueRealizou;
    }

}