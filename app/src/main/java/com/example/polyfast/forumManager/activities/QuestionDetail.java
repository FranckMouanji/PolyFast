package com.example.polyfast.forumManager.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.example.polyfast.forumManager.adapters.ResponseAdapter;
import com.example.polyfast.forumManager.database.QuestionHelper;
import com.example.polyfast.forumManager.database.ResponseHelper;
import com.example.polyfast.forumManager.database.SQLiteUserManager;
import com.example.polyfast.forumManager.database.StudentHelper;
import com.example.polyfast.forumManager.database.TeacherHelper;
import com.example.polyfast.forumManager.models.Client;
import com.example.polyfast.forumManager.models.Data;
import com.example.polyfast.forumManager.models.ForumQuestion;
import com.example.polyfast.forumManager.models.MyResponse;
import com.example.polyfast.forumManager.models.Sender;
import com.example.polyfast.forumManager.models.Student;
import com.example.polyfast.forumManager.models.Teacher;
import com.example.polyfast.forumManager.models.User;
import com.example.polyfast.forumManager.services.ApiService;
import com.example.polyfast.forumManager.utils.Utils;
import com.example.polyfast.fragment.Forum;
import com.example.polyfast.R;
import com.example.polyfast.forumManager.components.BottomSheetComments;
import com.example.polyfast.forumManager.components.BottomSheetResponse;
import com.example.polyfast.forumManager.models.ForumResponse;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;

