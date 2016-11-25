package com.example.semtempo.database;

import com.example.semtempo.controllers.model.Atividade;
import com.example.semtempo.controllers.model.Horario;
import com.example.semtempo.controllers.model.Prioridade;
import com.firebase.client.Firebase;
import com.firebase.client.ValueEventListener;

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
        Atividade a = new Atividade("Cachaça", Prioridade.ALTA);
        Atividade b = new Atividade("Estudar", Prioridade.BAIXA);
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
