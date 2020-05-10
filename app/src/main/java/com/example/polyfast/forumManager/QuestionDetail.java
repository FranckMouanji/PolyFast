package com.example.polyfast.forumManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.polyfast.Activities.ui.Forum;
import com.example.polyfast.R;
import com.example.polyfast.forumManager.bottomSheets.BottomSheetComments;
import com.example.polyfast.forumManager.bottomSheets.BottomSheetResponse;
import com.example.polyfast.forumManager.models.ResponseForum;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.List;

public class QuestionDetail extends AppCompatActivity implements BottomSheetResponse.BottomSheetListener{

   private String question_id;
   private ForumAdapter adapter;
   private List<ResponseForum> responses;
   BottomSheetResponse bottomSheetResponse = new BottomSheetResponse();
   BottomSheetComments bottomSheetComments = new BottomSheetComments();

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

      responses = new ArrayList<>();

      adapter = new ForumAdapter(responses);

      RecyclerView recyclerView = findViewById(R.id.forum_recycler);
      recyclerView.setLayoutManager(new LinearLayoutManager(this));
      recyclerView.setHasFixedSize(true);
      recyclerView.setAdapter(adapter);

      adapter.setOnButtonsLastItemClickListener(new ForumAdapter.OnButtonsLastItemClickListener() {
         @Override
         public void onResponseButtonClick() {
            bottomSheetResponse.setCancelable(false);
            bottomSheetResponse.show(getSupportFragmentManager(), "BottomSheetResponse");
         }

         @Override
         public void onQuestionButtonClick() {

            // TODO send the data.

            startActivity(new Intent(QuestionDetail.this, AskQuestion.class));

            finish();

         }
      });

      adapter.setOnNextElementButtonClickListener(() -> Toast.makeText(this,
            "Complete the recycler view with another elements.", Toast.LENGTH_SHORT).show());

      adapter.setOnResponseItemButtonClickListener(new ForumAdapter.OnResponseItemButtonClickListener() {
         @Override
         public void onCommentsButtonClick(int position) {
            Toast.makeText(QuestionDetail.this,
                  "Position : " + position, Toast.LENGTH_SHORT).show();
            bottomSheetComments.setCancelable(false);
            bottomSheetComments.show(getSupportFragmentManager(), "BottomSheetComments");
         }

         @Override
         public void onValidateButtonClick(int position) {
            Toast.makeText(QuestionDetail.this,
                  "Validate element on position : " + position, Toast.LENGTH_SHORT).show();
         }
      });

      setListOfResponse();

      question_id = getIntent().getStringExtra(Forum.QUESTION_ID);
      Toast.makeText(this, "Id of the question : " + question_id,
            Toast.LENGTH_SHORT).show();

   }

   /**
    * Function to set the list of the response.
    */
   private void setListOfResponse() {
      responses.add(new ResponseForum("question", null, null,
            null, null, question_id));

      responses.add(new ResponseForum("response", null, null,
            null, null, question_id));
      responses.add(new ResponseForum("response", null, null,
            null, null, question_id));

      responses.add(new ResponseForum("next_element", null, null,
            null, null, question_id));
      responses.add(new ResponseForum("buttons", null, null,
            null, null, question_id));

      adapter.notifyDataSetChanged();
   }

   @Override
   public boolean onOptionsItemSelected(@NonNull MenuItem item) {
      finish();
      return true;
   }

   @Override
   public void onButtonClicked(String text) {

      // Todo managed this part and send the response user.

      Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
   }

}
