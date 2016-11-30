package com.example.semtempo.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Lucas on 28/11/2016.
 */
public class AtividadeTest {
    private Atividade atv1, atv2, atv3, atv4;
    private Prioridade p1, p2, p3;
    private Horario h0, h1, h2, h3, h4;
    private Tag t1, t2;

    @Before
    public void instanciacao(){
        p1 = Prioridade.ALTA;
        p2 = Prioridade.MEDIA;
        p3 = Prioridade.BAIXA;
        t1 = Tag.LAZER;
        t2 = Tag.TRABALHO;
        h0 = new Horario(4, new GregorianCalendar(2016,5,13));
        h1 = new Horario(3, new GregorianCalendar(2016,6,12));
        h2 = new Horario(1, new GregorianCalendar(2016,7,22));
        h3 = new Horario(8, new GregorianCalendar(2016,8,11));
        h4 = new Horario(4, new GregorianCalendar(2016,9,18));
        atv1 = new Atividade("Chutar cu de bebo", p3, h0);
        atv2 = new Atividade("Comer pudim", p3, h0);
        atv3 = new Atividade("Escrever artigo de ES", p1, h0);
        atv4 = new Atividade("Escutar Polentinha do Arrocha", p2, h0);
    }

    @Test
    public void testarGetESetNomeAtv(){
        String expected = "Chutar cu de bebo";
        String newName = "Quebrar o dedo";
        Assert.assertEquals(expected, atv1.getNomeDaAtv());
        atv1.setNomeDaAtv(newName);
        Assert.assertNotEquals(expected, atv1.getNomeDaAtv());
        Assert.assertEquals(newName, atv1.getNomeDaAtv());
    }

    @Test
    public void testeGetSemanaHorario(){
        Horario h10 = new Horario(3,new GregorianCalendar());
        Assert.assertFalse(h10.getSemana() == h1.getSemana());

    }

    @Test
    public void testeGetDataHorario(){
        Horario h10 = new Horario(3,new GregorianCalendar());
        Assert.assertFalse(h10.getData() == h1.getData());
        Assert.assertTrue(h10.getTotalHorasInvestidas() == h1.getTotalHorasInvestidas());

    }

    @Test
    public void testeEqualsAtv(){
        Assert.assertFalse(atv1.equals(atv2));
        Assert.assertFalse(atv1.equals(h1));

    }





}
