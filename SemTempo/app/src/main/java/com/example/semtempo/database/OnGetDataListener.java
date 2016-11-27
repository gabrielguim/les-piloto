package com.example.semtempo.database;

import com.example.semtempo.model.Atividade;

import java.util.List;

/**
 * Created by Rafael on 11/27/2016.
 */

public interface OnGetDataListener {
    public void onStart();
    public void onSuccess(List<Atividade> lista);

}
