package com.example.semtempo.model;

import android.net.Uri;

public class Foto {

    private String base64Imagem;

    public Foto(String base64Imagem){
        this.base64Imagem = base64Imagem;
    }

    public String getBase64Imagem() {
        return base64Imagem;
    }

    public void setBase64Imagem(String base64Imagem) {
        this.base64Imagem = base64Imagem;
    }
}
