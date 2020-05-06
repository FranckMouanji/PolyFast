package com.example.polyfast.Activities.ui.upload_file;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModelProviders;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.polyfast.R;
import com.example.polyfast.data_class.File_uploaded;
import com.example.polyfast.data_class.Temp_information_user;
import com.example.polyfast.fireBase_fonction.ActionAboutFile;
import com.example.polyfast.fireBase_fonction.ActionAboutUsers;
import com.example.polyfast.fireBase_fonction.Controller;

import static android.app.Activity.RESULT_OK;

public class upload_fileFragment extends Fragment {

    private  Uri file;
    private TextView file_name_teacher;
    private TextView file_name_normal_student;



    public static upload_fileFragment newInstance() {
        return new upload_fileFragment();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.upload_file_fragment, container, false);


        //initialisation


        //layout teacher
        ScrollView layout_of_teacher = view.findViewById(R.id.layout_of_teacher);
        EditText nom_fichier_teacher = view.findViewById(R.id.nom_fichier_teacher);
        Spinner classe_6eme = view.findViewById(R.id.classe_6eme);
        Spinner classe_5eme = view.findViewById(R.id.classe_5eme);
        Spinner classe_4eme = view.findViewById(R.id.classe_4eme);
        Spinner classe_3eme = view.findViewById(R.id.classe_3eme);
        Spinner classe_2nd = view.findViewById(R.id.classe_2nd);
        Spinner classe_1ere = view.findViewById(R.id.classe_1ere);
        Spinner classe_Tle = view.findViewById(R.id.classe_Tle);
        Spinner choix_niveau = view.findViewById(R.id.choix_niveau);
        Spinner choix_categorie = view.findViewById(R.id.choix_categorie);
        Button choose_pdf_teacher = view.findViewById(R.id.choose_pdf_teacher);
        TextView empty_nom_document_teacher = view.findViewById(R.id.empty_nom_document_teacher);
        Button send_teacher = view.findViewById(R.id.send_teacher);

        file_name_teacher = view.findViewById(R.id.file_name_teacher);


        //layout normal Student
        ScrollView layout_of_normal_student = view.findViewById(R.id.layout_of_normal_student);
        Spinner nom_matiere_normal_student = view.findViewById(R.id.nom_matiere_normal_student);
        EditText nom_fichier_normal_student = view.findViewById(R.id.nom_fichier_normal_student);
        Spinner choix_categorie_normal_student = view.findViewById(R.id.choix_categorie_normal_student);
        Button choose_pdf_normal_student = view.findViewById(R.id.choose_pdf_normal_student);

        TextView empty_nom_document_normal_student = view.findViewById(R.id.empty_nom_document_student);
        Button send_normal_student = view.findViewById(R.id.send_normal_student);

        file_name_normal_student = view.findViewById(R.id.file_name_normal_student);

        //test de vue
        Temp_information_user temp_information_user = Controller.Trans_to_Temp_information_user(getContext());
        String nom_user = temp_information_user.getUser_name();
        String login_user = temp_information_user.getLogin();
        Controller.comparedData(getContext(), layout_of_teacher, layout_of_normal_student);



        //fonction de travail sur les vues

        //teacher
        choose_pdf_teacher.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("application/pdf");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, getResources().getString(R.string.selection_fichier)), 1);
            }
        });

        choix_niveau.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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




        send_teacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nom_document = String.valueOf(nom_fichier_teacher.getText());
                String classe = null;
                String niveau = String.valueOf(choix_niveau.getSelectedItem());
                String categorie = String.valueOf(choix_categorie.getSelectedItem());
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

                if (nom_document.equals("")){
                    empty_nom_document_teacher.setVisibility(View.VISIBLE);
                }else {
                    if (empty_nom_document_teacher.getVisibility() == View.VISIBLE){
                        empty_nom_document_teacher.setVisibility(View.VISIBLE);
                    }
                }

                if (empty_nom_document_teacher.getVisibility() == View.GONE && file!=null){
                    String nom_classe = niveau + classe;

                    File_uploaded file_uploaded = new File_uploaded(nom_document, null, login_user, nom_user, categorie, nom_classe, null);
                    ActionAboutUsers.completeInformationTeacher(getContext(), file_uploaded, file);
                }
            }
        });

        //normal student
        choose_pdf_normal_student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("application/pdf");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, getResources().getString(R.string.selection_fichier)), 1);
            }
        });


        send_normal_student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code_matiere = String.valueOf(nom_matiere_normal_student.getSelectedItem());
                String nom_document = String.valueOf(nom_fichier_normal_student.getText());
                String categorie = String.valueOf(choix_categorie_normal_student.getSelectedItem());


                if (nom_document.equals("")){
                    empty_nom_document_normal_student.setVisibility(View.VISIBLE);
                }else {
                    if (empty_nom_document_normal_student.getVisibility() == View.VISIBLE){
                        empty_nom_document_normal_student.setVisibility(View.VISIBLE);
                    }
                }
                if (empty_nom_document_normal_student.getVisibility() == View.GONE && file != null){
                    File_uploaded file_uploaded = new File_uploaded(nom_document, code_matiere, login_user, nom_user, categorie, null, null);
                    ActionAboutUsers.completeInformationUser(getContext(), file_uploaded, file);
                }
            }
        });





        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1  && resultCode == RESULT_OK &&

            data != null && data.getData() != null){
            file = data.getData();
            file_name_teacher.setText(file.toString());
            file_name_normal_student.setText(file.toString());
        }
    }





    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // TODO: Use the ViewModel
    }

}
