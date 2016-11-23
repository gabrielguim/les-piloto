package controllers;

/**
 * Created by Mafra on 21/11/2016.
 */

import java.util.*;
import model.*;

/**
 * Classe que atua sobre uma lista de atividades, filtrando-as sob os mais diferentes critérios.
 */
public class AtividadeController {

    /**
     * Um retorno apropriado para representar o que a função executa diz respeito a um mapa
     * que relaciona as atividades com horas investidas nelas, levando em conta apenas a semana passada como parâmetro.
     */
    public static Map<Atividade, Integer> filtrarAtvsPorTempoInvestido(Collection<Atividade> atvsASeremFiltradas, int semanaEscolhida){

        Map<Atividade, Integer> atvsFiltradas = new HashMap<>();

        for (Atividade atividade: atvsASeremFiltradas){
            for (Horario horario: atividade.retornarHorariosRealizDaAtv()){
                if (horario.retornarDataQueRealizou().get(Calendar.WEEK_OF_YEAR) == semanaEscolhida){
                    if (atvsFiltradas.containsKey(atividade)){
                        int actualTime = atvsFiltradas.get(atividade);
                        atvsFiltradas.put(atividade, actualTime + horario.retornarTotalHorasInvestidas());
                    } else {
                        atvsFiltradas.put(atividade, horario.retornarTotalHorasInvestidas());
                    }
                }
            }
        }

        return atvsFiltradas;
    }

    /**
     * Um retorno apropriado para representar o que a função executa diz respeito a uma coleção
     * apenas com as atividades realizadas naquela semana.
     */
    public static Collection<Atividade> filtraAtvsPelaSemana(Collection<Atividade> atvsASeremFiltradas, int semanaEscolhida){
        Collection<Atividade> atvsFiltradas = new ArrayList<>();

        for (Atividade atividade: atvsASeremFiltradas){
            for (Horario horario: atividade.retornarHorariosRealizDaAtv()){
                if (horario.retornarDataQueRealizou().get(Calendar.WEEK_OF_YEAR) == semanaEscolhida){
                    atvsFiltradas.add(atividade);
                    break;
                }
            }
        }
        return atvsFiltradas;
    }


    /**
     * Um retorno apropriado para representar o que a função executa diz respeito a um mapa
     * que relaciona a prioridade da atividade com as horas investidas nelas.
     */
    public static Map<Prioridade, Integer> agrupaAtvsPorPrioridade(Collection<Atividade> atvsASeremFiltradas){

        Map<Prioridade, Integer> atvsFiltradas = new HashMap<>();

        for (Atividade atividade: atvsASeremFiltradas){
            atvsFiltradas.put(atividade.retornarPrioridade(), atividade.calcularTotalDeHorasInvestidas());
        }

        return atvsFiltradas;
    }

}
