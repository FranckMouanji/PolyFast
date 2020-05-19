package com.example.polyfast.forumManager.components;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.polyfast.R;
import com.example.polyfast.forumManager.adapters.CommentAdapter;
import com.example.polyfast.forumManager.database.CommentHelper;
import com.example.polyfast.forumManager.database.QuestionHelper;
import com.example.polyfast.forumManager.database.ResponseHelper;
import com.example.polyfast.forumManager.database.SQLiteUserManager;
import com.example.polyfast.forumManager.database.StudentHelper;
import com.example.polyfast.forumManager.database.TeacherHelper;
import com.example.polyfast.forumManager.models.Client;
import com.example.polyfast.forumManager.models.Data;
import com.example.polyfast.forumManager.models.ForumComment;
import com.example.polyfast.forumManager.models.ForumQuestion;
import com.example.polyfast.forumManager.models.ForumResponse;
import com.example.polyfast.forumManager.models.MyResponse;
import com.example.polyfast.forumManager.models.Sender;
import com.example.polyfast.forumManager.models.Student;
import com.example.polyfast.forumManager.models.Teacher;
import com.example.polyfast.forumManager.models.User;
import com.example.polyfast.forumManager.services.ApiService;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BottomSheetComments extends BottomSheetDialogFragment {

   private static final String TAG = "BottomSheetComments";
   private BottomSheetBehavior mBehavior;
   private CommentAdapter adapter;
   private CommentPopup dialogComment;
   private ImageButton add_comment;
   private String responseId;

   private ApiService apiService;

   @NonNull
   @Override
   public Dialog onCreateDialog(Bundle savedInstanceState) {
      BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

      View v = View.inflate(getContext(), R.layout.bottomsheet_comment_response, null);

      apiService = Client.getClient("https://fcm.googleapis.com/").create(ApiService.class);

      assert getArguments() != null;
      responseId = getArguments().getString("response_id");

      ConstraintLayout layout = v.findViewById(R.id.root);
      LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) layout.getLayoutParams();
      params.height = getScreenHeight() - 100;
      layout.setLayoutParams(params);

      ImageButton close_btn = v.findViewById(R.id.close_btn);
      add_comment = v.findViewById(R.id.add_comment_btn);

      Query query = CommentHelper.getComments(responseId);

      FirestoreRecyclerOptions<ForumComment> options = new FirestoreRecyclerOptions
            .Builder<ForumComment>()
            .setQuery(query, ForumComment.class)
            .build();

      adapter = new CommentAdapter(options);

      RecyclerView recyclerView = v.findViewById(R.id.recycler_comment);
      recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
      recyclerView.setHasFixedSize(true);
      recyclerView.setAdapter(adapter);

      close_btn.setOnClickListener(v12 -> dismiss());

      dialog.setContentView(v);
      mBehavior = BottomSheetBehavior.from((View) v.getParent());
      return dialog;
   }


   @Override
   public void onStart() {
      super.onStart();

      adapter.startListening();

      mBehavior.setPeekHeight(getScreenHeight() - 100);
      mBehavior.setSkipCollapsed(false);
      mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

      dialogComment = new CommentPopup(requireContext(),
            R.style.Theme_MaterialComponents_BottomSheetDialog);

      add_comment.setOnClickListener(v13 -> {
         dialogComment.show();
         Toast.makeText(getContext(), "Add a new comment.", Toast.LENGTH_SHORT).show();
      });

      // Add new Comment.
      dialogComment.setOnSendCommentListener(comment -> {
         User user = new SQLiteUserManager(getContext()).getUserInfo();

         ForumComment forumComment = new ForumComment(null, user.getId(), comment, Calendar
               .getInstance().getTime(), responseId, user instanceof Student ? "student" : "teacher");
         ProgressDialog progressDialog = ProgressDialog.show(getContext(), null,
               requireContext().getString(R.string.loading_send));
         CommentHelper.addComment(forumComment).addOnCompleteListener(complete->{
            if (complete.isSuccessful()){
               ResponseHelper.updateCommentCount(responseId)
                     .addOnFailureListener(failure-> Toast.makeText(requireContext(),
                           requireContext().getString(R.string.error_has_provided), Toast.LENGTH_SHORT).show())
                     .addOnSuccessListener(success->{
                        forumComment.setId(Objects.requireNonNull(complete.getResult()).getId());
                        ForumResponse response = success.toObject(ForumResponse.class);
                        assert response != null;
                        response.setId(success.getId());
                        notifyResponseAuthor(response, user, forumComment);

                     });
            }
            progressDialog.dismiss();
         });

         dialogComment.dismiss();

      });

   }

   private void notifyResponseAuthor(ForumResponse response, User user, ForumComment comment) {
      QuestionHelper.getQuestionById(response.getQuestionId()).get().addOnSuccessListener(task->{
         ForumQuestion question = task.toObject(ForumQuestion.class);
         assert question != null;
         question.setId(task.getId());
         TeacherHelper.getCollectionReference().get()
               .addOnCompleteListener(queries -> {
                  if (!queries.isSuccessful()){
                     Log.i(TAG, "sendNotification: error when get the users.", queries.getException());
                     return;
                  }

                  List<DocumentSnapshot> documents = Objects.requireNonNull(queries.getResult()).getDocuments();

                  for (DocumentSnapshot document : documents) {
                     Teacher teacher = document.toObject(Teacher.class);
                     assert teacher != null;
                     teacher.setId(document.getId());
                     String[] materials = teacher.getMaterial().split("/");

                     for (String material : materials)
                        if (material.equals(question.getMaterial())
                              && !teacher.getId().equals(user.getId())) {
                           sendUpStream(user, teacher.getToken(), comment, question.getId());
                           Log.i(TAG, teacher + " are notify by response : " + comment);
                        }
                  }

               });

         StudentHelper.getCollectionReference().get()
               .addOnCompleteListener(snapshotTask -> {
                  if (!snapshotTask.isSuccessful()){
                     Log.i(TAG, "sendNotification: error : ", snapshotTask.getException());
                     return;
                  }
                  List<DocumentSnapshot> documents = Objects.requireNonNull(snapshotTask.getResult()).getDocuments();
                  for (DocumentSnapshot document : documents) {
                     Student student = document.toObject(Student.class);
                     assert student != null;
                     student.setId(document.getId());

                     if (student.getClassLevel().equals(question.getClassName())
                           && !student.getId().equals(user.getId())){
                        sendUpStream(user, student.getToken(), comment, question.getId());
                        Log.i(TAG, student + " are notify by response : " + comment);
                     }
                  }
               });

      });
   }

   private void sendUpStream(User user, String token, ForumComment comment, String questionId) {

      String senderStr;
      if (user instanceof Teacher)
         senderStr = "Mr : "+user.getName() + " " + user.getSurname();
      else
         senderStr = user.getName() + " " + user.getSurname();

      Data data = new Data(senderStr, comment.getComment() +"/"+ questionId,
            responseId, "comment");

      Sender sender = new Sender(data, token);

      apiService.sendNotification(sender)
            .enqueue(new Callback<MyResponse>() {

               @Override
               public void onResponse(@NonNull Call<MyResponse> call,
                                      @NonNull Response<MyResponse> response) {

                  if (response.code() == 200){

                     assert response.body() != null;

                     if (response.body().success != 1)
                        Toast.makeText(requireContext(), getString(R.string.error_has_provided),
                              Toast.LENGTH_SHORT).show();
                     else
                        Log.i(TAG, "onResponse: Notification is sent.");
                  }
               }

               @Override
               public void onFailure(@NonNull Call<MyResponse> call, @NonNull Throwable t) {
                  Log.i(TAG, "onFailure: The notification is not send.",t);
               }
            });
   }


   private static int getScreenHeight() {
      return Resources.getSystem().getDisplayMetrics().heightPixels;
   }

   @Override
   public void onDestroy() {
      super.onDestroy();
      adapter.stopListening();
   }
}
