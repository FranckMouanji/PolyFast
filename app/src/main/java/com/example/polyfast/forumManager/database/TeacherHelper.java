package com.example.polyfast.forumManager.database;

import com.example.polyfast.forumManager.models.Teacher;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Class to managed the teacher firebase database.
 *
 * @author Ronald Tchuekou.
 *
 */
public class TeacherHelper {

   private static final String COLLECTION_NAME = "Teacher";

   public static CollectionReference getCollectionReference() {
      return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
   }

   /**
    * Function to get the Teacher information.
    * @param teacherId Id of the Teacher.
    * @return Container of User reference.
    */
   public static Task<DocumentSnapshot> getTeacher (String teacherId) {
      return getCollectionReference().document(teacherId).get();
   }

   /**
    * Function to get teacher by e-mail address.
    * @param mail mail address.
    */
   public static Task<QuerySnapshot> getTeacherByMail(String mail) {
      return getCollectionReference().whereEqualTo("email", mail).get();
   }

   /**
    * Function to add a Teacher to the data base.
    * @param teacher New Teacher.
    */
   public static Task<DocumentReference> addTeacher(Teacher teacher) {
      return getCollectionReference().add(teacher);
   }

   /**
    * Function to update the Teacher in data base.
    * @param email Teacher mail.
    * @param password Teacher password.
    */
   public static Task<QuerySnapshot> updateTeacher (String email, String password) {

      Map<String, Object> teacherMap = new HashMap<>();

      teacherMap.put("email", email);
      teacherMap.put("password", password);

      return getCollectionReference().whereEqualTo("email", email).get()
            .addOnCompleteListener(command -> {
               if (command.isSuccessful())
                  getCollectionReference().document(
                        Objects.requireNonNull(command.getResult())
                              .getDocuments()
                              .get(0).getId()
                  ).update(teacherMap);
            });
   }


   /**
    * Function to update the token.
    * @param token Token.
    * @param userId User id.
    */
   public static void updateToken(String token, String userId) {
      getCollectionReference().document(userId).update("token", token);
   }
}
