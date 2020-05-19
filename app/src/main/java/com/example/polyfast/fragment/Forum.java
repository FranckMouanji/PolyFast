package com.example.polyfast.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.polyfast.R;
import com.example.polyfast.forumManager.activities.AskQuestion;
import com.example.polyfast.forumManager.adapters.MainAdapter;
import com.example.polyfast.forumManager.activities.QuestionDetail;
import com.example.polyfast.forumManager.database.QuestionHelper;
import com.example.polyfast.forumManager.database.SQLiteUserManager;
import com.example.polyfast.forumManager.models.ForumQuestion;
import com.example.polyfast.forumManager.models.Student;
import com.example.polyfast.forumManager.models.User;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.Query;


public class Forum extends Fragment {

   public static final String QUESTION_ID = "Question_id";
   private MainAdapter adapter;
   private RecyclerView recyclerView;

   @Override
   public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                            @Nullable Bundle savedInstanceState) {
      View v = inflater.inflate(R.layout.forum_fragment, container, false);

      recyclerView = v.findViewById(R.id.question_recycler);

      setFab(v);

      setListOfQuestion();

      return v;
   }

   /**
    * Function to set the floating action button.
    * @param v Content views.
    */
   private void setFab(View v) {
      FloatingActionButton fab = v.findViewById(R.id.add_question_btn);
      if (new SQLiteUserManager(getContext()).getUserInfo() instanceof Student) {
         fab.setOnClickListener(v1 -> {
            Intent intent = new Intent(requireContext(), AskQuestion.class);
            requireContext().startActivity(intent);
         });
      }
      else
         fab.setVisibility(View.GONE);
   }

   @Override
   public void onStart() {
      super.onStart();
      adapter.startListening();
   }

   @Override
   public void onDestroy() {
      super.onDestroy();
      adapter.stopListening();
   }

   /**
    * Function to set the list of question.
    */
   private void setListOfQuestion () {

      User user = new SQLiteUserManager(getContext()).getUserInfo();

      Query query = QuestionHelper.getQuestions(user);

      FirestoreRecyclerOptions<ForumQuestion> options = new FirestoreRecyclerOptions.Builder<ForumQuestion>()
            .setQuery(query, ForumQuestion.class)
            .build();

      adapter = new MainAdapter(options);

      recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
      recyclerView.setHasFixedSize(true);
      recyclerView.setAdapter(adapter);

      adapter.setOnItemClickListener(position -> {
         String question_id = adapter.getSnapshots().getSnapshot(position).getId();
         Intent intent = new Intent(getContext(), QuestionDetail.class);
         intent.putExtra(QUESTION_ID, question_id);
         requireContext().startActivity(intent);
      });


   }

}
