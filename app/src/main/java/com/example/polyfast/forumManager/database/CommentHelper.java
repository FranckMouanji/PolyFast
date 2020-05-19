package com.example.polyfast.forumManager.database;

import com.example.polyfast.forumManager.models.ForumComment;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

/**
 * class to managed the comments in the database.
 *
 * @author Ronald Tchuekou.
 */
public class CommentHelper {

   private static final String COLLECTION_NAME = "ForumComment";

   private static CollectionReference getCollectionReference() {
      return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
   }

   /**
    * Function to get Comment collection.
    */
   public static Query getComments (String responseId) {
      return getCollectionReference()
            .whereEqualTo("responseId", responseId)
            .orderBy("date", Query.Direction.ASCENDING);
   }

   /**
    * Function to add Comment document to the data base.
    * @param forumComment Comment .
    */
   public static Task<DocumentReference> addComment (ForumComment forumComment) {
      return getCollectionReference().add(forumComment);
   }

}
