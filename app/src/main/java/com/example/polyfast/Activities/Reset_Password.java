package com.example.polyfast.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.polyfast.R;
import com.example.polyfast.fireBase_fonction.ActionAboutUsers;

public class Reset_Password extends AppCompatActivity {

    ScrollView layout_reset;
    EditText password_change;
    EditText confirm_password_change;
    TextView empty_change_password;
    TextView error_caracter_of_password_change;
    TextView empty_confirm_password_change;
    TextView error_match_password;
    Button button_send_paasword_change;
    Button button_back_of_reset_layout;

    String mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset__password);
        //recuperation de l'info passer en intent
        final Intent intent = getIntent();
        if (intent != null){
            if (intent.hasExtra("mail")){
                mail = intent.getStringExtra("mail");
            }
        }


        //initialisation
        init();

        button_send_paasword_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verif_and_change_password();
            }
        });

        button_back_of_reset_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(), Sing_in_sind_up.class);
                startActivity(intent);
                finish();
            }
        });
    }


    private void init(){
        layout_reset = findViewById(R.id.layout_reset);
        password_change = findViewById(R.id.password_change);
        confirm_password_change = findViewById(R.id.confirm_password_change);
        empty_change_password = findViewById(R.id.empty_change_password);
        error_caracter_of_password_change = findViewById(R.id.error_caracter_of_password_change);
        empty_confirm_password_change = findViewById(R.id.empty_confirm_password_change);
        error_match_password = findViewById(R.id.error_match_password);
        button_send_paasword_change = findViewById(R.id.button_send_paasword_change);
        button_back_of_reset_layout = findViewById(R.id.button_back_of_reset_layout);
    }

    private void verif_and_change_password(){
        String password = String.valueOf(password_change.getText());
        String confirm_password = String.valueOf(confirm_password_change.getText());

        if (password.equals("")){
            empty_change_password.setVisibility(View.VISIBLE);
        }else{
            if (empty_change_password.getVisibility() == View.VISIBLE){
                empty_change_password.setVisibility(View.GONE);
            }
            if (password.length()<=6){
                error_caracter_of_password_change.setVisibility(View.VISIBLE);
            }else{
                if (error_caracter_of_password_change.getVisibility() == View.VISIBLE){
                    error_caracter_of_password_change.setVisibility(View.GONE);
                }
            }
        }

        if (confirm_password.equals("")){
            empty_confirm_password_change.setVisibility(View.VISIBLE);
        }else{
            if (empty_confirm_password_change.getVisibility() == View.VISIBLE){
                empty_confirm_password_change.setVisibility(View.GONE);
            }

            if (!(confirm_password.equals(password))){
                error_match_password.setVisibility(View.VISIBLE);
            }else{
                if (error_match_password.getVisibility() == View.VISIBLE){
                    error_match_password.setVisibility(View.GONE);
                }

                ActionAboutUsers.updateUser(mail, getApplicationContext(), password);

            }
        }
    }
}