import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class QuestionDetail extends AppCompatActivity implements BottomSheetResponse.BottomSheetListener{

   private static final String TAG = "QuestionDetail";
   private String question_id;
   private ResponseAdapter adapter;
   private ListenerRegistration questionListener;
   BottomSheetResponse bottomSheetResponse = new BottomSheetResponse();
   BottomSheetComments bottomSheetComments = new BottomSheetComments();
   RecyclerView recyclerView;

   MaterialCardView contentImage1, contentImage2;
   ImageView image1, image2;
   TextView question_label, last_author, last_date, push_author,
         push_date, question_description, imageDescription1, imageDescription2;
   Button answer_btn, ask_question_btn;
   private ForumQuestion question;
   private ApiService apiService;


   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_question_detail);

      apiService = Client.getClient("https://fcm.googleapis.com/").create(ApiService.class);
      question_id = getIntent().getStringExtra(Forum.QUESTION_ID);

      // Navigate to the forum fragment where click on the notification summary.
      if (getIntent() != null && getIntent().hasExtra("Type")){
         String type = getIntent().getStringExtra("Type");
         String responseId = getIntent().getStringExtra("responseId");
         assert type != null;
         if (type.equals("comment")){

            Bundle dataBundle = new Bundle();
            dataBundle.putString("response_id", responseId);

            bottomSheetComments.setArguments(dataBundle);
            bottomSheetComments.setCancelable(false);
            bottomSheetComments.show(getSupportFragmentManager(), "BottomSheetComments");

            Log.i(TAG, "Response id : " + responseId);
         }
      }


      MaterialToolbar toolbar = findViewById(R.id.toolbar);
      setSupportActionBar(toolbar);
      if (getSupportActionBar() != null) {
         getSupportActionBar().setDisplayHomeAsUpEnabled(true);
         getSupportActionBar().setDisplayShowHomeEnabled(true);
         getSupportActionBar().setTitle(R.string.questionDetail);
      }

      initView();

      setQuestion();

      Query query = ResponseHelper.getResponses(question_id);

      FirestoreRecyclerOptions<ForumResponse> options = new FirestoreRecyclerOptions
            .Builder<ForumResponse>()
            .setQuery(query, ForumResponse.class)
            .build();

      adapter = new ResponseAdapter(options);

      recyclerView = findViewById(R.id.forum_response);
      recyclerView.setLayoutManager(new LinearLayoutManager(this));
      recyclerView.setHasFixedSize(true);
      recyclerView.setAdapter(adapter);

      adapter.setOnResponseItemButtonClickListener(new ResponseAdapter
            .OnResponseItemButtonClickListener() {
         @Override
         public void onCommentsButtonClick(int position) {

            Bundle dataBundle = new Bundle();
            dataBundle.putString("response_id", adapter.getSnapshots()
                  .getSnapshot(position).getId());

            bottomSheetComments.setArguments(dataBundle);
            bottomSheetComments.setCancelable(false);
            bottomSheetComments.show(getSupportFragmentManager(), "BottomSheetComments");

         }

         @Override
         public void onValidateButtonClick(int position) {
            ResponseHelper.updateValidateCount(adapter.getSnapshots()
                  .getSnapshot(position).getId())
                  .addOnSuccessListener(success-> Toast.makeText(QuestionDetail.this,
                        getString(R.string.responses_validate), Toast.LENGTH_SHORT).show())
                  .addOnFailureListener(failure-> Toast.makeText(QuestionDetail.this,
                        getString(R.string.error_has_provided), Toast.LENGTH_SHORT).show());
         }

         @Override
         public void onImageClick(int position , View v) {
            ForumResponse response = adapter.getItem(position);

            ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat
                  .makeSceneTransitionAnimation(QuestionDetail.this,v,"image");

            Intent fullImageIntent = new Intent(QuestionDetail.this, FullImageView.class);
            fullImageIntent.putExtra("imageUri", response.getImage1());
            startActivity(fullImageIntent, activityOptionsCompat.toBundle());
         }
      });


   }

   /**
    * Function to initialize the view.
    */
   private void initView() {
      contentImage1 = findViewById(R.id.contentImage1);
      contentImage2 = findViewById(R.id.contentImage2);
      image1 = findViewById(R.id.image1);
      image2 = findViewById(R.id.image2);
      question_label = findViewById(R.id.question_label);
      last_author = findViewById(R.id.last_author);
      last_date = findViewById(R.id.last_date);
      push_author = findViewById(R.id.push_author);
      push_date = findViewById(R.id.push_date);
      question_description = findViewById(R.id.question_description);
      imageDescription1 = findViewById(R.id.imageDescription1);
      imageDescription2 = findViewById(R.id.imageDescription2);
      answer_btn = findViewById(R.id.answer_btn);
      ask_question_btn = findViewById(R.id.ask_question_btn);

      image1.setOnClickListener(v -> {
         ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat
               .makeSceneTransitionAnimation(QuestionDetail.this,v,"image");
         Intent fullImageIntent = new Intent(QuestionDetail.this, FullImageView.class);
         fullImageIntent.putExtra("imageUri", question.getImage1());
         startActivity(fullImageIntent, activityOptionsCompat.toBundle());
      });

      image2.setOnClickListener(v -> {
         ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat
               .makeSceneTransitionAnimation(QuestionDetail.this,v,"image");
         Intent fullImageIntent = new Intent(QuestionDetail.this, FullImageView.class);
         fullImageIntent.putExtra("imageUri", question.getImage2());
         startActivity(fullImageIntent, activityOptionsCompat.toBundle());
      });

      answer_btn.setOnClickListener(v -> {
         Bundle dataBundle = new Bundle();
         dataBundle.putString("question_id", question_id);

         bottomSheetResponse.setArguments(dataBundle);
         bottomSheetResponse.setCancelable(false);
         bottomSheetResponse.show(getSupportFragmentManager(), "BottomSheetResponse");
      });

      ask_question_btn.setOnClickListener(v -> {
         startActivity(new Intent(QuestionDetail.this, AskQuestion.class));
         finish();
      });
   }


   @Override
   public boolean onOptionsItemSelected(@NonNull MenuItem item) {
      finish();
      return true;
   }

   @Override
   public void onButtonClicked(String text, String imageUrl) { // TO ADD RESPONSE TO THE DATA BASE.

      User user = new SQLiteUserManager(this).getUserInfo();
      ProgressDialog progressDialog = ProgressDialog.show(this, null, getString(R.string.loading_send));
      ForumResponse response = new ForumResponse();
      response.setAuthorId(user.getId());
      response.setAnswerDate(Calendar.getInstance().getTime());
      response.setResponse(text);
      if (imageUrl == null)
         response.setImage1("null");
      else
         response.setImage1(imageUrl);
      response.setQuestionId(question_id);
      response.setCommentCount(0);
      response.setValidateCount(0);
      response.setStatus(user instanceof Student ? "student" : "teacher");

      ResponseHelper.addResponse(response).addOnCompleteListener(complete->{
         if (complete.isSuccessful()) {
            response.setId(Objects.requireNonNull(complete.getResult()).getId());
            QuestionHelper.updateLastAnswer(question_id, user.getName(), response.getAnswerDate());
            sendNotification(response);
            Log.i(TAG, "Response : " + response + " is sent successfully");
         }
         else
            Toast.makeText(this, getString(R.string.error_has_provided),
                  Toast.LENGTH_SHORT).show();

         progressDialog.dismiss();
      });

   }

   private void sendNotification(ForumResponse response) {

      User user = new SQLiteUserManager(this).getUserInfo();

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
                           sendUpStream(user, teacher.getToken(), response);
                           Log.i(TAG, teacher + " are notify by response : " + response);
                        }
                  }

               });

         StudentHelper.getCollectionReference().get()
               .addOnCompleteListener(task -> {
                  if (!task.isSuccessful()){
                     Log.i(TAG, "sendNotification: error : ", task.getException());
                     return;
                  }
                  List<DocumentSnapshot> documents = Objects.requireNonNull(task.getResult()).getDocuments();
                  for (DocumentSnapshot document : documents) {
                     Student student = document.toObject(Student.class);
                     assert student != null;
                     student.setId(document.getId());

                     if (student.getClassLevel().equals(question.getClassName())
                           && !student.getId().equals(user.getId())){
                        sendUpStream(user, student.getToken(), response);
                        Log.i(TAG, student + " are notify by response : " + response);
                     }
                  }
               });


   }

   private void sendUpStream(User user, String token, ForumResponse response) {

      String senderStr;
      if (user instanceof Teacher)
         senderStr = "Mr : "+user.getName() + " " + user.getSurname();
      else
         senderStr = user.getName() + " " + user.getSurname();

      Data data = new Data(senderStr, response.getResponse() +"/"+ response.getQuestionId(),
            question.getMaterial(), "response");

      Sender sender = new Sender(data, token);

      apiService.sendNotification(sender)
            .enqueue(new Callback<MyResponse>() {

               @Override
               public void onResponse(@NonNull Call<MyResponse> call,
                                      @NonNull Response<MyResponse> response) {

                  if (response.code() == 200){

                     assert response.body() != null;

                     if (response.body().success != 1)
                        Toast.makeText(QuestionDetail.this, getString(R.string.error_has_provided),
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

   /**
    * Function to set the question view values.
    */
   private void setQuestion() {

      questionListener = QuestionHelper.getQuestionById(question_id)
            .addSnapshotListener((snapshot, e) -> {
               if (e != null)
                  return;

               if (snapshot != null && snapshot.exists()){

                  question = snapshot.toObject(ForumQuestion.class);

                  String date_last;
                  assert question != null;

                  question.setId(snapshot.getId());

                  if (question.getLastAnswerDate() == null)
                     date_last = getString(R.string.no_answer);
                  else
                     date_last = Utils.getFullDateFormat(question.getLastAnswerDate())
                           + " " + getString(R.string.at) + " " +
                           Utils.getFullTimeFormat(question.getLastAnswerDate());

                  String date_push = Utils.getFullDateFormat(question.getPushDate())
                        + " " + getString(R.string.at) + " " +
                        Utils.getFullTimeFormat(question.getPushDate());

                  question_label.setText(question.getLabel());
                  last_author.setText(
                        question.getLastAnswerAuthorId()
                              .equals("null") ? getString(R.string.no_answer) : question
                              .getLastAnswerAuthorId()
                  );

                  last_date.setText(date_last);

                  StudentHelper.getStudent(question.getAuthorId())
                        .addOnSuccessListener(success-> {
                           Student student = success.toObject(Student.class);
                           assert student != null;
                           String authorName = student.getName() + student.getSurname();
                           push_author.setText(authorName);
                        });

                  push_date.setText(date_push);
                  question_description.setText(question.getDescription());

                  if (!question.getImage1().equals("null")){
                     contentImage1.setVisibility(View.VISIBLE);
                     imageDescription1.setText(question.getImageDescription1());

                     Glide.with(this)
                           .load(Uri.parse(question.getImage1()))
                           .override(Target.SIZE_ORIGINAL)
                           .centerCrop()
                           .error(getResources().getDrawable(R.drawable.side_nav_bar))
                           .placeholder(getResources().getDrawable(R.drawable.side_nav_bar))
                           .into(image1);
                  }

                  if (!question.getImage2().equals("null")){
                     contentImage2.setVisibility(View.VISIBLE);
                     imageDescription2.setText(question.getImageDescription2());
                     Glide.with(this)
                           .load(Uri.parse(question.getImage2()))
                           .override(Target.SIZE_ORIGINAL)
                           .centerCrop()
                           .error(getResources().getDrawable(R.drawable.side_nav_bar))
                           .placeholder(getResources().getDrawable(R.drawable.side_nav_bar))
                           .into(image2);
                  }


               }
            });

   }

   @Override
   protected void onStart() {
      super.onStart();
      adapter.startListening();
   }

   @Override
   protected void onDestroy() {
      super.onDestroy();
      adapter.stopListening();
      questionListener.remove();
   }
}
