package com.example.semtempo.services;

/**
 * Created by Mafra on 21/11/2016.
 */

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.semtempo.model.Atividade;
import com.example.semtempo.model.Horario;
import com.example.semtempo.model.Prioridade;
import com.example.semtempo.model.Tag;
import com.example.semtempo.utils.Utils;

import static android.content.ContentValues.TAG;

/**
 * Classe que faz atua sobre uma lista de atividades, filtrando-as sob os mais diferentes critérios
 */
public class AtividadeService {

    /**
     * Filtra uma lista de atividades pela semana
     *
     * @param allActivities Lista a ser filtrada
     * @param week    Semana pela qual será filtrada
     * @return filteredActivities Um mapa que relaciona atividade e horas gastas levando em conta apenas aquela semana
     *
     */
    public static Map<Atividade, Integer> groupActivitiesByWeekAndSpentHours(List<Atividade> allActivities, int week){
        Map<Atividade, Integer> filteredActivities = new HashMap<>();

        for (Atividade atividade: allActivities){
            Horario horario = atividade.getHorario();

            if (horario.getSemana() == week) {
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
    public static List<Atividade> filterActivitiesByWeek(List<Atividade> allActivities, int week) {
        List<Atividade> filteredActivities = new ArrayList<>();

        for (Atividade atividade: allActivities){
            if (atividade.getHorario().getSemana() == week){
                filteredActivities.add(atividade);
            }

        }

        return filteredActivities;
    }

    public static List<Integer> filterByDayAndSpentHours(List<Atividade> atividades) {
        List<Integer> filteredTasks = new ArrayList<>();

        Map<Integer, Integer> atividades_data = new HashMap<>();
        int[] weekDays = {Calendar.SUNDAY, Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY, Calendar.SATURDAY};

        for (int i = 0; i < weekDays.length; i++) {
            atividades_data.put(weekDays[i], 0);
        }

        for (Atividade atividade: atividades){
            Calendar horario = Utils.convertDateToCalendar(atividade.getHorario().getData());
            atividades_data.put(horario.get(Calendar.DAY_OF_WEEK), atividades_data.get(horario.get(Calendar.DAY_OF_WEEK)) + atividade.getHorario().getTotalHorasInvestidas());
        }

        for (int i = 0; i < weekDays.length; i++) {
            filteredTasks.add(atividades_data.get(weekDays[i]));
        }

        return filteredTasks;
    }

    public static int getTotalSpentHours(List<Atividade> atividades) {
        int totalDeHoras = 0;

        for (int i = 0; i < atividades.size(); i++) {
            totalDeHoras += atividades.get(i).getHorario().getTotalHorasInvestidas();
        }

        return totalDeHoras;
    }

    /**
     * Inform the quantity of hours spent by the user categorized by Tag(SemCategoria, Lazer, Trabalho)
     *
     * @param activities List of all user activities
     * @return categoriesHours A map with informations about how much time
     * the user spent on the categorized(SemCategoria, Lazer, Trabalho) activity
     */
    public static Map<Tag, Integer> getTotalSpentHoursByCategories(List<Atividade> activities) {
        Map<Tag, Integer> categoriesHours = new HashMap<>();

        categoriesHours.put(Tag.SEMCATEGORIA, 0);
        categoriesHours.put(Tag.LAZER, 0);
        categoriesHours.put(Tag.TRABALHO, 0);

        for (int i = 0; i < activities.size(); i++) {
            Atividade atv = activities.get(i);
            Tag tag = atv.getTag();
            int horas = atv.getHorario().getTotalHorasInvestidas();

            if(tag == null){
                int actualTime = categoriesHours.get(Tag.SEMCATEGORIA);
                categoriesHours.put(Tag.SEMCATEGORIA, actualTime + horas);
            }else{
                int actualTime = categoriesHours.get(tag);
                categoriesHours.put(tag, actualTime + horas);
            }
        }

        return categoriesHours;
    }

    /**
     * Inform the quantity of hours spent by the user on the actual week categorized by Tag(SemCategoria, Lazer, Trabalho)
     *
     * @param activities List of all user activities
     * @param actualWeek Actual system week
     * @return categoriesHours A map with informations about how much time in the actual week
     *  the user spent on the categorized(SemCategoria, Lazer, Trabalho) activity
     */
    public static Map<Tag, Integer> getTotalSpentHoursByCategoriesActWeek(List<Atividade> activities, int actualWeek) {
        Map<Tag, Integer> categoriesHours = new HashMap<>();

        categoriesHours.put(Tag.SEMCATEGORIA, 0);
        categoriesHours.put(Tag.LAZER, 0);
        categoriesHours.put(Tag.TRABALHO, 0);

        for (int i = 0; i < activities.size(); i++) {
            Atividade atv = activities.get(i);
            Horario horario = atv.getHorario();
            int atvWeek = horario.getSemana();

            if(atvWeek == actualWeek){
                Tag tag = atv.getTag();
                int horas = atv.getHorario().getTotalHorasInvestidas();

                if(tag == null){
                    int actualTime = categoriesHours.get(Tag.SEMCATEGORIA);
                    categoriesHours.put(Tag.SEMCATEGORIA, actualTime + horas);
                }else{
                    int actualTime = categoriesHours.get(tag);
                    categoriesHours.put(tag, actualTime + horas);
                }
            }
        }

        return categoriesHours;
    }

    public static boolean registerActivityYesterday(List<Atividade> activities){
        Calendar yesterday = new GregorianCalendar();
        yesterday.add(Calendar.DATE, -1);
        int month = yesterday.get(Calendar.MONTH);
        int day = yesterday.get(Calendar.DAY_OF_MONTH);
        int year = yesterday.get(Calendar.YEAR);

        for (Atividade activity : activities){
            Calendar data = activity.getHorario().getDataQueRealizou();
            if (data.get(Calendar.MONTH) == month && data.get(Calendar.DAY_OF_MONTH) == day && data.get(Calendar.YEAR) == year){
                return true;
            }
        }

        return false;
    }
}
