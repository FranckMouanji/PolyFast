package com.example.polyfast.data_class;

public class Temp_information_user {

    private  String login;
    private  String user_name;


    private  String statut;
    private  String etat;

    public Temp_information_user(){
        this.login = null;
        this.user_name = null;
        this.statut  = null;
        this.etat  = null;
    }

    public Temp_information_user(String m_login, String m_name, String m_statut, String m_etat){
        this.login = m_login;
        this.user_name = m_name;
        this.statut = m_statut;
        this.etat = m_etat;
    }



    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }


    public String getLogin() {return login;}

    public void setLogin(String login) {this.login = login;}

    public String getUser_name() { return user_name; }

    public void setUser_name(String password) {this.user_name = password;}
}
