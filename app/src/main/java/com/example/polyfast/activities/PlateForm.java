package com.example.polyfast.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;

import com.example.polyfast.R;
import com.example.polyfast.data_class.Temp_information_user;
import com.example.polyfast.fireBase_fonction.Controller;
import com.example.polyfast.forumManager.database.SQLiteUserManager;
import com.example.polyfast.forumManager.database.StudentHelper;
import com.example.polyfast.forumManager.database.TeacherHelper;
import com.example.polyfast.forumManager.models.Student;
import com.example.polyfast.forumManager.models.Token;
import com.example.polyfast.forumManager.models.User;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.iid.FirebaseInstanceId;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Objects;

public class PlateForm extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plate_form);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_cour, R.id.nav_exercice, R.id.nav_correction_exercice, R.id.nav_uplode_file,
                R.id.nav_forum, R.id.nav_a_propos, R.id.nav_nous_contacter, R.id.nav_compte)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        //manage tu nav par Mouanji Franck
        View header_drawer = navigationView.getHeaderView(0);
        TextView user_name;
        TextView user_address;
        user_name = header_drawer.findViewById(R.id.user_name);
        user_address = header_drawer.findViewById(R.id.user_address);
        if (Controller.verif_file(this)){
            Temp_information_user temp_information_user = Controller.Trans_to_Temp_information_user(this);
            user_address.setText(temp_information_user.getLogin());
            user_name.setText(temp_information_user.getUser_name());
        }else{
            user_name.setText(getResources().getString(R.string.app_name));
            user_address.setText(getResources().getString(R.string.app_mail));
        }
        // Navigate to the forum fragment where click on the notification summary.
        if (getIntent() != null && getIntent().hasExtra("fragment_id")){
            int fragmentId = getIntent().getIntExtra("fragment_id", R.id.nav_forum);
            navController.navigate(fragmentId);
        }

        // FIREBASE INSTANCE ID TOKEN.
        // Update the user token.
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Token token = new Token(Objects.requireNonNull(task.getResult()).getToken());
                Log.i("MainActivity", "Token : " + token.getToken());
                SQLiteUserManager db = new SQLiteUserManager(this);
                if (db.userInfoExist()) {
                    User user = db.getUserInfo();

                    if (user instanceof Student)
                        StudentHelper.updateToken(token.getToken(), user.getId());
                    else
                        TeacherHelper.updateToken(token.getToken(), user.getId());
                }
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.plate_form, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.app_bar_download){
            Intent intent = new Intent(PlateForm.this, DownloadedFile.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        System.exit(RESULT_OK);
    }
}