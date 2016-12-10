package com.example.semtempo.utils;

import android.graphics.Bitmap;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import com.example.semtempo.model.Atividade;

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

    public static void sortByHours(List<Atividade> atividades) {
        Collections.sort(atividades, new Comparator<Atividade>() {
            @Override
            public int compare(Atividade atv1, Atividade atv2) {
                int result = 0;
                if (atv1.getHorario().getTotalHorasInvestidas()  < atv2.getHorario().getTotalHorasInvestidas())
                    result =  -1;
                else if (atv1.getHorario().getTotalHorasInvestidas()  > atv2.getHorario().getTotalHorasInvestidas())
                    result = 1;

                return result;
            }
        });
    }

    public static void sortByPriority(List<Atividade> atividades) {
        Collections.sort(atividades, new Comparator<Atividade>() {
            @Override
            public int compare(Atividade atv1, Atividade atv2) {
                return atv1.getPrioridade().compareTo(atv2.getPrioridade());
            }
        });
    }

    public static void sortByDate(List<Atividade> atividades) {
        Collections.sort(atividades, new Comparator<Atividade>() {
            @Override
            public int compare(Atividade atv1, Atividade atv2) {
                return convertDateToCalendar(atv2.getHorario().getData()).getTime().compareTo(convertDateToCalendar(atv1.getHorario().getData()).getTime());
            }
        });
    }

    public static Calendar convertDateToCalendar(String data) {
        Calendar calendar = new GregorianCalendar();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
        try {
            calendar.setTime(sdf.parse(data));
        } catch (ParseException e) {
            System.out.println("Erro ao converter a data");
        }

        return calendar;
    }

    public String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        byte[] byteFormat = stream.toByteArray();
        String imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);

        return imgString;
    }

    public static void addLista(Atividade a) {
        lista.add(a);
    }

    public static List<Atividade> getLista() {
        return lista;
    }

}
