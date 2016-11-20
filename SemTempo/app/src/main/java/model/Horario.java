package model;

import java.util.Date;

/**
 * Created by Lucas on 20/11/2016.
 */
public class Horario {

    private int totalHorasInvestidas;
    private Date dataQueRealizou;

    public Horario(int totalHorasInvestidas, Date dataQueRealizou){
        this.totalHorasInvestidas = totalHorasInvestidas;
        this.dataQueRealizou = dataQueRealizou;
    }

    public Date getDataQueRealizou() {
        return dataQueRealizou;
    }

    public void alterarDataQueRealizou(Date dataQueRealizou) {
        this.dataQueRealizou = dataQueRealizou;
    }

    public int getTotalHorasInvestidas() {
        return totalHorasInvestidas;
    }

    public void alterarTotalHorasInvestidas(int totalHorasInvestidas) {
        this.totalHorasInvestidas = totalHorasInvestidas;
    }

}
