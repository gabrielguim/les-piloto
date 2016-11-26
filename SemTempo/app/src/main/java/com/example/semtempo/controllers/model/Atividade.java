package com.example.semtempo.controllers.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Lucas on 20/11/2016.
 */
public class Atividade {

    private String nomeDaAtv;
    private Foto fotoDaAtividade;
    private List<Horario> horariosRealizDaAtv;
    private Tag categoria;
    private Prioridade prioridade;
    private String id;

    public Atividade(String nomeDaAtv, Prioridade prioridade){
        this.nomeDaAtv = nomeDaAtv;
        this.prioridade = prioridade;
        this.horariosRealizDaAtv = new ArrayList<Horario>();
    }

    public Atividade(String nomeDaAtv, Foto fotoDaAtividade, Tag categoria, Prioridade prioridade){
        this.nomeDaAtv = nomeDaAtv;
        this.fotoDaAtividade = fotoDaAtividade;
        this.categoria = categoria;
        this.prioridade = prioridade;
        this.horariosRealizDaAtv = new ArrayList<Horario>();
    }

    public String getId(){
        return id;
    }

    public void setId(String id){
        this.id = id;
    }

    public Foto getFotoDaAtividade() {
        return fotoDaAtividade;
    }

    public void alterarFotoDaAtividade(Foto fotoDaAtividade) {
        this.fotoDaAtividade = fotoDaAtividade;
    }

    public List<Horario> getHorariosRealizDaAtv() {
        return horariosRealizDaAtv;
    }

    public void setListaHorarios(List<Horario> lista){
        this.horariosRealizDaAtv = lista;
    }

    public Tag getCategoria() {
        return categoria;
    }

    public void alterarCategoria(Tag categoria) {
        this.categoria = categoria;
    }

    public Prioridade getPrioridade() {
        return prioridade;
    }

    public void alterarPrioridade(Prioridade prioridade) {
        this.prioridade = prioridade;
    }

    public String getNomeDaAtv() {
        return nomeDaAtv;
    }

    public void alterarNomeDaAtv(String nomeDaAtv) {
        this.nomeDaAtv = nomeDaAtv;
    }

    public void registrarNovoHorario(Horario tempoInvestido){
        horariosRealizDaAtv.add(tempoInvestido);
    }

    /**
     * Retorna boolean pelo fato de que se um horário não consta na coleção, ele retornará falso.
     * */
    public boolean removerHorario(Horario horarioEscolhido){
        return horariosRealizDaAtv.remove(horarioEscolhido);
    }

    public int getTotalDeHorasGasto(){
        int total = 0;
        for (Horario horario: getHorariosRealizDaAtv()){
            total += horario.getTotalHorasInvestidas();
        }

        return total;
    }


}
