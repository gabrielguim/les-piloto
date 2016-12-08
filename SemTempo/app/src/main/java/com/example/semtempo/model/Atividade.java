package com.example.semtempo.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lucas on 20/11/2016.
 */
public class Atividade {

    private String nomeDaAtv;
    private Foto fotoDaAtividade;
    private Horario horariosRealizDaAtv;
    private Categoria categoria;
    private Prioridade prioridade;
    private List<String> tags;

    public Atividade(){}

    public Atividade(String nomeDaAtv, Prioridade prioridade, Horario horario, Categoria tag, List<String> tags){
        this.nomeDaAtv = nomeDaAtv;
        this.prioridade = prioridade;
        this.horariosRealizDaAtv = horario;
        setTags(tags);
    }

    public Atividade(String nomeDaAtv, Foto fotoDaAtividade, Categoria categoria, Prioridade prioridade, Horario horario, List<String> tags){
        this.nomeDaAtv = nomeDaAtv;
        this.fotoDaAtividade = fotoDaAtividade;
        this.categoria = categoria;
        this.prioridade = prioridade;
        this.horariosRealizDaAtv = horario;
        setTags(tags);
    }

    public Foto getFotoDaAtividade() { return fotoDaAtividade; }

    public void alterarFotoDaAtividade(Foto fotoDaAtividade) { this.fotoDaAtividade = fotoDaAtividade; }

    public Prioridade getPrioridade() { return prioridade; }

    public Horario getHorariosRealizDaAtv() { return horariosRealizDaAtv; }

    public void setListaHorarios(Horario horario){ this.horariosRealizDaAtv = horario; }

    public Categoria getCategoria() { return categoria; }

    public void setCategoria(Categoria categoria) { this.categoria = categoria; }

    public void setPrioridade(Prioridade prioridade) { this.prioridade = prioridade;}

    public String getNomeDaAtv() { return nomeDaAtv; }

    public void setNomeDaAtv(String nomeDaAtv) { this.nomeDaAtv = nomeDaAtv; }

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
