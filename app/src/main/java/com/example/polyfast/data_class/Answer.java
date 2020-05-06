package com.example.polyfast.data_class;

public class Answer {

    private String id_questionnaire;
    private String id_user;
    private String response;

    public Answer() {
    }

    public Answer(String id_questionnaire, String id_user, String response) {
        this.id_questionnaire = id_questionnaire;
        this.id_user = id_user;
        this.response = response;
    }

    public String getId_questionnaire() {
        return id_questionnaire;
    }

    public void setId_questionnaire(String id_questionnaire) {
        this.id_questionnaire = id_questionnaire;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "id_questionnaire='" + id_questionnaire + '\'' +
                ", id_user='" + id_user + '\'' +
                ", response='" + response + '\'' +
                '}';
    }
}
