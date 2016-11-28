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
    private Foto fotoDaAtividade;
    private Horario horariosRealizDaAtv;
    private Tag categoria;
    private Prioridade prioridade;
    private String id;

    public Atividade(){

    }

    public Atividade(String nomeDaAtv, Prioridade prioridade, Horario horario){
        this.nomeDaAtv = nomeDaAtv;
        this.prioridade = prioridade;
        this.horariosRealizDaAtv = horario;
    }

    public Atividade(String nomeDaAtv, Foto fotoDaAtividade, Tag categoria, Prioridade prioridade, Horario horario){
        this.nomeDaAtv = nomeDaAtv;
        this.fotoDaAtividade = fotoDaAtividade;
        this.categoria = categoria;
        this.prioridade = prioridade;
        this.horariosRealizDaAtv = horario;
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

    public Horario getHorariosRealizDaAtv() {
        return horariosRealizDaAtv;
    }

    public void setListaHorarios(Horario horario){
        this.horariosRealizDaAtv = horario;
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
        horariosRealizDaAtv = tempoInvestido;
    }

    @Override
    public int hashCode() {
        return nomeDaAtv.hashCode();
    }

    /**
     * Retorna boolean pelo fato de que se um horário não consta na coleção, ele retornará falso.
     * */
//    public boolean removerHorario(Horario horarioEscolhido){
//        return horariosRealizDaAtv.remove(horarioEscolhido);
//    }

//    public int getTotalDeHorasGasto(){
//        int total = 0;
//        for (Horario horario: getHorariosRealizDaAtv()){
//            total += horario.getTotalHorasInvestidas();
//        }
//
//        return total;
//    }

    @Override
    public boolean equals(Object obj){
        if(!(obj instanceof Atividade)){
            return false;
        }


        Atividade atv = (Atividade) obj;
        System.out.println("Equals");
        System.out.println(this.nomeDaAtv);
        System.out.println(atv.getNomeDaAtv());
        return this.nomeDaAtv.equals(atv.getNomeDaAtv());
    }


}
