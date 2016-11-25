package controllers;

/**
 * Created by Mafra on 21/11/2016.
 */

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import model.Atividade;
import model.Horario;
import model.Prioridade;

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
                if (getWeek(horario) == week){
                    if (filteredActivities.containsKey(atividade)){
                        int actualTime = filteredActivities.get(atividade);
                        filteredActivities.put(atividade, actualTime + horario.getTotalHorasInvestidas());
                    } else {
                        filteredActivities.put(atividade, horario.getTotalHorasInvestidas());
                    }
                }
            }
        }

        return filteredActivities;
    }

    private static int getWeek(Horario horario){

        GregorianCalendar date = (GregorianCalendar) horario.getDataQueRealizou();
        Calendar cal = Calendar.getInstance();

        int month = date.get(GregorianCalendar.MONTH);
        int day = date.get(GregorianCalendar.DAY_OF_MONTH);
        int year = date.get(GregorianCalendar.YEAR);

        cal.set(year, month, day);
        int taskWeek = cal.get(Calendar.WEEK_OF_YEAR);

        return taskWeek;
    }

    /**
     * Filtra uma lista de atividades pela semana
     *
     * @param allActivities Lista a ser filtrada
     * @param week    Semana pela qual será filtrada
     * @return filteredActivities Uma lista apenas com as atividades realizadas naquela semana
     * @author            João Mafra
     */
    public static Collection<Atividade> filterActivitiesByWeek(Collection<Atividade> allActivities, int week){
        Collection<Atividade> filteredActivities = new ArrayList<>();

        for (Atividade atividade: allActivities){
            for (Horario horario: atividade.getHorariosRealizDaAtv()){
                if (getWeek(horario) == week){
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
