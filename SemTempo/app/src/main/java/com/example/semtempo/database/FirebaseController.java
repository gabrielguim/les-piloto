package com.example.semtempo.database;

import com.example.semtempo.controllers.model.Atividade;
import com.example.semtempo.controllers.model.Horario;
import com.example.semtempo.controllers.model.Prioridade;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

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
      //  a.registrarNovoHorario(new Horario(5, new GregorianCalendar()));
        // /atv.add(a);
        atv.add(b);
        getFirebase().child(user).setValue(atv);
    }

    public static void saveActivity(String user, Atividade activity){
        //TODO

    }

    public static void retrieveActivities(String user){
        System.out.println("Aqui entrou tambem");
        System.out.println(user);
        getFirebase().child(user).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                Map<String, Object> message = (Map<String, Object>)dataSnapshot.getValue();
                System.out.println(message.get("totalDeHorasGasto"));
                System.out.println(message.get("prioridade"));
                System.out.println(message.get("totalDeHorasGasto"));

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {
                System.out.println("Edit");
                Map<String, Object> message = (Map<String, Object>)dataSnapshot.getValue();
                System.out.println(message.get("totalDeHorasGasto"));
                System.out.println(message.get("prioridade"));
                System.out.println(message.get("totalDeHorasGasto"));

        }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }


        });
    }



    public static void cleanUserDate(String user){
        getFirebase().child(user).removeValue();
    }

}
