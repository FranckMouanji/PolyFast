package com.example.polyfast.Activities.ui;

import android.content.Context;
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
import com.example.polyfast.forumManager.AskQuestion;
import com.example.polyfast.forumManager.MainAdapter;
import com.example.polyfast.forumManager.QuestionDetail;
import com.example.polyfast.forumManager.models.ForumQuestion;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class Forum extends Fragment {

    public static final String QUESTION_ID = "Question_id";
    private MainAdapter adapter;
    private List<ForumQuestion> forumQuestions;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.forum_fragment, container, false);

        RecyclerView recyclerView = v.findViewById(R.id.question_recycler);
        FloatingActionButton fab = v.findViewById(R.id.add_question_btn);

        fab.setOnClickListener(v1 -> {
            Intent intent = new Intent(requireContext(), AskQuestion.class);
            requireContext().startActivity(intent);
        });

        forumQuestions = new ArrayList<>();

        adapter = new MainAdapter(forumQuestions);
        
        adapter.setOnItemClickListener(position -> {
            Intent intent = new Intent(getContext(), QuestionDetail.class);
            intent.putExtra(QUESTION_ID, forumQuestions.get(position).getId());
            requireContext().startActivity(intent);
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        setListOfQuestion();

        return v;
    }

    /**
     * Function to set the list of question.
     */
    private void setListOfQuestion () {

        forumQuestions.add(new ForumQuestion("id", "Loading images with Glide is easy " +
              "and in many cases requires only a single line:",
              "Although it’s good practice to clear loads you no longer" +
                    " need, you’re not required to do so. In fact, Glide " +
                    "will automatically clear the load and recycle any resources used by the load " +
                    "when the Activity or Fragment you pass in to Glide.with() is destroyed.",
              "Mathematiques", "3emAll", Calendar.getInstance().getTime(),
              "none", "none", "non", "none",
              30, "none"));

        forumQuestions.add(new ForumQuestion("id", "Loading images with Glide is easy " +
              "and in many cases requires only a single line:",
              "Although it’s good practice to clear loads you no longer" +
                    " need, you’re not required to do so. In fact, Glide " +
                    "will automatically clear the load and recycle any resources used by the load " +
                    "when the Activity or Fragment you pass in to Glide.with() is destroyed.",
              "Education Civique", "3emAll", Calendar.getInstance().getTime(),
              "none", "none", "non", "none",
              30, "none"));

        forumQuestions.add(new ForumQuestion("id", "Loading images with Glide is easy " +
              "and in many cases requires only a single line:",
              "Although it’s good practice to clear loads you no longer" +
                    " need, you’re not required to do so. In fact, Glide " +
                    "will automatically clear the load and recycle any resources used by the load " +
                    "when the Activity or Fragment you pass in to Glide.with() is destroyed.",
              "Anglais", "3emAll", Calendar.getInstance().getTime(),
              "none", "none", "non", "none",
              30, "none"));

        forumQuestions.add(new ForumQuestion("id", "Loading images with Glide is easy " +
              "and in many cases requires only a single line:",
              "Although it’s good practice to clear loads you no longer" +
                    " need, you’re not required to do so. In fact, Glide " +
                    "will automatically clear the load and recycle any resources used by the load " +
                    "when the Activity or Fragment you pass in to Glide.with() is destroyed.",
              "Français", "3emAll", Calendar.getInstance().getTime(),
              "none", "none", "non", "none",
              30, "none"));

        forumQuestions.add(new ForumQuestion("id", "Loading images with Glide is easy " +
              "and in many cases requires only a single line:",
              "Although it’s good practice to clear loads you no longer" +
                    " need, you’re not required to do so. In fact, Glide " +
                    "will automatically clear the load and recycle any resources used by the load " +
                    "when the Activity or Fragment you pass in to Glide.with() is destroyed.",
              "Histoire", "3emAll", Calendar.getInstance().getTime(),
              "none", "none", "non", "none",
              30, "none"));

        forumQuestions.add(new ForumQuestion("id", "Loading images with Glide is easy " +
              "and in many cases requires only a single line:",
              "Although it’s good practice to clear loads you no longer" +
                    " need, you’re not required to do so. In fact, Glide " +
                    "will automatically clear the load and recycle any resources used by the load " +
                    "when the Activity or Fragment you pass in to Glide.with() is destroyed.",
              "Physique", "3emAll", Calendar.getInstance().getTime(),
              "none", "none", "non", "none",
              30, "none"));

        forumQuestions.add(new ForumQuestion("id", "Loading images with Glide is easy " +
              "and in many cases requires only a single line:",
              "Although it’s good practice to clear loads you no longer" +
                    " need, you’re not required to do so. In fact, Glide " +
                    "will automatically clear the load and recycle any resources used by the load " +
                    "when the Activity or Fragment you pass in to Glide.with() is destroyed.",
              "Physique", "3emAll", Calendar.getInstance().getTime(),
              "none", "none", "non", "none",
              30, "none"));

        forumQuestions.add(new ForumQuestion("id", "Loading images with Glide is easy " +
              "and in many cases requires only a single line:",
              "Although it’s good practice to clear loads you no longer" +
                    " need, you’re not required to do so. In fact, Glide " +
                    "will automatically clear the load and recycle any resources used by the load " +
                    "when the Activity or Fragment you pass in to Glide.with() is destroyed.",
              "Physique", "3emAll", Calendar.getInstance().getTime(),
              "none", "none", "non", "none",
              30, "none"));

        forumQuestions.add(new ForumQuestion("id", "Loading images with Glide is easy " +
              "and in many cases requires only a single line:",
              "Although it’s good practice to clear loads you no longer" +
                    " need, you’re not required to do so. In fact, Glide " +
                    "will automatically clear the load and recycle any resources used by the load " +
                    "when the Activity or Fragment you pass in to Glide.with() is destroyed.",
              "Physique", "3emAll", Calendar.getInstance().getTime(),
              "none", "none", "non", "none",
              30, "none"));

        forumQuestions.add(new ForumQuestion("id", "Loading images with Glide is easy " +
              "and in many cases requires only a single line:",
              "Although it’s good practice to clear loads you no longer" +
                    " need, you’re not required to do so. In fact, Glide " +
                    "will automatically clear the load and recycle any resources used by the load " +
                    "when the Activity or Fragment you pass in to Glide.with() is destroyed.",
              "Physique", "3emAll", Calendar.getInstance().getTime(),
              "none", "none", "non", "none",
              30, "none"));

        forumQuestions.add(new ForumQuestion("id", "Loading images with Glide is easy " +
              "and in many cases requires only a single line:",
              "Although it’s good practice to clear loads you no longer" +
                    " need, you’re not required to do so. In fact, Glide " +
                    "will automatically clear the load and recycle any resources used by the load " +
                    "when the Activity or Fragment you pass in to Glide.with() is destroyed.",
              "Physique", "3emAll", Calendar.getInstance().getTime(),
              "none", "none", "non", "none",
              30, "none"));

        forumQuestions.add(new ForumQuestion("id", "Loading images with Glide is easy " +
              "and in many cases requires only a single line:",
              "Although it’s good practice to clear loads you no longer" +
                    " need, you’re not required to do so. In fact, Glide " +
                    "will automatically clear the load and recycle any resources used by the load " +
                    "when the Activity or Fragment you pass in to Glide.with() is destroyed.",
              "Physique", "3emAll", Calendar.getInstance().getTime(),
              "none", "none", "non", "none",
              30, "none"));

        forumQuestions.add(new ForumQuestion("id", "Loading images with Glide is easy " +
              "and in many cases requires only a single line:",
              "Although it’s good practice to clear loads you no longer" +
                    " need, you’re not required to do so. In fact, Glide " +
                    "will automatically clear the load and recycle any resources used by the load " +
                    "when the Activity or Fragment you pass in to Glide.with() is destroyed.",
              "Physique", "3emAll", Calendar.getInstance().getTime(),
              "none", "none", "non", "none",
              30, "none"));

        forumQuestions.add(new ForumQuestion("id", "Loading images with Glide is easy " +
              "and in many cases requires only a single line:",
              "Although it’s good practice to clear loads you no longer" +
                    " need, you’re not required to do so. In fact, Glide " +
                    "will automatically clear the load and recycle any resources used by the load " +
                    "when the Activity or Fragment you pass in to Glide.with() is destroyed.",
              "Physique", "3emAll", Calendar.getInstance().getTime(),
              "none", "none", "non", "none",
              30, "none"));

        forumQuestions.add(new ForumQuestion("id", "Loading images with Glide is easy " +
              "and in many cases requires only a single line:",
              "Although it’s good practice to clear loads you no longer" +
                    " need, you’re not required to do so. In fact, Glide " +
                    "will automatically clear the load and recycle any resources used by the load " +
                    "when the Activity or Fragment you pass in to Glide.with() is destroyed.",
              "Physique", "3emAll", Calendar.getInstance().getTime(),
              "none", "none", "non", "none",
              30, "none"));

        forumQuestions.add(new ForumQuestion("id", "Loading images with Glide is easy " +
              "and in many cases requires only a single line:",
              "Although it’s good practice to clear loads you no longer" +
                    " need, you’re not required to do so. In fact, Glide " +
                    "will automatically clear the load and recycle any resources used by the load " +
                    "when the Activity or Fragment you pass in to Glide.with() is destroyed.",
              "Physique", "3emAll", Calendar.getInstance().getTime(),
              "none", "none", "non", "none",
              30, "none"));

        forumQuestions.add(new ForumQuestion("id", "Loading images with Glide is easy " +
              "and in many cases requires only a single line:",
              "Although it’s good practice to clear loads you no longer" +
                    " need, you’re not required to do so. In fact, Glide " +
                    "will automatically clear the load and recycle any resources used by the load " +
                    "when the Activity or Fragment you pass in to Glide.with() is destroyed.",
              "Physique", "3emAll", Calendar.getInstance().getTime(),
              "none", "none", "non", "none",
              30, "none"));

        forumQuestions.add(new ForumQuestion("id", "Loading images with Glide is easy " +
              "and in many cases requires only a single line:",
              "Although it’s good practice to clear loads you no longer" +
                    " need, you’re not required to do so. In fact, Glide " +
                    "will automatically clear the load and recycle any resources used by the load " +
                    "when the Activity or Fragment you pass in to Glide.with() is destroyed.",
              "Physique", "3emAll", Calendar.getInstance().getTime(),
              "none", "none", "non", "none",
              30, "none"));

        forumQuestions.add(new ForumQuestion("id", "Loading images with Glide is easy " +
              "and in many cases requires only a single line:",
              "Although it’s good practice to clear loads you no longer" +
                    " need, you’re not required to do so. In fact, Glide " +
                    "will automatically clear the load and recycle any resources used by the load " +
                    "when the Activity or Fragment you pass in to Glide.with() is destroyed.",
              "Physique", "3emAll", Calendar.getInstance().getTime(),
              "none", "none", "non", "none",
              30, "none"));

        adapter.notifyDataSetChanged();
    }

}
