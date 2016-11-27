package com.example.semtempo.controllers.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Lucas on 20/11/2016.
 */
public class Horario {

    private int totalHorasInvestidas;
    private String dataQueRealizou;
    private int semana;

    public Horario(int totalHorasInvestidas, Calendar dataQueRealizou){
        this.totalHorasInvestidas = totalHorasInvestidas;
        this.dataQueRealizou = dataQueRealizou.getTime().toString();
        this.semana = dataQueRealizou.get(Calendar.WEEK_OF_YEAR);
    }

    public Horario(int totalHorasInvestidas, int semana){
        this.semana = semana;
        this.totalHorasInvestidas = totalHorasInvestidas;
    }

    public Calendar getDataQueRealizou() throws ParseException {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
        cal.setTime(sdf.parse(dataQueRealizou));
        return cal;
    }

    public void alterarDataQueRealizou(Calendar dataQueRealizou) {
        this.dataQueRealizou = dataQueRealizou.getTime().toString();
    }

    public void setDataString(String dataQueRealizou) {
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
