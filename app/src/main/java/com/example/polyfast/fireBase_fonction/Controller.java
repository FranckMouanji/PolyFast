package com.example.polyfast.fireBase_fonction;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.example.polyfast.Activities.Sing_in_sind_up;
import com.example.polyfast.R;
import com.example.polyfast.data_class.File_uploaded;
import com.example.polyfast.data_class.Temp_information_user;
import com.example.polyfast.data_class.Users;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Controller {

    private static final String FILE_NAME = "user_Information";


    public static void delete_file(Context context){
        context.deleteFile(FILE_NAME);
        Intent intent  = new Intent(context, Sing_in_sind_up.class);
        context.startActivity(intent);
        ((Activity) context).finish();
    }


    private static boolean verif_file_exist(Context context) {
        File file = new File(context.getFilesDir(), FILE_NAME);
        return file.isFile();
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static boolean verif_file(Context context) {
        if (verif_file_exist(context)) {
            String content_of_file = Controller.take_information_of_file_users(context);
            return content_of_file != null && !content_of_file.equals(" ");
        } else {
            return false;
        }
    }


    static void create_file(Users users, Context context) {
        if (users != null) {
            String user_mail = users.getEmail();
            String user_name = users.getName() + " " + users.getSurname();
            String  statut = users.getStatut();
            String classe = users.getClasse();
            String contenu_ecrit = user_mail + "  " + user_name + "  " + statut + "  " + classe;
            FileOutputStream fileOutputStream = null;

            try {
                fileOutputStream = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
                fileOutputStream.write(contenu_ecrit.getBytes());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            String contenu_ecrit = " ";
            FileOutputStream fileOutputStream = null;

            try {
                fileOutputStream = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
                fileOutputStream.write(contenu_ecrit.getBytes());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String take_information_of_file_users(Context context) {
        String information_user_take;

        FileInputStream fis = null;
        try {
            fis = context.openFileInput(FILE_NAME);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assert fis != null;
        InputStreamReader inputStreamReader =
                new InputStreamReader(fis);
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
            String line = reader.readLine();
            while (line != null) {
                stringBuilder.append(line).append('\n');
                line = reader.readLine();
            }

            reader.close();
            inputStreamReader.close();
        } catch (IOException e) {
            // Error occurred when opening raw file for reading.
        } finally {
            information_user_take = stringBuilder.toString();
        }
        return information_user_take;
    }



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static Temp_information_user Trans_to_Temp_information_user(Context context){
        Temp_information_user temp_information_user;
        String information_user = Controller.take_information_of_file_users(context);
        if(information_user != null){
            String [] partition = information_user.split(" {2}");
            temp_information_user = new Temp_information_user(partition[0], partition[1], partition[2], partition[3]);
        }else {
            temp_information_user = new Temp_information_user();
        }
        return temp_information_user;
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void comparedData(Context context, ScrollView scrollViewprof, ScrollView scrollViewnormal){
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle(context.getResources().getString(R.string.wait));
        progressDialog.show();
        Temp_information_user temp_information_user = Trans_to_Temp_information_user(context);
        ActionAboutUsers.getParticularUser(temp_information_user.getLogin()).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                Users users = null;
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    users = documentSnapshot.toObject(Users.class);
                }
                assert users != null;
                if (users.getStatut().equals("professeur")){
                    scrollViewprof.setVisibility(View.VISIBLE);
                }else {
                        scrollViewnormal.setVisibility(View.VISIBLE);

                }
                progressDialog.dismiss();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(context, context.getResources().getString(R.string.verif_connexion), Toast.LENGTH_SHORT).show();
                    }
                });


    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void recup_and_charge_data(Context context, ListView listView, String page, TextView textView, List<File_uploaded> file_uploadeds){
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle(context.getResources().getString(R.string.wait));
        progressDialog.show();
        Temp_information_user temp_information_user = Trans_to_Temp_information_user(context);
        ActionAboutUsers.getParticularUser(temp_information_user.getLogin()).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                Users users=null;
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    users = documentSnapshot.toObject(Users.class);
                }

                if (users.getStatut().equals("professeur")){
                    if (page.equals("home")){
                        ActionAboutFile.getAllFile().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                File_uploaded file_uploaded = null;
                                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                    file_uploaded = documentSnapshot.toObject(File_uploaded.class);
                                    file_uploadeds.add(file_uploaded);
                                }

                                if (file_uploaded == null){
                                    progressDialog.dismiss();
                                    textView.setVisibility(View.VISIBLE);
                                    listView.setVisibility(View.GONE);
                                }else {
                                    textView.setVisibility(View.GONE);
                                    listView.setVisibility(View.VISIBLE);
                                    String [] file_name = new String[file_uploadeds.size()];
                                    String [] file_code = new String[file_uploadeds.size()];
                                    String [] senderName = new String[file_uploadeds.size()];
                                    for (int i=0; i<file_name.length; i++){

                                        file_name[i] = file_uploadeds.get(i).getFileName();
                                        file_code[i] = file_uploadeds.get(i).getStudyName();
                                        senderName[i] = file_uploadeds.get(i).getSenderName();
                                    }
                                    progressDialog.dismiss();
                                    MonAdapter adapter = new MonAdapter(context, file_code, file_name, senderName);
                                    listView.setAdapter(adapter);
                                }


                            }
                        });
                    }

                    if (page.equals("cours")){
                        ActionAboutFile.getParticularFile("Cour").addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                File_uploaded file_uploaded = null;
                                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                    file_uploaded = documentSnapshot.toObject(File_uploaded.class);
                                    file_uploadeds.add(file_uploaded);
                                }

                                if (file_uploaded == null){
                                    progressDialog.dismiss();
                                    textView.setVisibility(View.VISIBLE);
                                    listView.setVisibility(View.GONE);
                                }else {
                                    textView.setVisibility(View.GONE);
                                    listView.setVisibility(View.VISIBLE);
                                    String [] file_name = new String[file_uploadeds.size()];
                                    String [] file_code = new String[file_uploadeds.size()];
                                    String [] senderName = new String[file_uploadeds.size()];
                                    for (int i=0; i<file_name.length; i++){

                                        file_name[i] = file_uploadeds.get(i).getFileName();
                                        file_code[i] = file_uploadeds.get(i).getStudyName();
                                        senderName[i] = file_uploadeds.get(i).getSenderName();
                                    }
                                    progressDialog.dismiss();
                                    MonAdapter adapter = new MonAdapter(context, file_code, file_name, senderName);
                                    listView.setAdapter(adapter);
                                }


                            }
                        });
                    }
                    if (page.equals("exercices")){
                        ActionAboutFile.getParticularFile("Exercice").addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                File_uploaded file_uploaded = null;
                                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                    file_uploaded = documentSnapshot.toObject(File_uploaded.class);
                                    file_uploadeds.add(file_uploaded);
                                }


                                if (file_uploaded == null){
                                    progressDialog.dismiss();
                                    textView.setVisibility(View.VISIBLE);
                                    listView.setVisibility(View.GONE);
                                }else {
                                    textView.setVisibility(View.GONE);
                                    listView.setVisibility(View.VISIBLE);
                                    String [] file_name = new String[file_uploadeds.size()];
                                    String [] file_code = new String[file_uploadeds.size()];
                                    String [] senderName = new String[file_uploadeds.size()];
                                    for (int i=0; i<file_name.length; i++){

                                        file_name[i] = file_uploadeds.get(i).getFileName();
                                        file_code[i] = file_uploadeds.get(i).getStudyName();
                                        senderName[i] = file_uploadeds.get(i).getSenderName();
                                    }
                                    progressDialog.dismiss();
                                    MonAdapter adapter = new MonAdapter(context, file_code, file_name, senderName);
                                    listView.setAdapter(adapter);
                                }



                            }
                        });
                    }

                    if (page.equals("correction")){
                        ActionAboutFile.getParticularFile("Correction Exercice").addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                File_uploaded file_uploaded = null;
                                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                    file_uploaded = documentSnapshot.toObject(File_uploaded.class);
                                    file_uploadeds.add(file_uploaded);
                                }

                                if (file_uploaded == null){
                                    progressDialog.dismiss();
                                    textView.setVisibility(View.VISIBLE);
                                    listView.setVisibility(View.GONE);
                                }else {
                                    textView.setVisibility(View.GONE);
                                    listView.setVisibility(View.VISIBLE);
                                    String [] file_name = new String[file_uploadeds.size()];
                                    String [] file_code = new String[file_uploadeds.size()];
                                    String [] senderName = new String[file_uploadeds.size()];
                                    for (int i=0; i<file_name.length; i++){

                                        file_name[i] = file_uploadeds.get(i).getFileName();
                                        file_code[i] = file_uploadeds.get(i).getStudyName();
                                        senderName[i] = file_uploadeds.get(i).getSenderName();
                                    }
                                    progressDialog.dismiss();
                                    MonAdapter adapter = new MonAdapter(context, file_code, file_name, senderName);
                                    listView.setAdapter(adapter);
                                }



                            }
                        });
                    }

                }

                if (users.getStatut().equals("etudiant")){

                        if (page.equals("home")){
                            ActionAboutFile.getAllFile(users.getNiveau(), users.getClasse()).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    File_uploaded file_uploaded = null;
                                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                        file_uploaded = documentSnapshot.toObject(File_uploaded.class);
                                        file_uploadeds.add(file_uploaded);
                                    }

                                    if (file_uploaded == null){
                                        progressDialog.dismiss();
                                        textView.setVisibility(View.VISIBLE);
                                        listView.setVisibility(View.GONE);
                                    }else {
                                        textView.setVisibility(View.GONE);
                                        listView.setVisibility(View.VISIBLE);
                                        String [] file_name = new String[file_uploadeds.size()];
                                        String [] file_code = new String[file_uploadeds.size()];
                                        String [] senderName = new String[file_uploadeds.size()];
                                        for (int i=0; i<file_name.length; i++){

                                            file_name[i] = file_uploadeds.get(i).getFileName();
                                            file_code[i] = file_uploadeds.get(i).getStudyName();
                                            senderName[i] = file_uploadeds.get(i).getSenderName();
                                        }
                                        progressDialog.dismiss();
                                        MonAdapter adapter = new MonAdapter(context, file_code, file_name, senderName);
                                        listView.setAdapter(adapter);
                                    }

                                }
                            });
                        }

                        if (page.equals("cours")){
                            ActionAboutFile.getParticularFile(users.getNiveau(), users.getClasse(),"Cour").addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    File_uploaded file_uploaded = null;
                                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                        file_uploaded = documentSnapshot.toObject(File_uploaded.class);
                                        file_uploadeds.add(file_uploaded);
                                    }

                                    if (file_uploaded == null){
                                        progressDialog.dismiss();
                                        textView.setVisibility(View.VISIBLE);
                                        listView.setVisibility(View.GONE);
                                    }else {
                                        textView.setVisibility(View.GONE);
                                        listView.setVisibility(View.VISIBLE);
                                        String [] file_name = new String[file_uploadeds.size()];
                                        String [] file_code = new String[file_uploadeds.size()];
                                        String [] senderName = new String[file_uploadeds.size()];
                                        for (int i=0; i<file_name.length; i++){

                                            file_name[i] = file_uploadeds.get(i).getFileName();
                                            file_code[i] = file_uploadeds.get(i).getStudyName();
                                            senderName[i] = file_uploadeds.get(i).getSenderName();
                                        }
                                        progressDialog.dismiss();
                                        MonAdapter adapter = new MonAdapter(context, file_code, file_name, senderName);
                                        listView.setAdapter(adapter);
                                    }



                                }
                            });
                        }
                        if (page.equals("exercices")){
                            ActionAboutFile.getParticularFile(users.getNiveau(), users.getClasse(),"Exercice").addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    File_uploaded file_uploaded = null;
                                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                        file_uploaded = documentSnapshot.toObject(File_uploaded.class);
                                        file_uploadeds.add(file_uploaded);
                                    }

                                    if (file_uploaded == null){
                                        progressDialog.dismiss();
                                        textView.setVisibility(View.VISIBLE);
                                        listView.setVisibility(View.GONE);
                                    }else {
                                        textView.setVisibility(View.GONE);
                                        listView.setVisibility(View.VISIBLE);
                                        String [] file_name = new String[file_uploadeds.size()];
                                        String [] file_code = new String[file_uploadeds.size()];
                                        String [] senderName = new String[file_uploadeds.size()];
                                        for (int i=0; i<file_name.length; i++){

                                            file_name[i] = file_uploadeds.get(i).getFileName();
                                            file_code[i] = file_uploadeds.get(i).getStudyName();
                                            senderName[i] = file_uploadeds.get(i).getSenderName();
                                        }
                                        progressDialog.dismiss();
                                        MonAdapter adapter = new MonAdapter(context, file_code, file_name, senderName);
                                        listView.setAdapter(adapter);
                                    }


                                }
                            });
                        }

                        if (page.equals("correction")){
                            ActionAboutFile.getParticularFile(users.getNiveau(), users.getClasse(),"Correction Exercice").addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    File_uploaded file_uploaded = null;
                                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                        file_uploaded = documentSnapshot.toObject(File_uploaded.class);
                                        file_uploadeds.add(file_uploaded);
                                    }

                                    if (file_uploaded == null){
                                        progressDialog.dismiss();
                                        textView.setVisibility(View.VISIBLE);
                                        listView.setVisibility(View.GONE);
                                    }else {
                                        textView.setVisibility(View.GONE);
                                        listView.setVisibility(View.VISIBLE);
                                        String [] file_name = new String[file_uploadeds.size()];
                                        String [] file_code = new String[file_uploadeds.size()];
                                        String [] senderName = new String[file_uploadeds.size()];
                                        for (int i=0; i<file_name.length; i++){

                                            file_name[i] = file_uploadeds.get(i).getFileName();
                                            file_code[i] = file_uploadeds.get(i).getStudyName();
                                            senderName[i] = file_uploadeds.get(i).getSenderName();
                                        }
                                        progressDialog.dismiss();
                                        MonAdapter adapter = new MonAdapter(context, file_code, file_name, senderName);
                                        listView.setAdapter(adapter);
                                    }


                                }
                            });
                        }
                }

            }
        });
    }


    public static void Download(Context context, String file_name, String file_extension, String url){
        if (haveStoragePermission(context)){
            DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            Uri uri = Uri.parse(url);
            DownloadManager.Request request = new DownloadManager.Request(uri);

            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            //request.setDestinationInExternalFilesDir(context, destinationDirectory, file_name + file_extension);
            request.setVisibleInDownloadsUi(true);
            request.setDestinationInExternalPublicDir("/PolyFast", "/Download"  + "/" + file_name + file_extension);

            assert downloadManager != null;
            downloadManager.enqueue(request);
        }

    }

    private static boolean haveStoragePermission(Context context) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (context.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.e("Permission error","You have permission");
                return true;
            } else {

                Log.e("Permission error","You have asked for permission");
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //you dont need to worry about these stuff below api level 23
            Log.e("Permission error","You already have the permission");
            return true;
        }
    }


}


class MonAdapter extends ArrayAdapter<String>{
        Context context;
        String nom_fichier[];
        String expediteur[];
        String code_matiere[];


        MonAdapter(Context m_context, String m_code_matiere[], String m_titre[], String m_expediteur[]){
            super(m_context, R.layout.listview_content, m_titre);
            context = m_context;
            code_matiere = m_code_matiere;
            nom_fichier = m_titre;
            expediteur = m_expediteur;
        }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater)context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert layoutInflater != null;
        View view = layoutInflater.inflate(R.layout.listview_content, parent, false);
        TextView nom_fichier_present = view.findViewById(R.id.nom_fichier);
        TextView  nom_expediteur_present = view.findViewById(R.id.nom_expediteur);

        nom_fichier_present.setText(code_matiere[position]+"  "+nom_fichier[position]);
        nom_expediteur_present.setText(context.getResources().getString(R.string.upload_by)+" "+expediteur[position]);

        return view;
    }

}
