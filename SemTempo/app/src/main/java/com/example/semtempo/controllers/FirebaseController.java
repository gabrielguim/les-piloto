package com.example.semtempo.controllers;

import com.example.semtempo.model.Atividade;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mafra on 24/11/2016.
 */
public class FirebaseController {

    public static final String FIREBASE_URL = "https://sem-tempo.firebaseio.com/";
    public static final String ATIVIDADES = "activities";

    private static Firebase firebase;

    public static Firebase getFirebase(){
        if( firebase == null ){
            firebase = new Firebase(FIREBASE_URL);
        }
        return firebase;
    }

    public static void saveActivity(String user, Atividade activity){
        Firebase firebaseRef = getFirebase();
        Firebase activitiesReference = firebaseRef.child(user).child(ATIVIDADES);
        Firebase novaAtividade = activitiesReference.push();
        novaAtividade.setValue(activity);
    }

    public static void retrieveActivities(String user, final OnGetDataListener listener){
        final Firebase atividadesRef = getFirebase().child(user).child(ATIVIDADES);
        final List<Atividade> lista = new ArrayList<>();

        atividadesRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                Atividade a = dataSnapshot.getValue(Atividade.class);
                lista.add(a);
                listener.onSuccess(lista);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {
                System.out.println("FILHO MODIFICADO!");

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                System.out.println("onChildRemoved");
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {
                System.out.println("onChildMoved");
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("onCancelled");
            }


        });
    }


}
