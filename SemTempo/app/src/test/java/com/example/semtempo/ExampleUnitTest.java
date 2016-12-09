package com.example.semtempo;

import com.example.semtempo.model.Atividade;
import com.example.semtempo.model.Categoria;
import com.example.semtempo.model.Horario;
import com.example.semtempo.model.Prioridade;
import com.example.semtempo.services.AtividadeService;

import junit.framework.Assert;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void addition_isCorrect() throws Exception {
        Calendar now = new GregorianCalendar(2016,5,1);
        System.out.println(now.getTime().toString());
        now.add(Calendar.DATE, -1);
        System.out.println(now.getTime().toString());


    }

    @Test
    public void testRegisterActivityYesterday1(){
        Atividade atv1 = new Atividade("Chutar cu de bebo", Prioridade.ALTA, new Horario(2, new GregorianCalendar()), Categoria.LAZER);
        Atividade atv2 = new Atividade("Chutar cu de bebo", Prioridade.ALTA, new Horario(2, new GregorianCalendar()), Categoria.LAZER);
        Atividade atv3 = new Atividade("Chutar cu de bebo", Prioridade.ALTA, new Horario(2, new GregorianCalendar()), Categoria.LAZER);
        Atividade atv4 = new Atividade("Chutar cu de bebo", Prioridade.ALTA, new Horario(2, new GregorianCalendar()), Categoria.LAZER);

        List<Atividade> lista = new ArrayList<>();
        lista.add(atv1);
        lista.add(atv2);
        lista.add(atv3);
        lista.add(atv4);

        Assert.assertFalse(AtividadeService.registerActivityYesterday(lista));

    }

    @Test
    public void testRegisterActivityYesterday2(){
        Atividade atv1 = new Atividade("Chutar cu de bebo", Prioridade.ALTA, new Horario(2, new GregorianCalendar(2016,11,7)), Categoria.LAZER);
        Atividade atv2 = new Atividade("Chutar cu de bebo", Prioridade.ALTA, new Horario(2, new GregorianCalendar()), Categoria.LAZER);
        Atividade atv3 = new Atividade("Chutar cu de bebo", Prioridade.ALTA, new Horario(2, new GregorianCalendar()), Categoria.LAZER);
        Atividade atv4 = new Atividade("Chutar cu de bebo", Prioridade.ALTA, new Horario(2, new GregorianCalendar()), Categoria.LAZER);

        List<Atividade> lista = new ArrayList<>();
        lista.add(atv1);
        lista.add(atv2);
        lista.add(atv3);
        lista.add(atv4);

        Assert.assertTrue(AtividadeService.registerActivityYesterday(lista));

    }

    @Test
    public void testRegisterActivityYesterday3(){
        Atividade atv1 = new Atividade("Chutar cu de bebo", Prioridade.ALTA, new Horario(2, new GregorianCalendar(2016,11,7)), Categoria.LAZER);
        Atividade atv2 = new Atividade("Chutar cu de bebo", Prioridade.ALTA, new Horario(2, new GregorianCalendar(2016,11,7)), Categoria.LAZER);
        Atividade atv3 = new Atividade("Chutar cu de bebo", Prioridade.ALTA, new Horario(2, new GregorianCalendar(2016,11,7)), Categoria.LAZER);
        Atividade atv4 = new Atividade("Chutar cu de bebo", Prioridade.ALTA, new Horario(2, new GregorianCalendar(2016,11,7)), Categoria.LAZER);

        List<Atividade> lista = new ArrayList<>();
        lista.add(atv1);
        lista.add(atv2);
        lista.add(atv3);
        lista.add(atv4);

        Assert.assertTrue(AtividadeService.registerActivityYesterday(lista));

    }

    @Test
    public void testRegisterActivityYesterday4(){
        Atividade atv1 = new Atividade("Chutar cu de bebo", Prioridade.ALTA, new Horario(2, new GregorianCalendar(2016,11,6)), Categoria.LAZER);
        Atividade atv2 = new Atividade("Chutar cu de bebo", Prioridade.ALTA, new Horario(2, new GregorianCalendar(2016,11,6)), Categoria.LAZER);
        Atividade atv3 = new Atividade("Chutar cu de bebo", Prioridade.ALTA, new Horario(2, new GregorianCalendar(2016,11,8)), Categoria.LAZER);
        Atividade atv4 = new Atividade("Chutar cu de bebo", Prioridade.ALTA, new Horario(2, new GregorianCalendar(2016,11,8)), Categoria.LAZER);

        List<Atividade> lista = new ArrayList<>();
        lista.add(atv1);
        lista.add(atv2);
        lista.add(atv3);
        lista.add(atv4);

        Assert.assertFalse(AtividadeService.registerActivityYesterday(lista));

    }
}