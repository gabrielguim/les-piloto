package model;

import java.util.*;

public class Atividade {

    private String nome;
    private Collection<Horario> horarios;
    private Prioridade prioridade;


    /** Construtor adequado para o caso do usuário
     *  não querer colocar foto na criação da atividade.
     */
    public Atividade(String nome, Prioridade prioridade){
        this.nome = nome;
        this.prioridade = prioridade;
        this.horarios = new ArrayList<>();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Collection<Horario> getHorarios() {
        return horarios;
    }


    public Prioridade getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(Prioridade prioridade) {
        this.prioridade = prioridade;
    }

    public void registrarNovoHorario(Horario tempoInvestido){
        horarios.add(tempoInvestido);
    }

    /**
     * Calcula o total de hora de todos os horários cadastrados em uma determinada atividade.
     * */
    public int calcularTotalDeHorasInvestidas(){
        int total = 0;
        for (Horario horario: getHorarios()){
            total += horario.getTotalHorasInvestidas();
        }

        return total;
    }

    /** Esse método se faz necessário para o processo de ordenação, tendo em vista que o sistema não
     * tem como supor, por exemplo, que a prioridade ALTA deve vir primeiro que Média, que por sua vez
     * deve vir primeiro que BAIXA.
     * */
    public int retornarPesoDaPrioridade(){
        return prioridade.getPeso();
    }

}
