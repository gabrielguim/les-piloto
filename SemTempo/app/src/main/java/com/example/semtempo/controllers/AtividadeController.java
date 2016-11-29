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
    public static Collection<Atividade> filterActivitiesByWeek(Collection<Atividade> allActivities, int week) {
        Collection<Atividade> filteredActivities = new ArrayList<>();

        for (Atividade atividade: allActivities){
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
        Map<Atividade, Integer> allTimeActivity = TimeCalculation(allActivities);

        for (Map.Entry<Atividade, Integer> entry : allTimeActivity.entrySet())
        {
            filteredActivities.put(entry.getKey().getPrioridade(), entry.getValue());
        }

        return filteredActivities;
    }

    public static Map<Atividade, Integer> TimeCalculation(Collection<Atividade> allActivities){
        Map<Atividade, Integer> calculation = new HashMap<>();

        for (Atividade atividade: allActivities){
            if(calculation.containsKey(atividade)){
                int actualTime = calculation.get(atividade);
                calculation.put(atividade, actualTime +
                        atividade.getHorariosRealizDaAtv().getTotalHorasInvestidas());
            }else{
                calculation.put(atividade,
                        atividade.getHorariosRealizDaAtv().getTotalHorasInvestidas());
            }
        }


        return calculation;
    }

    public static List<Integer> filterByDayAndSpentHours(List<Atividade> atividades) {
        List<Integer> filteredTasks = new ArrayList<>();

        Map<Integer, Integer> atividades_data = new HashMap<>();
        int[] weekDays = {Calendar.SUNDAY, Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY, Calendar.SATURDAY};

        for (int i = 0; i < weekDays.length; i++) {
            atividades_data.put(weekDays[i], 0);
        }

        for (Atividade atividade: atividades){
            Calendar horario = Utils.convertDateToCalendar(atividade.getHorariosRealizDaAtv().getData());
            atividades_data.put(horario.get(Calendar.DAY_OF_WEEK), atividades_data.get(horario.get(Calendar.DAY_OF_WEEK)) + atividade.getHorariosRealizDaAtv().getTotalHorasInvestidas());
        }

        for (int i = 0; i < weekDays.length; i++) {
            filteredTasks.add(atividades_data.get(weekDays[i]));
        }

        return filteredTasks;
    }

    public static int getTotalSpentHoursByWeek(List<Atividade> atividades) {
        int totalDeHoras = 0;

        for (int i = 0; i < atividades.size(); i++) {
            totalDeHoras += atividades.get(i).getHorariosRealizDaAtv().getTotalHorasInvestidas();
        }

        return totalDeHoras;
    }
}
