package com.example.semtempo.model;

import com.example.semtempo.services.AtividadeService;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by Lucas on 28/11/2016.
 */
public class AtividadeTest {
    private Atividade atv1, atv2, atv3, atv4;
    private Prioridade prioridadeAlta, prioridadeMedia, prioridadeBaixa;
    private Horario h0, h1, h2, h3, h4;
    private Categoria categoriaLazer, categoriaTrabalho, semCategoria;
    private Categoria categoriaAtv1,categoriaAtv2,categoriaAtv3;
    private List<Atividade> atvs;
    private List<Prioridade> expected;
    private List<Prioridade> priorities;
    private List<String> tagsAtv1;
    private List<String> tagsAtv2;
    private List<String> tagsAtv3;
    private List<String> tagsAtv4;

    @Before
    public void instanciacao(){
        atvs = new ArrayList<>();
        priorities = new ArrayList<>();
        expected = new ArrayList<>();
        tagsAtv1 = new ArrayList<>();
        tagsAtv2 = new ArrayList<>();
        tagsAtv3 = new ArrayList<>();
        tagsAtv4 = new ArrayList<>();
        prioridadeAlta = Prioridade.ALTA;
        prioridadeMedia = Prioridade.MEDIA;
        prioridadeBaixa = Prioridade.BAIXA;
        categoriaLazer = Categoria.LAZER;
        categoriaTrabalho = Categoria.TRABALHO;
        semCategoria = Categoria.SEMCATEGORIA;
        h0 = new Horario(4, new GregorianCalendar(2016,5,13));
        h1 = new Horario(3, new GregorianCalendar(2016,6,12));
        h2 = new Horario(1, new GregorianCalendar(2016,7,22));
        h3 = new Horario(8, new GregorianCalendar(2016,8,11));
        h4 = new Horario(4, new GregorianCalendar(2016,9,18));
        atv1 = new Atividade("Chutar cu de bebo", prioridadeBaixa, h0, categoriaLazer, tagsAtv1);
        atv2 = new Atividade("Comer pudim", prioridadeBaixa, h0, categoriaTrabalho, tagsAtv2);
        atv3 = new Atividade("Escrever artigo de ES", prioridadeAlta, h0, semCategoria, tagsAtv3);
        atv4 = new Atividade("Escutar Polentinha do Arrocha", prioridadeMedia, h0, categoriaLazer, tagsAtv4);
        atvs.add(atv1);
        atvs.add(atv2);
        atvs.add(atv3);
        atvs.add(atv4);
        categoriaAtv1 = atv1.getCategoria();
        categoriaAtv2 = atv2.getCategoria();
        categoriaAtv3 = atv3.getCategoria();
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

    @Test
    public void testarNenhumaAtvEncontradaFiltroPorSem(){
        Collection<Atividade> nenhumaAtvEncontrada = AtividadeService.filterActivitiesByWeek(atvs, 30);
        Assert.assertTrue(nenhumaAtvEncontrada.size() == 0);
    }

    @Test
    public void testarMaisDeUmaAtvEncontradaFiltroPorSem(){
        Collection<Atividade> umaAtvEncontrada = AtividadeService.filterActivitiesByWeek(atvs, h0.getSemana());
        Assert.assertTrue(umaAtvEncontrada.size() == 4);
    }

    @Test
    public void testarOrdenarPorPrioridade(){
        expected.add(prioridadeAlta);
        expected.add(prioridadeMedia);
        expected.add(prioridadeBaixa);
        priorities.add(prioridadeBaixa);
        priorities.add(prioridadeAlta);
        priorities.add(prioridadeMedia);
        Collections.sort(priorities, new Comparator<Prioridade>() {
            @Override
            public int compare(Prioridade lhs, Prioridade rhs) {
                if (lhs.getPeso() < rhs.getPeso()) {
                    return -1;
                }
                if (lhs.getPeso() > rhs.getPeso()) {
                    return 1;
                }
                return 0;
            }
        });
        Assert.assertEquals(expected,priorities);

    }

    @Test
    public void testarCategoriaDeAtv(){
        Assert.assertEquals(categoriaAtv1.toString(),"Lazer");
        Assert.assertEquals(categoriaAtv2.toString(),"Trabalho");
        Assert.assertEquals(categoriaAtv3.toString(),"Não Categorizado");
//        Tag tagAtv4 = atv4.getTag();
//        Exception e = new IllegalArgumentException();
//        Assert.assertEquals(tagAtv4.toString(),e);
    }

}
