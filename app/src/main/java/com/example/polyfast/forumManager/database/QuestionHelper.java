package com.example.polyfast.forumManager.database;

import android.util.Log;

import com.example.polyfast.forumManager.models.ForumQuestion;
import com.example.polyfast.forumManager.models.Student;
import com.example.polyfast.forumManager.models.Teacher;
import com.example.polyfast.forumManager.models.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Class to managed the question in the database.
 *
 * @author Ronald Tchuekou.
 */
public class QuestionHelper {

   private static final String COLLECTION_NAME = "ForumQuestion";
   private static final String TAG = "QuestionHelper";

   private static CollectionReference getCollectionReference() {
      return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
   }

   /**
    * Function to get question collection.
    */
   public static Task<QuerySnapshot> getQuestions (User user) {
      if (user instanceof Student) {
            Student student = (Student) user;
         return getCollectionReference()
               .whereEqualTo("className", student.getClassLevel())
               .orderBy("pushDate", Query.Direction.DESCENDING)
               .get();
      }
      else{
         Teacher teacher = (Teacher) user;
         String[] materials = teacher.getMaterial().split("/");
         return getCollectionReference()
               .whereIn("material", Arrays.asList(materials))
               .orderBy("pushDate", Query.Direction.DESCENDING)
               .get();
      }
   }

   /**
    * Function to get the question by id.
    * @param questionId Question id.
    */
   public static Task<DocumentSnapshot> getQuestionById (String questionId) {
      return getCollectionReference().document(questionId).get();
   }

   /**
    * Function to add question document to the data base.
    * @param forumQuestion Question .
    */
    public static Task<DocumentReference> addQuestion (ForumQuestion forumQuestion) {
      return getCollectionReference().add(forumQuestion);
    }

   /**
    * Function to update the last user answer.
    * @param questionId Question id.
    * @param lastAnswerAuthorId Author id.
    * @param date Date.
    */
    public static void updateLastAnswer (String questionId, String lastAnswerAuthorId, Date date) {
       Map<String, Object> questionMap = new HashMap<>();
       questionMap.put("lastAnswerAuthorId", lastAnswerAuthorId);
       questionMap.put("lastAnswerDate", date);

       getCollectionReference().document(questionId).get().addOnCompleteListener(command -> {
          if (command.isSuccessful()) {
             questionMap.put("responseCount", Objects.requireNonNull(Objects.requireNonNull(command.getResult())
                   .toObject(ForumQuestion.class)).getResponseCount() + 1);
             getCollectionReference().document(questionId).update(questionMap);
          } else
             Log.e(TAG, "Error", command.getException());
       });
    }

}
