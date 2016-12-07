package com.example.semtempo.model;

import android.net.Uri;

import com.example.semtempo.utils.Utils;

/**
 * Created by Lucas on 20/11/2016.
 */
public class Atividade {

    private String nomeDaAtv;
    private Horario horario;
    private Prioridade prioridade;
    private Tag tag;
    private String base64Imagem;

    public Atividade(){}

    public Atividade(String nomeDaAtv, Prioridade prioridade, Horario horario, Tag tag){
        this.nomeDaAtv = nomeDaAtv;
        this.prioridade = prioridade;
        this.horario = horario;
        this.tag = tag;
    }

    public void setFoto(String base64Imagem){
        this.base64Imagem = base64Imagem;
    }

    public String getFoto(){
        return base64Imagem;
    }

    public Horario getHorario() { return horario; }

    public void setHorario(Horario horario){this.horario = horario;}

    public Prioridade getPrioridade() { return prioridade;}

    public void setPrioridade(Prioridade prioridade) { this.prioridade = prioridade;}

    public String getNomeDaAtv() { return nomeDaAtv; }

    public void setNomeDaAtv(String nomeDaAtv) { this.nomeDaAtv = nomeDaAtv; }

    public Tag getTag(){
        return tag;
    }

    public void setTag(Tag tag){
        this.tag = tag;
    }

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
