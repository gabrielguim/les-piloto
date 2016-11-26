package com.example.semtempo.database;

import com.example.semtempo.controllers.model.Atividade;
import com.example.semtempo.controllers.model.Horario;
import com.example.semtempo.controllers.model.Prioridade;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.GenericTypeIndicator;
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
        a.registrarNovoHorario(new Horario(5, new GregorianCalendar()));
        atv.add(a);
        atv.add(b);
        getFirebase().child(user).setValue(atv);
    }

    public static void saveActivity(String user, Atividade activity){
        Firebase firebaseRef = getFirebase();
        Firebase activitiesReference = firebaseRef.child(user).child("activities");
        Firebase novaAtividade = activitiesReference.push();
        System.out.println(novaAtividade.getKey());
        activity.setId(novaAtividade.getKey());
        novaAtividade.setValue(activity);
    }

    public static void saveNewHourActivity(String user, Atividade atividade, Horario horario){
        Firebase firebaseRef = getFirebase();
        Firebase activitiesReference = firebaseRef.child(user).child("activities");
        Firebase activityRef = activitiesReference.child(atividade.getId());
        Firebase horariosRef = activityRef.child("horarios");
        Firebase novoHorario = horariosRef.push();
        novoHorario.setValue(horario);
    }

    public static List<Atividade> retrieveActivities(String user){
        System.out.println("Aqui entrou tambem");
        System.out.println(user);

        final List<Atividade> lista = new ArrayList<>();

        final Firebase atividadesRef = getFirebase().child(user).child("activities");

        atividadesRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                final String title = (String) dataSnapshot.child("nomeDaAtv").getValue();
                String description = (String) dataSnapshot.child("prioridade").getValue();
                String id = (String) dataSnapshot.child("id").getValue();
                System.out.println(title);
                System.out.println(description);

                Atividade atv = new Atividade(title, Prioridade.ALTA);
                lista.add(atv);

                Firebase horariosRef = atividadesRef.child(id).child("horarios");

                horariosRef.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot snapHoras, String s) {
                        Long horasGastas = (Long) snapHoras.child("totalHorasInvestidas").getValue();
                        System.out.println("HORARIO " + title + " HORAS INVESTIDAS: " + horasGastas);
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {
                System.out.println("Edit");
                Map<String, Object> message = (Map<String, Object>)dataSnapshot.getValue();
                System.out.println(message.get("totalDeHorasGasto"));
                System.out.println(message.get("prioridade"));
                System.out.println(message.get("totalDeHorasGasto"));
                GenericTypeIndicator<List<String>> t = new GenericTypeIndicator<List<String>>() {};
                try{
                    List<Object> yourStringArray = (ArrayList<Object>) message.get("horariosRealizDaAtv");
                    Map<String, Object> horarios = (Map<String, Object>) yourStringArray.get(0);
                    System.out.println(horarios.get("totalHorasInvestidas"));
                    System.out.println(horarios.get("dataQueRealizou"));
                }catch(Exception e){
                    System.out.println(e.getMessage());
                }

        }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }


        });

        return lista;
    }



    public static void cleanUserDate(String user){
        getFirebase().child(user).removeValue();
    }

}
