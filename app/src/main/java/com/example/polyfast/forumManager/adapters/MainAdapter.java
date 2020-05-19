package com.example.polyfast.forumManager.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.polyfast.R;
import com.example.polyfast.forumManager.database.StudentHelper;
import com.example.polyfast.forumManager.models.Student;
import com.example.polyfast.forumManager.utils.Utils;
import com.example.polyfast.forumManager.models.ForumQuestion;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class MainAdapter extends FirestoreRecyclerAdapter<ForumQuestion, MainAdapter.QuestionHolder> {

   private Context context;
   private OnItemClickListener mListener;

   public interface OnItemClickListener {
      void onItemClick(int position);
   }

   public void setOnItemClickListener (OnItemClickListener listener) {
      mListener = listener;
   }

   public MainAdapter(@NonNull FirestoreRecyclerOptions<ForumQuestion> options) {
      super(options);
   }

   @NonNull
   @Override
   public QuestionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      context = parent.getContext();
      return new QuestionHolder(LayoutInflater.from(context).inflate(R.layout.item_question,
            parent, false), mListener);
   }

   @Override
   protected void onBindViewHolder(@NonNull QuestionHolder holder, int i, @NonNull ForumQuestion forumQuestion) {

      holder.avatar.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_account));
      holder.label.setText(forumQuestion.getLabel());
      holder.description.setText(forumQuestion.getDescription());
      holder.class_name.setText(forumQuestion.getClassName());
      String responseNum = "" + forumQuestion.getResponseCount();
      holder.response_number.setText(responseNum);
      holder.material.setText(forumQuestion.getMaterial());

      StudentHelper.getStudent(forumQuestion.getAuthorId()).addOnSuccessListener(success-> {
         Student student = success.toObject(Student.class);
         assert student != null;
         String authorName = student.getName() + student.getSurname();
         holder.user_name.setText(authorName);
      });

      String dateFormat = Utils.getFullDateFormat(forumQuestion.getPushDate()) + " " +
            context.getResources().getString(R.string.at) + " " +
            Utils.getFullTimeFormat(forumQuestion.getPushDate());

      holder.date_push.setText(dateFormat);
   }

   static class QuestionHolder extends RecyclerView.ViewHolder {

      ImageView avatar;
      TextView user_name, label, description, class_name,
            response_number, date_push, material;

      QuestionHolder(@NonNull View itemView, OnItemClickListener listener) {
         super(itemView);

         user_name = itemView.findViewById(R.id.user_name);
         avatar = itemView.findViewById(R.id.avatar);
         label = itemView.findViewById(R.id.question_label);
         description = itemView.findViewById(R.id.question_description);
         class_name = itemView.findViewById(R.id.class_name);
         response_number = itemView.findViewById(R.id.response_number);
         date_push = itemView.findViewById(R.id.push_date);
         material = itemView.findViewById(R.id.material);

         itemView.setOnClickListener(v -> {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION)
               listener.onItemClick(position);
         });
      }
   }
}
