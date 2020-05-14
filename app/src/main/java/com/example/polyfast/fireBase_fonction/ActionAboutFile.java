package com.example.polyfast.fireBase_fonction;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.polyfast.data_class.File_uploaded;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ActionAboutFile {

    private static final String COLLECTION_NAME = "File_uploaded";
    private static final String FILEPATH_NAME_COUR = "Cour/";
    private static final String FILEPATH_NAME_EXERCISE = "Exercice/";
    private static final String FILEPATH_NAME_ANSWER_OF_EXERCISE = "Correction_Des_Exercices/";
    private static final String NIVEAU = "niveau";
    private static final String FILIERE = "filiere";
    private static final String CATEGORIE = "categorie";



    private static CollectionReference getFileCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    private static StorageReference getFileReference(){
        return FirebaseStorage.getInstance().getReference();
    }

    private static Task<DocumentReference> addFile(File_uploaded fileUploaded){
        return ActionAboutFile.getFileCollection().add(fileUploaded);
    }

   //normal student
    public static Task<QuerySnapshot> getParticularFile(String level, String filiere, String categorie){
        return ActionAboutFile.getFileCollection()
                .whereEqualTo(FILIERE, filiere)
                .whereEqualTo(CATEGORIE, categorie)
                .get();
    }

    public static Task<QuerySnapshot> getAllFile(String level, String filiere){
        return ActionAboutFile.getFileCollection()
                .whereEqualTo(FILIERE, filiere)
                .get();
    }

    //teacher
    public static Task<QuerySnapshot> getParticularFile(String categorie){
        return ActionAboutFile.getFileCollection()
                .whereEqualTo(CATEGORIE, categorie)
                .get();
    }

    public static Task<QuerySnapshot> getAllFile(){
        return ActionAboutFile.getFileCollection()
                .get();
    }

    public static void setFile(Context context, File_uploaded fileUploaded, Uri data){
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();

        if (fileUploaded.getCategorie().equals("Cour")){

            StorageReference reference = getFileReference().child(FILEPATH_NAME_COUR+System.currentTimeMillis()+".pdf");
            reference.putFile(data)
                    .addOnSuccessListener(taskSnapshot -> {
                        Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uri.isComplete());
                        Uri url = uri.getResult();
                        fileUploaded.setFilePathInFirebase(url.toString());
                        ActionAboutFile.getFileCollection().document().set(fileUploaded);
                        Toast.makeText(context, "uploaded", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    })
                    .addOnProgressListener(taskSnapshot -> {
                        double progess = (100.0*taskSnapshot.getBytesTransferred())/ taskSnapshot.getTotalByteCount();
                        progressDialog.setMessage("Uploaded: "+(int)progess+"%");

                    });

        }else if (fileUploaded.getCategorie().equals("Exercice")){

            StorageReference reference = getFileReference().child(FILEPATH_NAME_EXERCISE+System.currentTimeMillis()+".pdf");
            reference.putFile(data)
                    .addOnSuccessListener(taskSnapshot -> {
                        Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uri.isComplete());
                        Uri url = uri.getResult();
                        fileUploaded.setFilePathInFirebase(url.toString());
                        ActionAboutFile.getFileCollection().document().set(fileUploaded);
                        Toast.makeText(context, "uploaded", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    })
                    .addOnProgressListener(taskSnapshot -> {
                        double progess = (100.0*taskSnapshot.getBytesTransferred())/ taskSnapshot.getTotalByteCount();
                        progressDialog.setMessage("Uploaded: "+(int)progess+"%");

                    });

        }else if (fileUploaded.getCategorie().equals("Correction Exercice")){

            StorageReference reference = getFileReference().child(FILEPATH_NAME_ANSWER_OF_EXERCISE+System.currentTimeMillis()+".pdf");
            reference.putFile(data)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uri.isComplete());
                            Uri url = uri.getResult();
                            fileUploaded.setFilePathInFirebase(url.toString());
                            ActionAboutFile.getFileCollection().document().set(fileUploaded);
                            Toast.makeText(context, "uploaded", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            double progess = (100.0*taskSnapshot.getBytesTransferred())/ taskSnapshot.getTotalByteCount();
                            progressDialog.setMessage("Uploaded: "+(int)progess+"%");

                        }
                    });

        }
    }
}
