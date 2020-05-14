package com.example.polyfast.fragment;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import com.example.polyfast.forumManager.AskQuestion;
import com.example.polyfast.forumManager.MainAdapter;
import com.example.polyfast.forumManager.QuestionDetail;
import com.example.polyfast.forumManager.database.QuestionHelper;
import com.example.polyfast.forumManager.database.SQLiteUserManager;
import com.example.polyfast.forumManager.models.ForumQuestion;
import com.example.polyfast.forumManager.models.Student;
import com.example.polyfast.forumManager.models.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.polyfast.forumManager.QuestionDetail.ANSWER_COUNT_ACTION;

public class Forum extends Fragment {

    public static final String QUESTION_ID = "Question_id";
   public static final String POSITION = "Position";
   private MainAdapter adapter;
    private List<ForumQuestion> questions;
    private BroadcastReceiver receiver;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.forum_fragment, container, false);

        RecyclerView recyclerView = v.findViewById(R.id.question_recycler);

        setFab(v);

        questions = new ArrayList<>();

        adapter = new MainAdapter(questions);
        
        adapter.setOnItemClickListener(position -> {
            Intent intent = new Intent(getContext(), QuestionDetail.class);
            intent.putExtra(QUESTION_ID, questions.get(position).getId());
            intent.putExtra(POSITION, position);
            requireContext().startActivity(intent);
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

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

   /**
     * Function to set the list of question.
     */
    private void setListOfQuestion () {

        ProgressDialog progressDialog = ProgressDialog.show(getContext(), null,
              requireContext().getString(R.string.loading));

        User user = new SQLiteUserManager(getContext()).getUserInfo();

        QuestionHelper.getQuestions(user).addOnCompleteListener(complete -> {
           if (complete.isSuccessful()){
                for (DocumentSnapshot document : Objects.requireNonNull(complete.getResult()).getDocuments()) {
                    ForumQuestion question = document.toObject(ForumQuestion.class);

                    assert question != null;
                    question.setId(document.getId());

                    questions.add(question);

                }
                adapter.notifyDataSetChanged();
           }
           else
               Objects.requireNonNull(complete.getException()).printStackTrace();

           progressDialog.dismiss();
        });

    }

   @Override
   public void onResume() {
      super.onResume();

      receiver = new BroadcastReceiver() {
         @Override
         public void onReceive(Context context, Intent intent) {
            int mPosition = intent.getIntExtra(POSITION, 0);
            int responseCount = intent.getIntExtra("response_count", 0);

            ForumQuestion question = questions.get(mPosition);
            question.setResponseCount(responseCount);

            questions.remove(mPosition);
            questions.add(mPosition, question);

            adapter.notifyItemChanged(mPosition);
         }
      };
      requireContext().registerReceiver(receiver, new IntentFilter(ANSWER_COUNT_ACTION));
   }

   @Override
   public void onDestroy() {
      super.onDestroy();

      if (receiver != null)
         requireContext().unregisterReceiver(receiver);
   }

}
