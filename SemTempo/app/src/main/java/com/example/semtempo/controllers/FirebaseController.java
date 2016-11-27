package com.example.semtempo.controllers;

import com.example.semtempo.model.Atividade;
import com.example.semtempo.database.OnGetDataListener;
import com.example.semtempo.model.Horario;
import com.example.semtempo.model.Prioridade;
import com.example.semtempo.utils.Utils;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.GenericTypeIndicator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

/**
 * Created by Mafra on 24/11/2016.
 */
public class FirebaseController {

    public static final String FIREBASE_URL = "https://sem-tempo.firebaseio.com/";
    public static final String ATIVIDADES = "activities";
    public static final String HORARIOS = "horariosRealizDaAtv";
    public static final String TOTAL_DE_HORAS = "totalHorasInvestidas";
    public static final String DATA = "data";
    public static final String NOME = "nomeDaAtv";
    public static final String PRIORIDADE = "prioridade";
    public static final String ID = "id";
    public static final String SEMANA = "semana";

    private static Firebase firebase;
    public static Firebase getFirebase(){
        if( firebase == null ){
            firebase = new Firebase(FIREBASE_URL);
        }
        return firebase;
    }

    public static void saveUser(String user){
//        Atividade a = new Atividade("Cachaça", Prioridade.ALTA);
//        Atividade b = new Atividade("Estudar", Prioridade.BAIXA);
        List<Atividade> atv = new ArrayList<>();
//        a.registrarNovoHorario(new Horario(5, new GregorianCalendar()));
//        atv.add(a);
//        atv.add(b);
//        getFirebase().child(user).setValue(atv);
    }

    public static void saveActivity(String user, Atividade activity){
        Firebase firebaseRef = getFirebase();
        Firebase activitiesReference = firebaseRef.child(user).child(ATIVIDADES);
        Firebase novaAtividade = activitiesReference.push();
        System.out.println(novaAtividade.getKey());
        activity.setId(novaAtividade.getKey());
        novaAtividade.setValue(activity);
    }

    public static void saveNewHourActivity(String user, Atividade atividade, Horario horario){
        Firebase firebaseRef = getFirebase();
        Firebase activitiesReference = firebaseRef.child(user).child(ATIVIDADES);
        String key = atividade.getId();

        if(key != null){
            Firebase novoHorario = activitiesReference.child(key).child(HORARIOS).push();
            novoHorario.setValue(horario);
        } else {
            System.out.println("O NOVO HORARIO NAO FOI SALVO - KEY DA ATIVIDADE NAO EXISTE");
        }

    }

    public static void retrieveActivities(String user, final OnGetDataListener listener){
        final Firebase atividadesRef = getFirebase().child(user).child(ATIVIDADES);
        final List<Atividade> lista = new ArrayList<>();

        atividadesRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                Atividade a = (Atividade) dataSnapshot.getValue(Atividade.class);
                lista.add(a);
                listener.onSuccess(lista);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {


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

//    public static void retrieveActivities(String user, final OnGetDataListener listener){
//        System.out.println("Aqui entrou tambem");
//        System.out.println(user);
//
//        final List<Atividade> lista = new ArrayList<>();
//        final Firebase atividadesRef = getFirebase().child(user).child(ATIVIDADES);
//
//
//        atividadesRef.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
//                Map<String, Object> atividade = (Map<String, Object>)dataSnapshot.getValue();
//                String nome = (String) atividade.get(NOME);
//                String prioridade = (String) atividade.get(PRIORIDADE);
//                String id = (String) atividade.get(ID);
//
//                GenericTypeIndicator<List<Atividade>> t = new GenericTypeIndicator<List<Atividade>>() {};
//                List<Object> lista_horarios = (List<Object>) atividade.get(HORARIOS);
//
//                Atividade novaAtividade = new Atividade(nome, convertePrioridade(prioridade));
//                novaAtividade.setId(id);
//
//                List<Horario> horarios_da_atividade = recuperaListaDeHorarios(lista_horarios);
//
//                novaAtividade.setListaHorarios(horarios_da_atividade);
//                lista.add(novaAtividade);
//
//                System.out.println("IMPRIMINDO STATUS DA ATIVIDADE " + nome);
//                System.out.println("NOME DA ATIVIDADE " + novaAtividade.getNomeDaAtv());
//                System.out.println("PRIORIDADE = " + novaAtividade.getPrioridade());
//                System.out.println("IMPRIMINDO HORARIOS DESSA ATIVIDADE");
//                System.out.println("-----------------------------------");
//                for (Horario horario: novaAtividade.getHorariosRealizDaAtv()){
//                    System.out.println("SEMANA DO ANO: " + horario.getSemana());
//                    System.out.println("TEMPO GASTO: " + horario.getTotalHorasInvestidas());
//                }
//                System.out.println("-----------------------------------");
//
//
//                listener.onSuccess(lista);
//                //Consertar essa merda;
//
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {
//                System.out.println("Edit");
//                Map<String, Object> message = (Map<String, Object>)dataSnapshot.getValue();
//                System.out.println(message.get("totalDeHorasGasto"));
//                System.out.println(message.get("prioridade"));
//                System.out.println(message.get("totalDeHorasGasto"));
//                GenericTypeIndicator<List<String>> t = new GenericTypeIndicator<List<String>>() {};
//                try{
//                    List<Object> yourStringArray = (ArrayList<Object>) message.get("horariosRealizDaAtv");
//                    Map<String, Object> horarios = (Map<String, Object>) yourStringArray.get(0);
//                    System.out.println(horarios.get("totalHorasInvestidas"));
//                    System.out.println(horarios.get("dataQueRealizou"));
//                }catch(Exception e){
//                    System.out.println(e.getMessage());
//                }
//
//        }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {}
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {}
//
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
//
//            }
//
//
//        });
//    }


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

    private static List<Horario> recuperaListaDeHorarios(List<Object> lista_horarios){
        List<Horario> horarios_da_atividade = new ArrayList<>();
        if (lista_horarios != null){
            for (int i = 0; i < lista_horarios.size(); i++){
                Map<String, Object> horarios = (Map<String, Object>) lista_horarios.get(i);
                Integer totalDeHorasInvestidas = (int) (long) horarios.get(TOTAL_DE_HORAS);
                String data = (String) horarios.get(DATA);
                Calendar calendar = Utils.convertDateToCalendar(data);
                Horario horario = new Horario(totalDeHorasInvestidas, calendar);
                horarios_da_atividade.add(horario);
            }
        }

        return horarios_da_atividade;
    }


}
