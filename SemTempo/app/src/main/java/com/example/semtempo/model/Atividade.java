package com.example.semtempo.model;

/**
 * Created by Lucas on 20/11/2016.
 */
public class Atividade {

    private String nomeDaAtv;
    private Horario horario;
    private Prioridade prioridade;

    public Atividade(){}

    public Atividade(String nomeDaAtv, Prioridade prioridade, Horario horario){
        this.nomeDaAtv = nomeDaAtv;
        this.prioridade = prioridade;
        this.horario = horario;
    }

    public Horario getHorario() { return horario; }

    public void setHorario(Horario horario){this.horario = horario;}

    public Prioridade getPrioridade() { return prioridade;}

    public void setPrioridade(Prioridade prioridade) { this.prioridade = prioridade;}

    public String getNomeDaAtv() { return nomeDaAtv; }

    public void setNomeDaAtv(String nomeDaAtv) { this.nomeDaAtv = nomeDaAtv; }

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
