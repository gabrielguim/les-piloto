package com.example.semtempo.controllers;

import com.example.semtempo.model.Atividade;
import com.example.semtempo.model.Horario;
import com.example.semtempo.model.Prioridade;
import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by Mafra on 24/11/2016.
 */
public class FirebaseController {

    public static final String FIREBASE_URL = "https://sem-tempo.firebaseio.com/";

    private static Firebase firebase;

    public static Firebase getFirebase(){
        if( firebase == null ){
            firebase = new Firebase(FIREBASE_URL);
        }
        return firebase;
    }

    public static void saveUser(String user){
        Horario horarioAux = new Horario(3, new GregorianCalendar());
        Atividade a = new Atividade("Cachaça", Prioridade.ALTA, horarioAux);
        Atividade b = new Atividade("Estudar", Prioridade.BAIXA, horarioAux);
        List<Atividade> atv = new ArrayList<>();
        a.registrarNovoHorario(new Horario(5, new GregorianCalendar()));
        atv.add(a);
        atv.add(b);
        getFirebase().child(user).setValue(atv);
    }

    public static void saveActivity(String user, Atividade activity){
        //TODO
    }

    public static void retrieveActivities(String user){
        //TODO
    }

    public static void cleanUserDate(String user){
        getFirebase().child(user).removeValue();
    }

}
