package com.example.semtempo.model;

import java.util.List;

/**
 * Created by Rafael on 11/26/2016.
 */

public class Usuario {
    private String nome;
    private List<Atividade> atividadeList;

    public Usuario(String nome, List atividadeList){
        this.nome = nome;
        this.atividadeList = atividadeList;
    }

    public String getNome(){
        return nome;
    }

    public List<Atividade> getAtividades(){
        return atividadeList;
    }

    public void setNome(String nome){
        this.nome = nome;
    }

    public void setAtividadeList(List<Atividade> atividadeList){
        this.atividadeList = atividadeList;
    }
}
