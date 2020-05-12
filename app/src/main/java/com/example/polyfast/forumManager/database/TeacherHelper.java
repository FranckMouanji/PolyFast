package com.example.polyfast.forumManager.database;

import com.example.polyfast.forumManager.models.Teacher;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

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

   private static CollectionReference getCollectionReference() {
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
    * Function to add a Teacher to the data base.
    * @param teacher New Teacher.
    */
   public static void addTeacher(Teacher teacher) {
      getCollectionReference().add(teacher);
   }

   /**
    * Function to update the Teacher in data base.
    * @param email Teacher mail.
    * @param password Teacher password.
    */
   public static void updateTeacher (String email, String password) {

      Map<String, Object> teacherMap = new HashMap<>();

      teacherMap.put("email", email);
      teacherMap.put("password", password);

      getCollectionReference().whereEqualTo("email", email).get()
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
    * Function to deleted a Teacher to the data base.
    * @param teacherId Teacher id.
    */
   public static Task<Void> deletedTeacher (String teacherId) {
      return getCollectionReference().document(teacherId).delete();
   }

}
