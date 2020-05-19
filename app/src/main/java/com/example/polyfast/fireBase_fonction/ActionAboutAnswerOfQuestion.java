package com.example.polyfast.fireBase_fonction;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.polyfast.activities.Reset_Password;
import com.example.polyfast.R;
import com.example.polyfast.data_class.Answer;
import com.example.polyfast.data_class.Users;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class ActionAboutAnswerOfQuestion {

    private static final String COLLECTION_NAME = "Answer";
    private static final String ID_USER = "id_user";

    public static CollectionReference getAnswerCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    public static Task<DocumentReference> addAnswer(Answer answer){
        return ActionAboutAnswerOfQuestion.getAnswerCollection().add(answer);
    }

    private static Task<QuerySnapshot> getParticularAnswer(String Id_user){
        return ActionAboutAnswerOfQuestion.getAnswerCollection()
                .whereEqualTo(ID_USER, Id_user)
                .get();
    }

    public static void verifAnswer(final Answer answer1, final Answer answer2, final Answer answer3, final Context context, final TextView error_answer1, final TextView error_answer2, final TextView error_answer3){
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle(context.getResources().getString(R.string.wait));
        progressDialog.show();
        ActionAboutUsers.getParticularUser(answer1.getId_user()).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                boolean exist = false;
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Users user = documentSnapshot.toObject(Users.class);
                    String recupE_mail = user.getEmail();
                    if (recupE_mail.equals(answer1.getId_user())) {
                        exist = true;
                    }
                }
                if (exist) {
                    ActionAboutAnswerOfQuestion.getParticularAnswer(answer1.getId_user()).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots1) {
                            boolean verif_answe1 = false;
                            boolean verif_answe2 = false;
                            boolean verif_answe3 = false;
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots1) {
                                Answer answer = documentSnapshot.toObject(Answer.class);
                                if (answer.getId_questionnaire().equals(answer1.getId_questionnaire())) {
                                    if (answer.getResponse().equals(answer1.getResponse())) {
                                        verif_answe1 = true;
                                    }
                                }
                                if (answer.getId_questionnaire().equals(answer2.getId_questionnaire())) {
                                    if (answer.getResponse().equals(answer2.getResponse())) {
                                        verif_answe2 = true;
                                    }
                                }

                                if (answer.getId_questionnaire().equals(answer3.getId_questionnaire())) {
                                    if (answer.getResponse().equals(answer3.getResponse())) {
                                        verif_answe3 = true;
                                    }
                                }
                            }
                            progressDialog.dismiss();

                            if (!verif_answe1) {
                                error_answer1.setVisibility(View.VISIBLE);
                            }else {
                                if (error_answer1.getVisibility() == View.VISIBLE){
                                    error_answer1.setVisibility(View.GONE);
                                }
                            }
                            if (!verif_answe2) {
                                error_answer2.setVisibility(View.VISIBLE);
                            }else {
                                if (error_answer2.getVisibility() == View.VISIBLE){
                                    error_answer2.setVisibility(View.GONE);
                                }
                            }
                            if (!verif_answe3) {
                                error_answer3.setVisibility(View.VISIBLE);
                            }else {
                                if (error_answer3.getVisibility() == View.VISIBLE){
                                    error_answer3.setVisibility(View.GONE);
                                }
                            }
                            if (verif_answe1 && verif_answe2 && verif_answe3) {
                                String mail = answer1.getId_user();
                                Intent intent = new Intent(context, Reset_Password.class);
                                intent.putExtra("mail", mail);
                                context.startActivity(intent);
                                ((Activity) context).finish();
                            }


                        }
                    });
                } else {
                    Toast.makeText(context, context.getResources().getString(R.string.users_not_exist), Toast.LENGTH_SHORT).show();
                }
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
}
