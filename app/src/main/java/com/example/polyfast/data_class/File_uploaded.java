package com.example.polyfast.data_class;

public class File_uploaded {

    private String fileName;
    private String studyName;
    private String senderMail;
    private String senderName;
    private String categorie;
    private String classe;
    private String filePathInFirebase;

    public File_uploaded(){
        this.fileName = "";
        this.studyName = "";
        this.senderMail = "";
        this.senderName = "";
        this.categorie = "";
        this.classe = "";
        this.filePathInFirebase = "";

    }

    public File_uploaded(String m_fileName, String m_studyName, String m_senderMail, String m_senderName, String m_categorie, String m_filiere, String m_filePath){
        this.fileName = m_fileName;
        this.studyName = m_studyName;
        this.senderMail = m_senderMail;
        this.senderName = m_senderName;
        this.categorie = m_categorie;
        this.classe = m_filiere;
        this.filePathInFirebase = m_filePath;
    }


    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getStudyName() {
        return studyName;
    }

    public void setStudyName(String studyName) {
        this.studyName = studyName;
    }

    public String getSenderMail() {
        return senderMail;
    }

    public void setSenderMail(String senderMail) {
        this.senderMail = senderMail;
    }



    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }


    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getFiliere() {
        return classe;
    }

    public void setFiliere(String filiere) {
        this.classe = filiere;
    }

    public String getFilePathInFirebase() {
        return filePathInFirebase;
    }

    public void setFilePathInFirebase(String filePathInFirebase) {
        this.filePathInFirebase = filePathInFirebase;
    }


}
