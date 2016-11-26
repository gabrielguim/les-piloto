package com.example.semtempo.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Mafra on 26/11/2016.
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
    public void testarSeAddHorarioIncrementaHrs(){
        Assert.assertTrue(atv1.calcularTotalDeHorasInvestidas() == 4);
        atv1.registrarNovoHorario(h1);
        Assert.assertTrue(atv1.calcularTotalDeHorasInvestidas() == 7);
        atv1.registrarNovoHorario(h3);
        Assert.assertTrue(atv1.calcularTotalDeHorasInvestidas() == 15);
        atv1.registrarNovoHorario(h2);
        Assert.assertTrue(atv1.calcularTotalDeHorasInvestidas() == 16);
        atv1.registrarNovoHorario(h4);
        Assert.assertTrue(atv1.calcularTotalDeHorasInvestidas() == 20);
    }

    @Test
    public void testarGetESetNomeAtv(){
        String expected = "Chutar cu de bebo";
        String newName = "Quebrar o dedo";
        Assert.assertEquals(expected, atv1.getNome());
        atv1.setNome(newName);
        Assert.assertNotEquals(expected, atv1.getNome());
        Assert.assertEquals(newName, atv1.getNome());
    }

    @Test
    public void testarGetESetPrioridadeAtv(){
        Prioridade expected = Prioridade.BAIXA;
        Assert.assertTrue(expected == atv1.getPrioridade());
        Assert.assertTrue(expected.getPeso() == atv1.retornarPesoDaPrioridade());
        Prioridade newPrioridade = Prioridade.ALTA;
        atv1.setPrioridade(newPrioridade);
        Assert.assertNotEquals(expected, atv1.getPrioridade());
        Assert.assertEquals(newPrioridade, atv1.getPrioridade());
    }

    @Test
    public void testGetDataQRealizou(){
        Assert.assertFalse(h1.getDataQueRealizou() == h2.getDataQueRealizou() );
    }

}
