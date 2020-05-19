package com.example.polyfast.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.polyfast.R;
import com.example.polyfast.data_class.Answer;
import com.example.polyfast.data_class.Users;
import com.example.polyfast.fireBase_fonction.ActionAboutAnswerOfQuestion;
import com.example.polyfast.fireBase_fonction.ActionAboutUsers;


public class Sing_in_sind_up extends AppCompatActivity {

    ScrollView layout_siging;
    EditText login_mail;
    EditText login_password;
    TextView password_forget;
    Button sigin_button;
    TextView create_account;

    ScrollView layout_sigiup;
    EditText name;
    EditText last_name;
    RadioButton prof;
    RadioButton etudiant;
    LinearLayout student_information;
    Spinner spinner1;
    Spinner classe_6eme;
    Spinner classe_5eme;
    Spinner classe_4eme;
    Spinner classe_3eme;
    Spinner classe_2nd;
    Spinner classe_1ere;
    Spinner classe_Tle;
    RadioButton normal;
    LinearLayout info_prof;
    Spinner matiere_enseigner;
    EditText mail_create;
    EditText password_create;
    EditText confirm_password_create;
    Button button_send_create_account;


    ScrollView layout_reset;
    EditText reset_mail;
    EditText answer_first_question1;
    EditText answer_first_question2;
    EditText answer_first_question3;
    Button reset_button;

    TextView empty_reset_mail;
    TextView error_reset_mail;
    TextView empty_answer_first_question1;
    TextView empty_answer_first_question2;
    TextView empty_answer_first_question3;
    TextView all_question_empty;


    ScrollView layout_question;
    EditText answer_first_question1_enter;
    EditText answer_first_question2_enter;
    EditText answer_first_question3_enter;
    Button survey_button;

    TextView empty_login_mail;
    TextView error_login_mail;
    TextView empty_login_password;
    TextView empty_create_name;
    TextView empty_create_last_name;
    TextView empty_create_mail;
    TextView error_create_mail;
    TextView empty_create_password;
    TextView error_caracter_of_password;
    TextView empty_confirm_create_password;
    TextView error_create_password;
    TextView erreur_questionnaire;

