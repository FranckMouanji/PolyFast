package com.example.polyfast.data_class;

/**
 * Class to managed the user.
 *
 * @uathor Ronald Tchuekou.
 *
 */
public class Users {

    private String id;
    private String name;
    private String surname;
    private String statut;
    private String matiere_enseigner;
    private String niveau;
    private String classe;
    private String email;
    private String password;

    public Users() {
        this.id = "";
        this.name = "";
        this.surname = "";
        this.statut = "";
        this.matiere_enseigner = "";
        this.niveau = "";
        this.classe = "";
        this.email = "";
        this.password = "";
    }

    public Users(String m_name, String surname,String m_statut, String matiere, String m_niveau, String m_filiere, String email, String password) {
        this.id = "";
        this.name = m_name;
        this.surname = surname;
        this.statut = m_statut;
        this.matiere_enseigner = matiere;
        this.niveau = m_niveau;
        this.classe = m_filiere;
        this.email = email;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }



    public String getMatiere_enseigner() {
        return matiere_enseigner;
    }

    public void setMatiere_enseigner(String matiere_enseigner) {
        this.matiere_enseigner = matiere_enseigner;
    }


    public String getNiveau() {
        return niveau;
    }

    public void setNiveau(String niveau) {
        this.niveau = niveau;
    }

    public String getClasse() {
        return classe;
    }

    public void setClasse(String filiere) {
        this.classe = filiere;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }
}
