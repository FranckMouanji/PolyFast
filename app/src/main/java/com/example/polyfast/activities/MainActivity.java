package com.example.polyfast.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.polyfast.R;
import com.example.polyfast.fireBase_fonction.Controller;

public class MainActivity extends AppCompatActivity {


    ImageView logo_debut;
    TextView text_start;
    RelativeLayout layout_start;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         // initialisation
        init();

        //appel des fonctions d'animation
        float from_alpha = 0;
        float to_alpha = 1;
        AlphaAnimation animation = new AlphaAnimation(from_alpha, to_alpha);
        animation.setDuration(2000);
        animation.setStartOffset(2000);
        text_start.setAnimation(animation);
        text_start.setVisibility(View.VISIBLE);


        TranslateAnimation animation1 = new TranslateAnimation(0,0,0,-600);
        animation1.setDuration(4000);
        animation1.setFillAfter(true);
        logo_debut.setAnimation(animation1);

        text_start.setOnClickListener(v -> {
            if (Controller.verif_file(MainActivity.this)){
                Intent intent = new Intent(getApplicationContext(), PlateForm.class);
                startActivity(intent);
                finish();
            }else{
                Intent intent = new Intent(getApplicationContext(), Sing_in_sind_up.class);
                startActivity(intent);
                finish();
            }


        });


    }

    private void init(){
        logo_debut = findViewById(R.id.logo_debut);
        text_start = findViewById(R.id.text_start);
        layout_start = findViewById(R.id.layout_start);
    }
}
