package model;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Lucas on 20/11/2016.
 */
public class Atividade {

    private String nomeDaAtv;
    private Foto fotoDaAtv;
    private Collection<Horario> horariosRealizDaAtv;
    private Tag categoriaDaAtv;
    private Prioridade prioridadeDaAtv;

    public Atividade(String nomeDaAtv, Foto fotoDaAtv, Tag categoriaDaAtv, Prioridade prioridadeDaAtv){
        this.nomeDaAtv = nomeDaAtv;
        this.fotoDaAtv = fotoDaAtv;
        this.categoriaDaAtv = categoriaDaAtv;
        this.prioridadeDaAtv = prioridadeDaAtv;
        this.horariosRealizDaAtv = new ArrayList<Horario>();
    }

    /** Construtor adequado para o caso do usuário
     *  não querer colocar foto na criação da atividade.
     */
    public Atividade(String nomeDaAtv, Prioridade prioridadeDaAtv){
        this.nomeDaAtv = nomeDaAtv;
        this.prioridadeDaAtv = prioridadeDaAtv;
        this.horariosRealizDaAtv = new ArrayList<Horario>();
    }

    public Foto retornarFoto() {
        return fotoDaAtv;
    }

    public void alterarFoto(Foto fotoEscolhida) {
        this.fotoDaAtv = fotoEscolhida;
    }

    public Collection<Horario> retornarHorariosRealizDaAtv() {
        return horariosRealizDaAtv;
    }

    public Tag retornarCategoria() {
        return categoriaDaAtv;
    }

    public void alterarCategoria(Tag categoriaAlvo) {
        this.categoriaDaAtv = categoriaAlvo;
    }

    public Prioridade retornarPrioridade() {
        return prioridadeDaAtv;
    }

    public void alterarPrioridade(Prioridade prioridadeDeInteresse) {
        this.prioridadeDaAtv = prioridadeDeInteresse;
    }

    public String retornarNome() {
        return nomeDaAtv;
    }

    public void alterarNome(String novoNomeEscolhido) {
        this.nomeDaAtv = novoNomeEscolhido;
    }

    public void registrarNovoHorario(Horario tempoInvestido){
        horariosRealizDaAtv.add(tempoInvestido);
    }

    /**
     * Retorna um booleano pelo fato de que se um horário
     * não consta na coleção, ele retornará falso.
     */
    public boolean removerHorario(Horario horarioEscolhido){
        return horariosRealizDaAtv.remove(horarioEscolhido);
    }

    public int calcularTotalDeHorasInvestidas(){
        int total = 0;
        for (Horario horario: retornarHorariosRealizDaAtv()){
            total += horario.retornarTotalHorasInvestidas();
        }

        return total;
    }


}
