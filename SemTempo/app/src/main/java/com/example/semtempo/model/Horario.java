package com.example.semtempo.model;

import java.text.*;
import java.util.*;

public class Horario {

    private int totalHorasInvestidas;
    private Calendar dataQueRealizou;

    public Horario(int totalHorasInvestidas, Calendar dataQueRealizou){
        this.totalHorasInvestidas = totalHorasInvestidas;
        this.dataQueRealizou = dataQueRealizou;
    }

    public int getSemana() { return dataQueRealizou.get(Calendar.WEEK_OF_YEAR); }

    public String getData(){ return dataQueRealizou.getTime().toString(); }

    public int getTotalHorasInvestidas() {
        return totalHorasInvestidas;
    }

    public Calendar getDataQueRealizou(){
        return dataQueRealizou;
    }


}