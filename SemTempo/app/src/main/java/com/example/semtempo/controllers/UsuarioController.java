package com.example.semtempo.controllers;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

/**
 * Created by Mafra on 25/11/2016.
 */
public class UsuarioController {

    private GoogleSignInAccount userInfo;
    private static UsuarioController usrController;

    public static UsuarioController getInstance(){
        if (usrController == null){
            usrController = new UsuarioController();
        }
        return usrController;
    }

    public GoogleSignInAccount getCurrentUser(){
        return userInfo;
    }

    public void setCurrentUser(GoogleSignInAccount userInfo){
        this.userInfo = userInfo;
    }
}
