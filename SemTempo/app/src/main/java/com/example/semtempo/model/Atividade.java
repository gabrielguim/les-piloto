package com.example.semtempo.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.example.semtempo.model.Foto;
import com.example.semtempo.model.Horario;
import com.example.semtempo.model.Prioridade;
import com.example.semtempo.model.Tag;

/**
 * Created by Lucas on 20/11/2016.
 */
public class Atividade {

    private String nomeDaAtv;
    private Horario horariosRealizDaAtv;
    private Prioridade prioridade;
    private String id;

    public Atividade(){}

    public Atividade(String nomeDaAtv, Prioridade prioridade, Horario horario){
        this.nomeDaAtv = nomeDaAtv;
        this.prioridade = prioridade;
        this.horariosRealizDaAtv = horario;
    }

    public String getId(){ return id; }

    public void setId(String id){ this.id = id;}

    public Horario getHorariosRealizDaAtv() { return horariosRealizDaAtv; }

    public void setHorariosRealizDaAtv(Horario horario){this.horariosRealizDaAtv = horario;}

    public Prioridade getPrioridade() { return prioridade;}

    public void setPrioridade(Prioridade prioridade) { this.prioridade = prioridade;}

    public String getNomeDaAtv() { return nomeDaAtv; }

    public void alterarNomeDaAtv(String nomeDaAtv) { this.nomeDaAtv = nomeDaAtv; }

    @Override
    public int hashCode() { return nomeDaAtv.hashCode(); }

    @Override
    public boolean equals(Object obj){
        if(!(obj instanceof Atividade)){
            return false;
        }
        Atividade atv = (Atividade) obj;
        return this.nomeDaAtv.equals(atv.getNomeDaAtv());
    }


}
