package com.example.semtempo.controllers;

/**
 * Created by Mafra on 21/11/2016.
 */

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.example.semtempo.controllers.model.Atividade;
import com.example.semtempo.controllers.model.Horario;
import com.example.semtempo.controllers.model.Prioridade;

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
     * @author            João Mafra
     */
    public static Map<Atividade, Integer> filterActivitiesByWeekAndSpentHours(Collection<Atividade> allActivities, int week){
        Map<Atividade, Integer> filteredActivities = new HashMap<>();



        for (Atividade atividade: allActivities){
            for (Horario horario: atividade.getHorariosRealizDaAtv()){
                try {
                    if (horario.getDataQueRealizou().get(Calendar.WEEK_OF_YEAR) == week) {
                        if (filteredActivities.containsKey(atividade)) {
                            int actualTime = filteredActivities.get(atividade);
                            filteredActivities.put(atividade, actualTime + horario.getTotalHorasInvestidas());
                        } else {
                            filteredActivities.put(atividade, horario.getTotalHorasInvestidas());
                        }
                    }
                }catch(Exception e){

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
     * @author            João Mafra
     */
    public static Collection<Atividade> filterActivitiesByWeek(Collection<Atividade> allActivities, int week) throws ParseException {
        Collection<Atividade> filteredActivities = new ArrayList<>();

        for (Atividade atividade: allActivities){
            for (Horario horario: atividade.getHorariosRealizDaAtv()){
                if (horario.getDataQueRealizou().get(Calendar.WEEK_OF_YEAR) == week){
                    filteredActivities.add(atividade);
                    break;
                }
            }
        }

        return filteredActivities;
    }


    /**
     * Agrupa as atividades de acordo com sua prioridade
     *
     * @param allActivities Lista a ser filtrada
     * @return filteredActivities Um mapa que relaciona a prioridade das atividades e horas gastas com cada prioridade
     * @author            João Mafra
     */
    public static Map<Prioridade, Integer> groupByPriority(Collection<Atividade> allActivities){

        Map<Prioridade, Integer> filteredActivities = new HashMap<>();

        for (Atividade atividade: allActivities){
            filteredActivities.put(atividade.getPrioridade(), atividade.getTotalDeHorasGasto());
        }

        return filteredActivities;
    }

}
