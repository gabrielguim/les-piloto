package com.example.semtempo.model;

import android.net.Uri;

public class Foto {

    private Uri uriFoto;

    public Foto(Uri uriFoto){
        this.uriFoto = uriFoto;
    }

    public Uri getUriFoto() {
        return uriFoto;
    }

    public void setUriFoto(Uri newUriFoto) {
        this.uriFoto = newUriFoto;
    }
}
