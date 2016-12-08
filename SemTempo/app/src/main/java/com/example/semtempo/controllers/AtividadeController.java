package com.example.semtempo.controllers;

/**
 * Created by Mafra on 21/11/2016.
 */

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.semtempo.model.Atividade;
import com.example.semtempo.model.Horario;
import com.example.semtempo.model.Prioridade;
import com.example.semtempo.utils.Utils;

/**
 * Classe que faz atua sobre uma lista de atividades, filtrando-as sob os mais diferentes critérios
 */
public class AtividadeController {

    /**
     * Filtra uma lista de atividades pela semana
     *
     * @param allActivities Lista a ser filtrada
     * @param week    Semana pela qual será filtrada
     * @return filteredActivities Um mapa que relaciona atividade e horas gastas levando em conta apenas aquela semana
     *
     */
    public static Map<Atividade, Integer> filterActivitiesByWeekAndSpentHours(Collection<Atividade> allActivities, int week){
        Map<Atividade, Integer> filteredActivities = new HashMap<>();



        for (Atividade atividade: allActivities){
            Calendar atvCal = Utils.convertDateToCalendar(atividade.getHorariosRealizDaAtv().getData());
            Horario horario = atividade.getHorariosRealizDaAtv();

            if (atvCal.get(Calendar.WEEK_OF_YEAR) == week) {
                if (filteredActivities.containsKey(atividade)) {
                    int actualTime = filteredActivities.get(atividade);
                    filteredActivities.put(atividade, actualTime + horario.getTotalHorasInvestidas());
                } else {
                    filteredActivities.put(atividade, horario.getTotalHorasInvestidas());
                }
            }
        }

        return filteredActivities;
    }

    /**
     * Filtra uma lista de atividades pela semana
     *
     * @param allActivities Lista a ser filtrada
     * @param week    Semana pela qual será filtrada
     * @return filteredActivities Uma lista apenas com as atividades realizadas naquela semana
     */
    public static Collection<Atividade> filterActivitiesByWeek(Collection<Atividade> allActivities, int week) throws ParseException {
        Collection<Atividade> filteredActivities = new ArrayList<>();

        for (Atividade atividade: allActivities){
            System.out.println("filterActivitiesByWeek");
            System.out.println("Atividade: " + atividade.getNomeDaAtv());
            Calendar horario = Utils.convertDateToCalendar(atividade.getHorariosRealizDaAtv().getData());
            if (horario.get(Calendar.WEEK_OF_YEAR) == week){
                filteredActivities.add(atividade);
            }

        }

        return filteredActivities;
    }


    /**
     * Agrupa as atividades de acordo com sua prioridade
     *
     * @param allActivities Lista a ser filtrada
     * @return filteredActivities Um mapa que relaciona a prioridade das atividades e horas gastas com cada prioridade
     */
    public static Map<Prioridade, Integer> groupByPriority(Collection<Atividade> allActivities){

        Map<Prioridade, Integer> filteredActivities = new HashMap<>();
        Map<Atividade, Integer> allTimeActivity = TimeCalcuation(allActivities);

        for (Map.Entry<Atividade, Integer> entry : allTimeActivity.entrySet())
        {
            filteredActivities.put(entry.getKey().getPrioridade(), entry.getValue());
        }

        return filteredActivities;
    }

    public static Map<Atividade, Integer> TimeCalcuation(Collection<Atividade> allActivities){
        Map<Atividade, Integer> calculaTion = new HashMap<>();

        for (Atividade atividade: allActivities){
            if(calculaTion.containsKey(atividade)){
                int actualTime = calculaTion.get(atividade);
                calculaTion.put(atividade, actualTime +
                        atividade.getHorariosRealizDaAtv().getTotalHorasInvestidas());
            }else{
                calculaTion.put(atividade,
                        atividade.getHorariosRealizDaAtv().getTotalHorasInvestidas());
            }
        }


        return calculaTion;
    }

    /**
     * Filtra uma lista de atividades por tags passadas.
     * @param atividades Lista de atividades a ser filtrada.
     * @param tagsDaPesquisa Lista de String contendo as tags que devem estar contidas nas atividades.
     * @return Lista com as atividades que contém todos os filtros buscados.
     */
    public static List<Atividade> filterByTag (List<Atividade> atividades, List<String> tagsDaPesquisa){
        List<Atividade> atividadesFiltradas = new ArrayList<Atividade>();

        for (Atividade atividade : atividades) {
            List<String> tagsDaAtividade = atividade.getTags();

            if(isGreaterOrEqualThan(tagsDaAtividade, tagsDaPesquisa)) {
                if (tagsDaAtividade.containsAll(tagsDaPesquisa)){
                    atividadesFiltradas.add(atividade);
                }
            }
        }
        return atividadesFiltradas;
    }

    /**
     * Verifica se uma lista de tags é maior ou igual que outra
     * @param tags1 Lista de String a ser comparada
     * @param tags2 Lista de String base
     * @return True caso Tags1 tenha mais elementos que Tags2, caso contrario retorna False
     */
    private static boolean isGreaterOrEqualThan(List<String> tags1, List<String> tags2) {
        return tags1.size() >= tags2.size();
    }

}
