package com.example.semtempo.services;

/**
 * Created by Mafra on 21/11/2016.
 */

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.semtempo.model.Atividade;
import com.example.semtempo.model.Categoria;
import com.example.semtempo.model.Horario;
import com.example.semtempo.utils.Utils;

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
     * Inform the quantity of hours spent by the user categorized by Categoria(SemCategoria, Lazer, Trabalho)
     *
     * @param activities List of all user activities
     * @return categoriesHours A map with informations about how much time
     * the user spent on the categorized(SemCategoria, Lazer, Trabalho) activity
     */
    public static Map<Categoria, Integer> getTotalSpentHoursByCategories(List<Atividade> activities) {
        Map<Categoria, Integer> categoriesHours = new HashMap<>();

        categoriesHours.put(Categoria.SEMCATEGORIA, 0);
        categoriesHours.put(Categoria.LAZER, 0);
        categoriesHours.put(Categoria.TRABALHO, 0);

        for (int i = 0; i < activities.size(); i++) {
            Atividade atv = activities.get(i);
            Categoria categoria = atv.getCategoria();
            int horas = atv.getHorario().getTotalHorasInvestidas();

            if(categoria == null){
                int actualTime = categoriesHours.get(Categoria.SEMCATEGORIA);
                categoriesHours.put(Categoria.SEMCATEGORIA, actualTime + horas);
            }else{
                int actualTime = categoriesHours.get(categoria);
                categoriesHours.put(categoria, actualTime + horas);
            }
        }

        return categoriesHours;
    }

    /**
     * Inform the quantity of hours spent by the user on the actual week categorized by Categoria(SemCategoria, Lazer, Trabalho)
     *
     * @param activities List of all user activities
     * @param actualWeek Actual system week
     * @return categoriesHours A map with informations about how much time in the actual week
     *  the user spent on the categorized(SemCategoria, Lazer, Trabalho) activity
     */
    public static Map<Categoria, Integer> getTotalSpentHoursByCategoriesActWeek(List<Atividade> activities, int actualWeek) {
        Map<Categoria, Integer> categoriesHours = new HashMap<>();

        categoriesHours.put(Categoria.SEMCATEGORIA, 0);
        categoriesHours.put(Categoria.LAZER, 0);
        categoriesHours.put(Categoria.TRABALHO, 0);

        for (int i = 0; i < activities.size(); i++) {
            Atividade atv = activities.get(i);
            Horario horario = atv.getHorario();
            int atvWeek = horario.getSemana();

            if(atvWeek == actualWeek){
                Categoria categoria = atv.getCategoria();
                int horas = atv.getHorario().getTotalHorasInvestidas();

                if(categoria == null){
                    int actualTime = categoriesHours.get(Categoria.SEMCATEGORIA);
                    categoriesHours.put(Categoria.SEMCATEGORIA, actualTime + horas);
                }else{
                    int actualTime = categoriesHours.get(categoria);
                    categoriesHours.put(categoria, actualTime + horas);
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
            int monthOfActivity = activity.getHorario().getMes();
            int dayOfActivity = activity.getHorario().getDia();
            int yearOfActivity = activity.getHorario().getAno();
            if (monthOfActivity == month && dayOfActivity == day && yearOfActivity == year){
                return true;
            }
        }

        return false;
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
