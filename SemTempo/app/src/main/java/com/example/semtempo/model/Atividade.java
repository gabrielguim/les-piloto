package com.example.semtempo.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lucas on 20/11/2016.
 */
public class Atividade {

    private String nomeDaAtv;
    private Horario horario;
    private Prioridade prioridade;
    private Categoria categoria;
    private String base64Imagem;
    private List<String> tags;

    public Atividade(){}

    public Atividade(String nomeDaAtv, Prioridade prioridade, Horario horario, Categoria categoria, List<String> tags){
        this.nomeDaAtv = nomeDaAtv;
        this.prioridade = prioridade;
        this.horario = horario;
        this.categoria = categoria;
        setTags(tags);
    }

    public Atividade(String nomeDaAtv, Prioridade prioridade, Horario horario, Categoria categoria){
        this.nomeDaAtv = nomeDaAtv;
        this.prioridade = prioridade;
        this.horario = horario;
        this.categoria = categoria;
        setTags(new ArrayList<String>());
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

    public Categoria getCategoria(){
        return categoria;
    }

    public void setCategoria(Categoria categoria){
        this.categoria = categoria;
    }

    public List<String> getTags() { return tags; }

    private void setTags(List<String> tags) { this.tags = tags; }

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
