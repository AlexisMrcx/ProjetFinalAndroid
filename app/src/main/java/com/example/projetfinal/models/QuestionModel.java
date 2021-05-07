package com.example.projetfinal.models;

import android.graphics.Bitmap;

public class QuestionModel {
    String intitule;
    String reponse;
    Bitmap image;

    public QuestionModel(){
    }

    public QuestionModel(String i){
        intitule=i;
    }



    public String getReponse() {
        return reponse;
    }

    public void setReponse(String reponse) {
        this.reponse = reponse;
    }

    public String getIntitule() {
        return intitule;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
