package com.example.polyfast.forumManager.database;

import com.example.polyfast.forumManager.models.Student;
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
 * Class to managed the student firebase database.
 *
 * @author Ronald Tchuekou.
 *
 */
public class StudentHelper {

   private static final String COLLECTION_NAME = "Student";

   private static CollectionReference getCollectionReference() {
      return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
   }

   /**
    * Function to get the student information.
    * @param studentId Id of the student.
    * @return Container of User reference.
    */
   public static Task<DocumentSnapshot> getStudent (String studentId) {
      return getCollectionReference().document(studentId).get();
   }

   /**
    * Function to get the student by e-mail address.
    * @param mail mail address.
    */
   public static Task<QuerySnapshot> getStudentByMail (String mail) {
      return getCollectionReference().whereEqualTo("email", mail).get();
   }

   /**
    * Function to add a student to the data base.
    * @param student New student.
    */
   public static Task<DocumentReference> addStudent(Student student) {
      return getCollectionReference().add(student);
   }

   /**
    * Function to update the student in data base.
    * @param mail Student mail.
    * @param password Student Pass.
    */
   public static Task<QuerySnapshot> updateStudent (String mail, String password) {

      Map<String, Object> studentMap = new HashMap<>();

      studentMap.put("email", mail);
      studentMap.put("password", password);

      return getCollectionReference().whereEqualTo("email", mail).get()
            .addOnCompleteListener(command -> {
               if (command.isSuccessful())
                  getCollectionReference().document(
                        Objects.requireNonNull(command.getResult())
                              .getDocuments()
                              .get(0).getId()
                  ).update(studentMap);
            });

   }

}
