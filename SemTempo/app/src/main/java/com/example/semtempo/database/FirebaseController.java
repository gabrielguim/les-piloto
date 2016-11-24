package com.example.semtempo.database;

import com.example.semtempo.controllers.model.Atividade;
import com.example.semtempo.controllers.model.Prioridade;
import com.firebase.client.Firebase;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mafra on 24/11/2016.
 */
public class FirebaseController {

    private static Firebase firebase;

    public static Firebase getFirebase(){
        if( firebase == null ){
            firebase = new Firebase("https://sem-tempo.firebaseio.com/");
        }
        return firebase;
    }

    public static void saveUser(String email){
        Firebase usrReference = getFirebase();
        Atividade a = new Atividade("Futsal", Prioridade.ALTA);
        Atividade b = new Atividade("Estudar", Prioridade.BAIXA);
        List<Atividade> atv = new ArrayList<>();
        atv.add(a);
        atv.add(b);
        usrReference.child(email).setValue(atv);
    }

    public static void saveActivity(String email, Atividade activity){
        //TODO
    }

    public void retrieveActivities(String email){
        //TODO
    }

}
