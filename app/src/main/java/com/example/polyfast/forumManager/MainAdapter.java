package com.example.polyfast.forumManager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.polyfast.R;
import com.example.polyfast.forumManager.comomn.Utils;
import com.example.polyfast.forumManager.models.ForumQuestion;

import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

   private List<ForumQuestion> forumQuestions;
   private Context context;
   private OnItemClickListener mListener;

   public MainAdapter(List<ForumQuestion> forumQuestions) {
      this.forumQuestions = forumQuestions;
   }

   public interface OnItemClickListener {
      void onItemClick(int position);
   }

   public void setOnItemClickListener (OnItemClickListener listener) {
      mListener = listener;
   }

   @NonNull
   @Override
   public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      context = parent.getContext();
      return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_question,
            parent, false), mListener);
   }

   @Override
   public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
      ForumQuestion forumQuestion = forumQuestions.get(position);

      holder.avatar.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_account));
      holder.label.setText(forumQuestion.getLabel());
      holder.description.setText(forumQuestion.getDescription());
      holder.class_name.setText(forumQuestion.getClassName());
      String responseNum = "" + forumQuestion.getResponseNum();
      holder.response_number.setText(responseNum);
      holder.material.setText(forumQuestion.getMaterial());
      holder.user_name.setText("Ronald Tchuekou"); // TODO Set the user name.

      String dateFormat = Utils.getFullDateFormat(forumQuestion.getPushDate()) + " " +
            context.getResources().getString(R.string.at) + " " +
            Utils.getFullTimeFormat(forumQuestion.getPushDate());

      holder.date_push.setText(dateFormat);

   }

   @Override
   public int getItemCount() {
      return forumQuestions.size();
   }

   static class ViewHolder extends RecyclerView.ViewHolder {

      ImageView avatar;
      TextView user_name, label, description, class_name,
            response_number, date_push, material;

      public ViewHolder(@NonNull View itemView, OnItemClickListener listener) {
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
