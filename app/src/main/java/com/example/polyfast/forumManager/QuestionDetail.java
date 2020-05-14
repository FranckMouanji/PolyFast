package com.example.polyfast.forumManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.polyfast.forumManager.database.QuestionHelper;
import com.example.polyfast.forumManager.database.ResponseHelper;
import com.example.polyfast.forumManager.database.SQLiteUserManager;
import com.example.polyfast.forumManager.models.ForumQuestion;
import com.example.polyfast.forumManager.models.Student;
import com.example.polyfast.forumManager.models.User;
import com.example.polyfast.fragment.Forum;
import com.example.polyfast.R;
import com.example.polyfast.forumManager.components.BottomSheetComments;
import com.example.polyfast.forumManager.components.BottomSheetResponse;
import com.example.polyfast.forumManager.models.ForumModelsFactory;
import com.example.polyfast.forumManager.models.ForumResponse;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import static com.example.polyfast.fragment.Forum.POSITION;

public class QuestionDetail extends AppCompatActivity implements BottomSheetResponse.BottomSheetListener{

   public static final String ANSWER_COUNT_ACTION = "Answer_action";
   private static ForumModelsFactory NEXT_ELEMENT_ITEM ;
   private static ForumModelsFactory BUTTONS;
   private String question_id;
   private ForumAdapter adapter;
   private List<ForumModelsFactory> factories;
   BottomSheetResponse bottomSheetResponse = new BottomSheetResponse();
   BottomSheetComments bottomSheetComments = new BottomSheetComments();
   private RecyclerView recyclerView;
   private int position ;
   private BroadcastReceiver receiver;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_question_detail);

      MaterialToolbar toolbar = findViewById(R.id.toolbar);
      setSupportActionBar(toolbar);
      if (getSupportActionBar() != null) {
         getSupportActionBar().setDisplayHomeAsUpEnabled(true);
         getSupportActionBar().setDisplayShowHomeEnabled(true);
         getSupportActionBar().setTitle(R.string.questionDetail);
      }

      question_id = getIntent().getStringExtra(Forum.QUESTION_ID);
      position = getIntent().getIntExtra(POSITION, 0);

      NEXT_ELEMENT_ITEM = new ForumQuestion("next_element", null, null,
            null, null, null, null,
            null, null, null, 0,
            null, null, null );

      BUTTONS = new ForumQuestion("buttons", null, null,
            null, null, null, null,
            null, null, null, 0,
            null, null, null );

      factories = new ArrayList<>();

      adapter = new ForumAdapter(factories);

      recyclerView = findViewById(R.id.forum_recycler);
      recyclerView.setLayoutManager(new LinearLayoutManager(this));
      recyclerView.setHasFixedSize(true);
      recyclerView.setAdapter(adapter);

      adapter.setOnButtonsLastItemClickListener(new ForumAdapter.OnButtonsLastItemClickListener() {
         @Override
         public void onResponseButtonClick() {

            Bundle dataBundle = new Bundle();
            dataBundle.putString("question_id", question_id);

            bottomSheetResponse.setArguments(dataBundle);
            bottomSheetResponse.setCancelable(false);
            bottomSheetResponse.show(getSupportFragmentManager(), "BottomSheetResponse");
         }

         @Override
         public void onQuestionButtonClick() {
            startActivity(new Intent(QuestionDetail.this, AskQuestion.class));
            finish();
         }
      });

      adapter.setOnNextElementButtonClickListener(() -> {
         // TODO Managed to display the next element.
         Toast.makeText(this,
               "Complete the recycler view with another elements.", Toast.LENGTH_SHORT).show();
      });

      adapter.setOnResponseItemButtonClickListener(new ForumAdapter.OnResponseItemButtonClickListener() {
         @Override
         public void onCommentsButtonClick(int position) {

            Toast.makeText(QuestionDetail.this,
                  "Position : " + position, Toast.LENGTH_SHORT).show();

            Bundle dataBundle = new Bundle();
            dataBundle.putString("response_id", factories.get(position).getId());

            bottomSheetComments.setArguments(dataBundle);
            bottomSheetComments.setCancelable(false);
            bottomSheetComments.show(getSupportFragmentManager(), "BottomSheetComments");
            if (receiver == null) {
               receiver = new BroadcastReceiver() {
                  @Override
                  public void onReceive(Context context, Intent intent) {
                     ForumResponse response = (ForumResponse) factories.get(position);
                     response.setCommentCount(intent.getIntExtra("comment_count", 0));
                     factories.remove(position);
                     factories.add(position, response);
                     adapter.notifyItemChanged(position);
                  }
               };
               registerReceiver(receiver, new IntentFilter(BottomSheetComments.COMMENTED_ACTION));
            }

         }

         @Override
         public void onValidateButtonClick(int position) {
            Log.i("QuestionDetail", "Validate position id: "+factories.get(position).getId());
            ResponseHelper.updateValidateCount(factories.get(position).getId())
                  .addOnSuccessListener(success-> {
                     ForumResponse response = (ForumResponse) factories.get(position);
                     response.setValidateCount(response.getValidateCount()+1);
                     factories.remove(position);
                     factories.add(position, response);
                     adapter.notifyItemChanged(position);
                     Toast.makeText(QuestionDetail.this,
                           getString(R.string.responses_validate), Toast.LENGTH_SHORT).show();
                  })
                  .addOnFailureListener(failure-> Toast.makeText(QuestionDetail.this,
                        getString(R.string.error_has_provided), Toast.LENGTH_SHORT).show());
         }

         @Override
         public void onImageClick(int position , View v) {
            ForumResponse response = (ForumResponse) factories.get(position);
            ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat
                  .makeSceneTransitionAnimation(QuestionDetail.this,v,"image");
            Intent fullImageIntent = new Intent(QuestionDetail.this, FullImageView.class);
            fullImageIntent.putExtra("imageUri", response.getImage1());
            startActivity(fullImageIntent, activityOptionsCompat.toBundle());
            // Todo set an animation to display the full screen image.
         }
      });

      adapter.setOnQuestionItemClickListener(new ForumAdapter.OnQuestionItemClickListener() {
         @Override
         public void onImage1Click(int position, View v) {
            ForumQuestion question = (ForumQuestion) factories.get(position);
            ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat
                  .makeSceneTransitionAnimation(QuestionDetail.this,v,"image");
            Intent fullImageIntent = new Intent(QuestionDetail.this, FullImageView.class);
            fullImageIntent.putExtra("imageUri", question.getImage1());
            startActivity(fullImageIntent, activityOptionsCompat.toBundle());
         }

         @Override
         public void onImage2Click(int position, View v) {
            ForumQuestion question = (ForumQuestion) factories.get(position);
            ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat
                  .makeSceneTransitionAnimation(QuestionDetail.this,v,"image");
            Intent fullImageIntent = new Intent(QuestionDetail.this, FullImageView.class);
            fullImageIntent.putExtra("imageUri", question.getImage2());
            startActivity(fullImageIntent, activityOptionsCompat.toBundle());
         }
      });

      setListOfResponse();

   }

   /**
    * Function to set the list of the response.
    */
   private void setListOfResponse() {

      ProgressDialog progressDialog = ProgressDialog.show(this, null, getString(R.string.loading));
      QuestionHelper.getQuestionById(question_id).addOnCompleteListener(complete->{

         if (complete.isSuccessful()){

            ForumQuestion forumQuestion = Objects.requireNonNull(complete.getResult())
                  .toObject(ForumQuestion.class);

            assert forumQuestion != null;
            forumQuestion.setId(complete.getResult().getId());

            factories.add(forumQuestion);

            factories.add(NEXT_ELEMENT_ITEM);
            factories.add(BUTTONS);
            adapter.notifyDataSetChanged();

            ResponseHelper.getResponses(question_id).addOnCompleteListener(complete1-> {
               if (complete1.isSuccessful()){
                  for (DocumentSnapshot document : Objects.requireNonNull(complete1.getResult()).getDocuments()){
                     ForumResponse response = document.toObject(ForumResponse.class);
                     assert response != null;
                     response.setId(document.getId());
                     factories.add(1, response);
                     adapter.notifyItemInserted(1);
                  }

               }
            });

         }
         else
            Toast.makeText(this, getString(R.string.error_has_provided),
                  Toast.LENGTH_SHORT).show();

         progressDialog.dismiss();
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
            int index = getIndex();
            factories.add(index, response);
            adapter.incrementResponseCount();
            adapter.notifyItemInserted(index);
            recyclerView.scrollToPosition(index);

            QuestionHelper.updateLastAnswer(question_id, user.getName(), response.getAnswerDate());
         }
         else
            Toast.makeText(this, getString(R.string.error_has_provided),
                  Toast.LENGTH_SHORT).show();

         progressDialog.dismiss();
      });

   }

   /**
    * Function to get the index where can insert new response.
    */
   private int getIndex () {
      if (factories.contains(BUTTONS))
         return factories.indexOf(BUTTONS);
      else
         return factories.indexOf(NEXT_ELEMENT_ITEM);
   }

   @Override
   protected void onDestroy() {
      super.onDestroy();

      Intent intentBroadcast = new Intent();
      intentBroadcast.putExtra("response_count", getResponseCount());
      intentBroadcast.putExtra(POSITION, position);
      intentBroadcast.setAction(ANSWER_COUNT_ACTION);
      sendBroadcast(intentBroadcast);

      if (receiver != null)
         unregisterReceiver(receiver);

   }

   /**
    * Function get the count of response.
    */
   private int getResponseCount() {
      int count = 0;
      for (ForumModelsFactory factory : factories)
         if (factory instanceof ForumResponse)
            count ++;
      return count;
   }
}