    Users users;
    Button button_back_create_account;
    Button button_back_singin;
    Button button_back_create;
    TextView error_mail_or_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_in_sind_up);

        //initialisation
        init();

        //sigin
        sigin_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controle_and_siging();
            }
        });

        //going to singup
        create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCreateAccountForm();
            }
        });

        //going to reset layout
        password_forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAnswerForm();
            }
        });


        button_send_create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controleAndSingUp();
            }
        });


        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = String.valueOf(parent.getSelectedItem());
                if (item.equals("6eme")){
                    classe_6eme.setVisibility(View.VISIBLE);
                }else {
                    classe_6eme.setVisibility(View.GONE);
                }
                if (item.equals("5eme")){
                    classe_5eme.setVisibility(View.VISIBLE);
                }else {
                    classe_5eme.setVisibility(View.GONE);
                }
                if (item.equals("4eme")){
                    classe_4eme.setVisibility(View.VISIBLE);
                }else {
                    classe_4eme.setVisibility(View.GONE);
                }
                if (item.equals("3eme")){
                    classe_3eme.setVisibility(View.VISIBLE);
                }else {
                    classe_3eme.setVisibility(View.GONE);
                }
                if (item.equals("2nd")){
                    classe_2nd.setVisibility(View.VISIBLE);
                }else {
                    classe_2nd.setVisibility(View.GONE);
                }
                if (item.equals("1ere")){
                    classe_1ere.setVisibility(View.VISIBLE);
                }else {
                    classe_1ere.setVisibility(View.GONE);
                }
                if (item.equals("Tle")){
                    classe_Tle.setVisibility(View.VISIBLE);
                }else {
                    classe_Tle.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        button_back_create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLoginForm();
            }
        });

        button_back_singin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromAnswerFormtologin();
            }
        });

        button_back_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromQuestiontocreate();
            }
        });


        reset_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset_password();
            }
        });


        survey_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUsers();
            }
        });

    }




    private void init(){
        layout_siging = findViewById(R.id.layout_siging);
        login_mail = findViewById(R.id.login_mail);
        login_password = findViewById(R.id.login_password);
        password_forget = findViewById(R.id.password_forget);
        sigin_button = findViewById(R.id.sigin_button);
        create_account = findViewById(R.id.create_account);


        layout_sigiup = findViewById(R.id.layout_sigiup);
        name = findViewById(R.id.name);
        last_name = findViewById(R.id.last_name);
        prof = findViewById(R.id.prof);
        etudiant = findViewById(R.id.etudiant);
        student_information = findViewById(R.id.student_information);
        spinner1 = findViewById(R.id.spinner1);
        classe_6eme = findViewById(R.id.classe_6eme);
        classe_5eme = findViewById(R.id.classe_5eme);
        classe_4eme = findViewById(R.id.classe_4eme);
        classe_3eme = findViewById(R.id.classe_3eme);
        classe_2nd = findViewById(R.id.classe_2nd);
        classe_1ere = findViewById(R.id.classe_1ere);
        classe_Tle = findViewById(R.id.classe_Tle);
        normal = findViewById(R.id.normal);
        info_prof = findViewById(R.id.prof_information);
        matiere_enseigner = findViewById(R.id.matiere_enseigner);
        mail_create = findViewById(R.id.mail_create);
        password_create = findViewById(R.id.password_create);
        confirm_password_create = findViewById(R.id.confirm_password_create);
        button_send_create_account = findViewById(R.id.button_send_create_account);



        layout_reset = findViewById(R.id.layout_reset);
        reset_mail = findViewById(R.id.reset_mail);
        answer_first_question1 = findViewById(R.id.answer_first_question1);
        answer_first_question2 = findViewById(R.id.answer_first_question2);
        answer_first_question3 = findViewById(R.id.answer_first_question3);
        reset_button = findViewById(R.id.reset_button);

        empty_reset_mail = findViewById(R.id.empty_reset_mail);
        error_reset_mail = findViewById(R.id.error_reset_mail);
        empty_answer_first_question1 = findViewById(R.id.empty_answer_first_question1);
        empty_answer_first_question2 = findViewById(R.id.empty_answer_first_question2);
        empty_answer_first_question3 = findViewById(R.id.empty_answer_first_question3);
        all_question_empty = findViewById(R.id.all_question_empty);


        layout_question = findViewById(R.id.layout_question);
        answer_first_question1_enter = findViewById(R.id.answer_first_question1_enter);
        answer_first_question2_enter = findViewById(R.id.answer_first_question2_entrer);
        answer_first_question3_enter = findViewById(R.id.answer_first_question3_entrer);
        survey_button = findViewById(R.id.survey_button);


        empty_login_mail = findViewById(R.id.empty_login_mail);
        error_login_mail = findViewById(R.id.error_login_mail);
        empty_login_password = findViewById(R.id.empty_login_password);
        empty_create_name = findViewById(R.id.empty_create_name);
        empty_create_last_name = findViewById(R.id.empty_create_last_name);
        empty_create_mail = findViewById(R.id.empty_create_mail);
        error_create_mail = findViewById(R.id.error_create_mail);
        empty_create_password = findViewById(R.id.empty_create_password);
        error_caracter_of_password = findViewById(R.id.error_caracter_of_password);
        empty_confirm_create_password = findViewById(R.id.empty_confirm_create_password);
        error_create_password = findViewById(R.id.error_create_password);
        erreur_questionnaire = findViewById(R.id.erreur_questionnaire);


        button_back_create_account = findViewById(R.id.button_back_create_account);
        button_back_singin = findViewById(R.id.button_back_singin);
        button_back_create = findViewById(R.id.button_back_create);

        error_mail_or_password = findViewById(R.id.error_mail_or_password);
    }



    private  void setUsers(){
        String answer1 = String.valueOf(answer_first_question1_enter.getText());
        String answer2 = String.valueOf(answer_first_question2_enter.getText());
        String answer3 = String.valueOf(answer_first_question3_enter.getText());
        if (answer1.equals("") || answer2.equals("") || answer3.equals("")){
            erreur_questionnaire.setVisibility(View.VISIBLE);
        }else {
            if (erreur_questionnaire.getVisibility() == View.VISIBLE){
                erreur_questionnaire.setVisibility(View.GONE);
            }
        }

        if (erreur_questionnaire.getVisibility() == View.GONE){
            Answer answer4 = new Answer("question1", users.getEmail(), answer1);
            Answer answer5 = new Answer("question2", users.getEmail(), answer2);
            Answer answer6 = new Answer("question3", users.getEmail(), answer3);

            ActionAboutUsers.setUsersInFirestore(this, users, answer4, answer5, answer6);
        }
    }



    private void reset_password(){
        String mail = String.valueOf(reset_mail.getText());
        String answer1 = String.valueOf(answer_first_question1.getText());
        String answer2 = String.valueOf(answer_first_question2.getText());
        String answer3 = String.valueOf(answer_first_question3.getText());


        if (mail.equals("")){
            empty_reset_mail.setVisibility(View.VISIBLE);
        }else {
            if (empty_reset_mail.getVisibility() == View.VISIBLE){
                empty_reset_mail.setVisibility(View.GONE);
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(mail).matches()){
                error_reset_mail.setVisibility(View.VISIBLE);
            }else {
                if (error_reset_mail.getVisibility() == View.VISIBLE){
                    error_reset_mail.setVisibility(View.GONE);
                }
            }

        }

        if (answer1.equals("") || answer2.equals("") || answer3.equals("")){
            all_question_empty.setVisibility(View.VISIBLE);
        }else {
            if (all_question_empty.getVisibility() == View.VISIBLE){
                all_question_empty.setVisibility(View.GONE);
            }

            Answer reponse1 = new Answer("question1", mail, answer1);
            Answer reponse2 = new Answer("question2", mail, answer2);
            Answer reponse3 = new Answer("question3", mail, answer3);

            ActionAboutAnswerOfQuestion.verifAnswer(reponse1, reponse2, reponse3, this, empty_answer_first_question1, empty_answer_first_question2, empty_answer_first_question3);
        }



    }





    private void controle_and_siging(){
        String mail = String.valueOf(login_mail.getText());
        String password = String.valueOf(login_password.getText());
        if (mail.equals("")){
            empty_login_mail.setVisibility(View.VISIBLE);
        }else {
            if (empty_login_mail.getVisibility() == View.VISIBLE){
                empty_login_mail.setVisibility(View.GONE);
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(mail).matches()){
                error_login_mail.setVisibility(View.VISIBLE);
            }else {
                if (error_login_mail.getVisibility() == View.VISIBLE){
                    error_login_mail.setVisibility(View.GONE);
                }
            }

        }

        if (password.equals("")){
            empty_login_password.setVisibility(View.VISIBLE);
        }else {
            if (empty_login_password.getVisibility() == View.VISIBLE){
                empty_login_password.setVisibility(View.GONE);
            }
        }

        if (empty_login_mail.getVisibility() == View.GONE && empty_login_password.getVisibility() == View.GONE){
            ActionAboutUsers.singUsers(this, mail, password, error_mail_or_password);
        }

    }



    private void controleAndSingUp(){
        String nom = String.valueOf(name.getText());
        String prenom = String.valueOf(last_name.getText());
        String niveau = String.valueOf(spinner1.getSelectedItem());
        String classe = null;
        if (niveau.equals("6eme")){
            classe = classe_6eme.getSelectedItem().toString();
        }
        if (niveau.equals("5eme")){
            classe = classe_5eme.getSelectedItem().toString();
        }
        if (niveau.equals("4eme")){
            classe = classe_4eme.getSelectedItem().toString();
        }
        if (niveau.equals("3eme")){
            classe = classe_3eme.getSelectedItem().toString();
        }
        if (niveau.equals("2nd")){
            classe = classe_2nd.getSelectedItem().toString();
        }
        if (niveau.equals("1ere")){
            classe = classe_1ere.getSelectedItem().toString();
        }
        if (niveau.equals("Tle")){
            classe = classe_Tle.getSelectedItem().toString();
        }


        String statut;
        String matiere = null;
        String mail = String.valueOf(mail_create.getText());
        String password = String.valueOf(password_create.getText());
        String confirm_password = String.valueOf(confirm_password_create.getText());

        if (nom.equals("")){
            empty_create_name.setVisibility(View.VISIBLE);
        }else {
            if (empty_create_name.getVisibility() == View.VISIBLE){
                empty_create_name.setVisibility(View.GONE);
            }
        }

        if (prenom.equals("")){
            empty_create_last_name.setVisibility(View.VISIBLE);
        }else {
            if (empty_create_last_name.getVisibility() == View.VISIBLE){
                empty_create_last_name.setVisibility(View.GONE);
            }
        }

        if (prof.isChecked()){
            statut = "professeur";
            niveau = null;
            classe = null;
            matiere = matiere_enseigner.getSelectedItem().toString();
        }else {
            statut = "etudiant";
            matiere = null;
        }


        if (mail.equals("")){
            empty_create_mail.setVisibility(View.VISIBLE);
        }else {
            if (empty_create_mail.getVisibility() == View.VISIBLE){
                empty_create_mail.setVisibility(View.GONE);
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(mail).matches()){
                error_create_mail.setVisibility(View.VISIBLE);
            }else {
                if (error_create_mail.getVisibility() == View.VISIBLE){
                    error_create_mail.setVisibility(View.GONE);
                }
            }

        }


        if (password.equals("")){
            empty_create_password.setVisibility(View.VISIBLE);
        }else {
            if (empty_create_password.getVisibility() == View.VISIBLE){
                empty_create_password.setVisibility(View.GONE);
            }

            if (password.length() <= 6){
                error_caracter_of_password.setVisibility(View.VISIBLE);
            }else{
                if (error_caracter_of_password.getVisibility() == View.VISIBLE){
                    error_caracter_of_password.setVisibility(View.GONE);
                }
            }
        }

        if (confirm_password.equals("")){
            empty_confirm_create_password.setVisibility(View.VISIBLE);
        }else{
            if (empty_confirm_create_password.getVisibility() == View.VISIBLE){
                empty_confirm_create_password.setVisibility(View.GONE);
            }

            if (!(confirm_password.equals(password))){
                error_create_password.setVisibility(View.VISIBLE);
            }else{
                if (error_create_password.getVisibility() == View.VISIBLE){
                    error_create_password.setVisibility(View.GONE);
                }
            }


            if (empty_create_name.getVisibility() == View.GONE && empty_create_last_name.getVisibility() == View.GONE && empty_create_mail.getVisibility() == View.GONE && error_create_mail.getVisibility() == View.GONE && empty_create_password.getVisibility() == View.GONE && error_caracter_of_password.getVisibility() == View.GONE && empty_confirm_create_password.getVisibility() == View.GONE && error_create_password.getVisibility() == View.GONE){
                setQuestionForm();
                String nom_classe = niveau+classe;
                users = new Users(nom, prenom, statut, matiere, niveau, nom_classe, mail, password);
            }
        }


    }




    private void setCreateAccountForm() {
        float donne1 = 0;
        float donne2 = 1;
        AlphaAnimation openAnimation = new AlphaAnimation(donne2, donne1);
        openAnimation.setDuration(1000);
        layout_siging.setAnimation(openAnimation);
        layout_siging.setVisibility(View.GONE);

        AlphaAnimation closeAnimation = new AlphaAnimation(donne1, donne2);
        closeAnimation.setDuration(1000);
        closeAnimation.setStartOffset(1000);
        layout_sigiup.setAnimation(closeAnimation);
        layout_sigiup.setVisibility(View.VISIBLE);
    }

    private void setLoginForm() {
        float donne1 = 0;
        float donne2 = 1;
        AlphaAnimation openAnimation = new AlphaAnimation(donne2, donne1);
        openAnimation.setDuration(1000);
        layout_sigiup.setAnimation(openAnimation);
        layout_sigiup.setVisibility(View.GONE);

        AlphaAnimation closeAnimation = new AlphaAnimation(donne1, donne2);
        closeAnimation.setDuration(1000);
        closeAnimation.setStartOffset(1000);
        layout_siging.setAnimation(closeAnimation);
        layout_siging.setVisibility(View.VISIBLE);
    }


    private void setQuestionForm() {
        float donne1 = 0;
        float donne2 = 1;
        AlphaAnimation openAnimation = new AlphaAnimation(donne2, donne1);
        openAnimation.setDuration(1000);
        layout_sigiup.setAnimation(openAnimation);
        layout_sigiup.setVisibility(View.GONE);

        AlphaAnimation closeAnimation = new AlphaAnimation(donne1, donne2);
        closeAnimation.setDuration(1000);
        closeAnimation.setStartOffset(1000);
        layout_question.setAnimation(closeAnimation);
        layout_question.setVisibility(View.VISIBLE);
    }


    private void fromQuestiontocreate() {
        float donne1 = 0;
        float donne2 = 1;
        AlphaAnimation openAnimation = new AlphaAnimation(donne2, donne1);
        openAnimation.setDuration(1000);
        layout_question.setAnimation(openAnimation);
        layout_question.setVisibility(View.GONE);

        AlphaAnimation closeAnimation = new AlphaAnimation(donne1, donne2);
        closeAnimation.setDuration(1000);
        closeAnimation.setStartOffset(1000);
        layout_sigiup.setAnimation(closeAnimation);
        layout_sigiup.setVisibility(View.VISIBLE);
    }



    private void setAnswerForm() {
        float donne1 = 0;
        float donne2 = 1;
        AlphaAnimation openAnimation = new AlphaAnimation(donne2, donne1);
        openAnimation.setDuration(1000);
        layout_siging.setAnimation(openAnimation);
        layout_siging.setVisibility(View.GONE);

        AlphaAnimation closeAnimation = new AlphaAnimation(donne1, donne2);
        closeAnimation.setDuration(1000);
        closeAnimation.setStartOffset(1000);
        layout_reset.setAnimation(closeAnimation);
        layout_reset.setVisibility(View.VISIBLE);
    }


    private void fromAnswerFormtologin() {
        float donne1 = 0;
        float donne2 = 1;
        AlphaAnimation openAnimation = new AlphaAnimation(donne2, donne1);
        openAnimation.setDuration(1000);
        layout_reset.setAnimation(openAnimation);
        layout_reset.setVisibility(View.GONE);

        AlphaAnimation closeAnimation = new AlphaAnimation(donne1, donne2);
        closeAnimation.setDuration(1000);
        closeAnimation.setStartOffset(1000);
        layout_siging.setAnimation(closeAnimation);
        layout_siging.setVisibility(View.VISIBLE);
    }



    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()) {
            case R.id.prof:
                if (checked)
                    student_information.setVisibility(View.GONE);
                    info_prof.setVisibility(View.VISIBLE);
                    break;
            case R.id.etudiant:
                if (checked)
                    student_information.setVisibility(View.VISIBLE);
                    info_prof.setVisibility(View.GONE);
                    break;
        }
    }
}
