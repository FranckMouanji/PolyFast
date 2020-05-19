package com.example.polyfast.forumManager.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.example.polyfast.R;
import com.example.polyfast.forumManager.database.StudentHelper;
import com.example.polyfast.forumManager.database.TeacherHelper;
import com.example.polyfast.forumManager.models.ForumResponse;
import com.example.polyfast.forumManager.models.Student;
import com.example.polyfast.forumManager.models.Teacher;
import com.example.polyfast.forumManager.utils.Utils;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

/**
 * Function to adapted the forum answer.
 * @author Ronald Tchuekou.
 */
public class ResponseAdapter extends FirestoreRecyclerAdapter<ForumResponse, ResponseAdapter.ResponseHolder> {

   private Context context;

   // Listener.
   private OnResponseItemButtonClickListener responseItemButtonClickListener;

   public ResponseAdapter(@NonNull FirestoreRecyclerOptions<ForumResponse> options) {
      super(options);
   }

   @NonNull
   @Override
   public ResponseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      context = parent.getContext();
      return new ResponseHolder(
            LayoutInflater.from(parent.getContext()).inflate(R.layout.item_forum_response, parent, false),
            responseItemButtonClickListener);
   }

   @Override
   protected void onBindViewHolder(@NonNull ResponseHolder responseHolder, int position, @NonNull
         ForumResponse response) {

      String status = response.getStatus();
      if (status.equals("student"))
         StudentHelper.getStudent(response.getAuthorId()).addOnSuccessListener(success-> {
            Student student = success.toObject(Student.class);
            assert student != null;
            String authorName = context.getString(R.string.student) + student.getName() +" "+ student.getSurname();
            responseHolder.user_name_answer.setText(authorName);
         });
      else
         TeacherHelper.getTeacher(response.getAuthorId()).addOnSuccessListener(success-> {
            Teacher teacher = success.toObject(Teacher.class);
            assert teacher != null;
            String authorName = context.getString(R.string.M)+teacher.getName() +" "+ teacher.getSurname();
            responseHolder.user_name_answer.setText(authorName);
         });

      String date_push = Utils.getFullDateFormat(response.getAnswerDate()) + " " + context.getString(R.string.at) + " " +
            Utils.getFullTimeFormat(response.getAnswerDate());
      responseHolder.answer_date.setText(date_push);

      String responsePaginate = context.getString(R.string.response) ;
      responseHolder.response_paginate.setText(responsePaginate);

      responseHolder.user_response.setText(response.getResponse());
      if (response.getImage1().equals("null"))
         responseHolder.imageDescription.setVisibility(View.GONE);
      else{
         Glide.with(context)
               .load(Uri.parse(response.getImage1()))
               .override(Target.SIZE_ORIGINAL)
               .centerCrop()
               .error(context.getResources().getDrawable(R.drawable.side_nav_bar))
               .placeholder(context.getResources().getDrawable(R.drawable.side_nav_bar))
               .into(responseHolder.imageDescription);
      }

      String commentCount = response.getCommentCount() + " " + context.getString(R.string.person_comment_this);
      responseHolder.comment_count.setText(commentCount);
      responseHolder.validate_number.setText(String.valueOf(response.getValidateCount()));

   }

   // ************************* Interfaces to managed the click listener.
   public interface OnResponseItemButtonClickListener {
      void onCommentsButtonClick(int position);
      void onValidateButtonClick(int position);
      void onImageClick(int position, View view);
   }

   /**
    * Function to listen the click on the comments button.
    */
   public void setOnResponseItemButtonClickListener(OnResponseItemButtonClickListener listener) {
      responseItemButtonClickListener = listener;
   }

   /**
    * Class holder to response item.
    */
   static class ResponseHolder extends RecyclerView.ViewHolder {

      ImageView imageDescription;
      TextView user_name_answer, answer_date, response_paginate,
            validate_number, user_response, comment_count;
      Button commented_btn, validate_btn;

      ResponseHolder(@NonNull View itemView, OnResponseItemButtonClickListener mListener) {
         super(itemView);

         imageDescription = itemView.findViewById(R.id.imageDescription);
         user_name_answer = itemView.findViewById(R.id.comment_author);
         answer_date = itemView.findViewById(R.id.comment_date);
         response_paginate = itemView.findViewById(R.id.response_paginate);
         validate_number = itemView.findViewById(R.id.like_count);
         user_response = itemView.findViewById(R.id.comment_text);
         comment_count = itemView.findViewById(R.id.comment_count);
         commented_btn = itemView.findViewById(R.id.commented_btn);
         validate_btn = itemView.findViewById(R.id.send_comment);

         imageDescription.setOnClickListener(v->{
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION)
               mListener.onImageClick(position, v);
         });

         commented_btn.setOnClickListener(v -> {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION)
               mListener.onCommentsButtonClick(position);
         });

         validate_btn.setOnClickListener(v -> {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION)
               mListener.onValidateButtonClick(position);
         });
      }
   }


}
