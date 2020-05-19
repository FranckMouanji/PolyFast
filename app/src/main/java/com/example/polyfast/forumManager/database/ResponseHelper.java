package com.example.polyfast.forumManager.database;

import android.util.Log;

import com.example.polyfast.forumManager.models.ForumResponse;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Objects;

/**
 * Class to managed the response forum in the database.
 *
 * @author Ronald Tchuekou.
 */
public class ResponseHelper {

   private static final String COLLECTION_NAME = "ForumResponse";
   private static final String TAG = "ResponseHelper";

   private static CollectionReference getCollectionReference() {
      return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
   }

   /**
    * Function to get Response collection.
    */
   public static Query getResponses (String questionId) {
      return getCollectionReference()
            .whereEqualTo("questionId", questionId)
            .orderBy("answerDate", Query.Direction.DESCENDING);
   }

   /**
    * Function to add Response document to the data base.
    * @param forumResponse Response .
    */
   public static Task<DocumentReference> addResponse (ForumResponse forumResponse) {
      return getCollectionReference().add(forumResponse);
   }

   /**
    * Function to update the last user answer.
    * @param responseId Response id.
    */
   public static Task<DocumentSnapshot> updateValidateCount (String responseId) {

      return getCollectionReference().document(responseId).get().addOnCompleteListener(command -> {
         if (command.isSuccessful()) {
            getCollectionReference().document(responseId).update("validateCount", Objects
                  .requireNonNull(Objects.requireNonNull(command.getResult())
                  .toObject(ForumResponse.class)).getValidateCount() + 1);
         }
         else
            Log.e(TAG, "Error", command.getException());
      });
   }

   /**
    * Function to update the comment count of the response.
    * @param responseId Response id.
    */
   public static Task<DocumentSnapshot> updateCommentCount(String responseId) {
      return getCollectionReference().document(responseId).get().addOnCompleteListener(command -> {
         if (command.isSuccessful()) {
            getCollectionReference().document(responseId).update("commentCount", Objects
                  .requireNonNull(Objects.requireNonNull(command.getResult())
                        .toObject(ForumResponse.class)).getCommentCount() + 1);
         } else
            Log.e(TAG, "Error", command.getException());
      });
   }

}
