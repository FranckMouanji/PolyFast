package com.example.polyfast.fireBase_fonction;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.polyfast.Activities.PlateForm;
import com.example.polyfast.R;
import com.example.polyfast.data_class.Answer;
import com.example.polyfast.data_class.File_uploaded;
import com.example.polyfast.data_class.Users;
import com.example.polyfast.forumManager.utils.Utils;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ActionAboutUsers {

    private static final String COLLECTION_NAME = "Users";
    private static final String ID_USER = "email";

    private static CollectionReference getUsersCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    private static Task<DocumentReference> addUser(Users users){
        return ActionAboutUsers.getUsersCollection().add(users);
    }

    static Task<QuerySnapshot> getParticularUser(String mail){
        return ActionAboutUsers.getUsersCollection()
                .whereEqualTo(ID_USER, mail)
                .get();
    }

    public static void setUsersInFirestore(final Context context, final Users users, final Answer answer, final Answer answer1, final Answer answer2){
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle(context.getResources().getString(R.string.wait));
        progressDialog.show();
        String e_mail = users.getEmail();
        ActionAboutUsers.getParticularUser(e_mail).addOnSuccessListener(queryDocumentSnapshots -> {
            boolean exist = false;
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                Users user = documentSnapshot.toObject(Users.class);
                String recupE_mail = user.getEmail();
                if (recupE_mail.equals(users.getEmail())) {
                    exist = true;
                }
            }
            if (exist) {
                Toast.makeText(context, context.getResources().getString(R.string.user_exist), Toast.LENGTH_SHORT).show();
            } else {
                addUser(users).addOnSuccessListener(documentReference -> {
                    ActionAboutAnswerOfQuestion.addAnswer(answer);
                    ActionAboutAnswerOfQuestion.addAnswer(answer1);
                    ActionAboutAnswerOfQuestion.addAnswer(answer2);
                    Controller.create_file(users, context);
                    progressDialog.dismiss();
                    Intent intent = new Intent(context, PlateForm.class);
                    context.startActivity(intent);

                    Utils.addUser(users, context); // Add user in particular collection.

                    ((Activity)context).finish();
                });
            }
        })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(context, context.getResources().getString(R.string.verif_connexion), Toast.LENGTH_SHORT).show();
                });


    }



    public static void singUsers(final Context context, String mail, final String password,
                                 final TextView textView){

        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle(context.getResources().getString(R.string.wait));
        progressDialog.show();
        ActionAboutUsers.getParticularUser(mail).addOnSuccessListener(queryDocumentSnapshots -> {
            String password_recup = "";
            Users users = null;
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                users  = documentSnapshot.toObject(Users.class);
                password_recup = users.getPassword();
            }
            progressDialog.dismiss();
            if (! password_recup.equals("")){
                if (!(password_recup.equals(password))){
                    textView.setVisibility(View.VISIBLE);
                }else {
                    if (textView.getVisibility() == View.VISIBLE){
                        textView.setVisibility(View.GONE);
                    }

                    Controller.create_file(users, context);
                    Intent intent = new Intent(context, PlateForm.class);

                    Utils.addToSQL(users, context);

                    context.startActivity(intent);
                    ((Activity)context).finish();
                }
            }
        })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(context, context.getResources().getString(R.string.verif_connexion), Toast.LENGTH_SHORT).show();
                });
    }


    public static void updateUser(String mail, final Context context, final String password){
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle(context.getResources().getString(R.string.wait));
        progressDialog.show();
        ActionAboutUsers.getParticularUser(mail).addOnSuccessListener(queryDocumentSnapshots -> {
            Users users=null;
            String id = null;
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
               id = documentSnapshot.getId();
               users = documentSnapshot.toObject(Users.class);
            }
            progressDialog.dismiss();
            assert id != null;
            ActionAboutUsers.getUsersCollection().document(id).update("password", password);
            Toast.makeText(context, R.string.reussi, Toast.LENGTH_SHORT).show();
            users.setPassword(password);

            Controller.create_file(users, context);
            Intent intent = new Intent(context, PlateForm.class);
            context.startActivity(intent);

            Utils.updateUser(users, context); // Update the particular collection.

            ((Activity)context).finish();
        }).addOnFailureListener(e -> {
            progressDialog.dismiss();
            Toast.makeText(context, context.getResources().getString(R.string.verif_connexion), Toast.LENGTH_SHORT).show();
        });
    }


    public static void completeInformationTeacher(Context context, File_uploaded file_uploaded, Uri data){
        String mail = file_uploaded.getSenderMail();
        getParticularUser(mail).addOnSuccessListener(queryDocumentSnapshots -> {
            Users users = null;
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                users = documentSnapshot.toObject(Users.class);
            }
            assert users != null;
            file_uploaded.setStudyName(users.getMatiere_enseigner());
            ActionAboutFile.setFile(context, file_uploaded, data);
        });
    }


    public static void completeInformationUser(Context context, File_uploaded fileUploaded, Uri data){
        String mail = fileUploaded.getSenderMail();
        getParticularUser(mail).addOnSuccessListener(queryDocumentSnapshots -> {
            Users users = null;
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                users = documentSnapshot.toObject(Users.class);
            }
            assert users != null;
            fileUploaded.setFiliere(users.getClasse());
            ActionAboutFile.setFile(context, fileUploaded, data);
        });
    }

}
