package com.example.semtempo.database;

import android.provider.ContactsContract;

import com.example.semtempo.controllers.model.Atividade;
import com.example.semtempo.controllers.model.Horario;
import com.example.semtempo.controllers.model.Prioridade;
import com.example.semtempo.utils.Utils;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.GenericTypeIndicator;
import com.firebase.client.MutableData;
import com.firebase.client.Transaction;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
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
        List<Atividade> atividades = Utils.getLista();
        String key = null;

        for(Atividade a: atividades){
            if(atividade.getNomeDaAtv().equals(a.getNomeDaAtv())){
                key = a.getId();
            }
        }

        if(key != null){
            Firebase novoHorario = activitiesReference.child(key).child("horarios").push();
            novoHorario.setValue(horario);
        }


    }



    public static List<Atividade> retrieveActivities(String user, final OnGetDataListener listener){
        System.out.println("Aqui entrou tambem");
        System.out.println(user);

        final List<Atividade> lista = new ArrayList<>();
        final Firebase atividadesRef = getFirebase().child(user).child("activities");


        atividadesRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                Map<String, Object> message = (Map<String, Object>)dataSnapshot.getValue();
                String nome = (String) message.get("nomeDaAtv");
                String prioridade = (String) message.get("prioridade");
                String id = (String) message.get("id");

                GenericTypeIndicator<List<Atividade>> t = new GenericTypeIndicator<List<Atividade>>() {};
                List<Object> yourStringArray = (List<Object>) message.get("horariosRealizDaAtv");

                Atividade atividade = new Atividade(nome, convertePrioridade(prioridade));
                atividade.setId(id);

                List<Horario> horarios_da_atividade = new ArrayList<Horario>();

                if (yourStringArray != null){
                    for (int i = 0; i < yourStringArray.size(); i++){
                        Map<String, Object> horarios = (Map<String, Object>) yourStringArray.get(i);
                        Integer totalDeHorasInvestidas = (int) (long) horarios.get("totalHorasInvestidas");
                        Integer semanaDoAno = (int) (long) horarios.get("semana");
                        Horario horario = new Horario(totalDeHorasInvestidas, semanaDoAno);
                        horarios_da_atividade.add(horario);
                    }
                }

                atividade.setListaHorarios(horarios_da_atividade);
                lista.add(atividade);


                System.out.println("IMPRIMINDO STATUS DA ATIVIDADE " + nome);
                System.out.println("NOME DA ATIVIDADE " + atividade.getNomeDaAtv());
                System.out.println("PRIORIDADE = " + atividade.getPrioridade());
                System.out.println("IMPRIMINDO HORARIOS DESSA ATIVIDADE");
                System.out.println("-----------------------------------");
                for (Horario horario: atividade.getHorariosRealizDaAtv()){
                    System.out.println("SEMANA DO ANO: " + horario.getSemana());
                    System.out.println("TEMPO GASTO: " + horario.getTotalHorasInvestidas());
                }
                System.out.println("-----------------------------------");


                listener.onSuccess(lista);

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

    private static Prioridade convertePrioridade(String prioridade){
        System.out.println(prioridade);
        if (prioridade.equals("ALTA")){
            return Prioridade.ALTA;
        } else if (prioridade.equals("MEDIA")){
            return Prioridade.MEDIA;
        } else {
            return Prioridade.BAIXA;
        }
    }


}
