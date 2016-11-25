package model;

/**
 * Created by Lucas on 20/11/2016.
 */
public class Foto {

    private String IDFoto;

    public Foto(String IDFoto){
        this.IDFoto = IDFoto;
    }

    public String getIDFoto() {
        return IDFoto;
    }

    public void alterarIDFoto(String IDFoto) {
        this.IDFoto = IDFoto;
    }
}
