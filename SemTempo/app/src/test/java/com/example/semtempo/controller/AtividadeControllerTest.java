package com.example.semtempo.controller;

import com.example.semtempo.controllers.AtividadeController;
import com.example.semtempo.model.Atividade;
import com.example.semtempo.model.Horario;
import com.example.semtempo.model.Prioridade;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by gustavooliveira on 12/8/16.
 */
public class AtividadeControllerTest {
    private Atividade atv1, atv2, atv3, atv4;
    private Prioridade p1, p2, p3;
    List<String> tags1, tags2, tags3, tags4;
    List<Atividade> listaDeAtividades;

    @Before
    public void setup() {
        p1 = Prioridade.ALTA;
        p2 = Prioridade.MEDIA;
        p3 = Prioridade.BAIXA;
        tags1 = new ArrayList<String>();
        tags1.add("Zoeira");
        tags1.add("Diversão");
        tags1.add("ChutoMesmo");

        tags2 = new ArrayList<String>();
        tags2.add("Gordices");
        tags2.add("MelhorPudim");

        tags3 = new ArrayList<String>();
        tags3.add("Estudos");

        tags4 = new ArrayList<String>();
        tags4.add("Estudos");

        atv1 = new Atividade("Chutar cu de bebo", p3, new Horario(2, new GregorianCalendar()), tags1);
        atv2 = new Atividade("Comer pudim", p3, new Horario(4, new GregorianCalendar()), tags2);
        atv3 = new Atividade("Escrever artigo de ES", p1, new Horario(5, new GregorianCalendar()), tags3);
        atv4 = new Atividade("Escutar Polentinha do Arrocha", p2, new Horario(7, new GregorianCalendar()), tags4);

        listaDeAtividades = new ArrayList<Atividade>();

        listaDeAtividades.add(atv1);
        listaDeAtividades.add(atv2);
        listaDeAtividades.add(atv3);
        listaDeAtividades.add(atv4);
    }


    @Test
    public void TestFilterByTag() {
        List<String> tagsBuscadas = new ArrayList<String>();
        tagsBuscadas.add("Estudos");

        List<Atividade> atividadesFiltradas;

        atividadesFiltradas = AtividadeController.filterByTag(listaDeAtividades, tagsBuscadas);

        Assert.assertEquals(2, atividadesFiltradas.size());

        tagsBuscadas.remove(0);
        tagsBuscadas.add("Zoeira");
        tagsBuscadas.add("Diversão");

        atividadesFiltradas = AtividadeController.filterByTag(listaDeAtividades, tagsBuscadas);

        Assert.assertEquals(1, atividadesFiltradas.size());

        tagsBuscadas.remove(0);
        tagsBuscadas.remove(0);

        atividadesFiltradas = AtividadeController.filterByTag(listaDeAtividades, tagsBuscadas);

        Assert.assertEquals(4, atividadesFiltradas.size());
    }
}
