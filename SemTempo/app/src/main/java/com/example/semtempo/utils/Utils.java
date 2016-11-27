package com.example.semtempo.utils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.example.semtempo.controllers.model.Atividade;

/**
 * Created by Gabriel on 24/11/2016.
 */

public class Utils {
    private static List<Atividade> lista = new ArrayList<>();

    public static void setListViewHeightBasedOnChildren(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();

        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;

        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();

    }

    public static void sortByHours(List<Atividade> atividades, int esquerda, int direita) {
        Collections.reverse(atividades);
    }

    public static void sortByPriority(List<Atividade> atividades, int esquerda, int direita) {
        Collections.reverse(atividades);
    }

    public static void addLista(Atividade a) {
        lista.add(a);
    }

    public static List<Atividade> getLista() {
        return lista;
    }
}
