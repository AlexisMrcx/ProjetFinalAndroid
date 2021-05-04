package com.example.projetfinal.models;

public class QuestionModel {
    String intitule;
    String reponse;


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
}
