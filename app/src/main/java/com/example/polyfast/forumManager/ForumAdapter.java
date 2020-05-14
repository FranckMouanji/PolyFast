package com.example.polyfast.forumManager;

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
import com.example.polyfast.forumManager.database.SQLiteUserManager;
import com.example.polyfast.forumManager.database.StudentHelper;
import com.example.polyfast.forumManager.database.TeacherHelper;
import com.example.polyfast.forumManager.models.ForumComment;
import com.example.polyfast.forumManager.models.ForumModelsFactory;
import com.example.polyfast.forumManager.models.ForumQuestion;
import com.example.polyfast.forumManager.models.ForumResponse;
import com.example.polyfast.forumManager.models.Student;
import com.example.polyfast.forumManager.models.Teacher;
import com.example.polyfast.forumManager.utils.Utils;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

/**
 * Function to adapted the forum answer.
 * @author Ronald Tchuekou.
 */
public class ForumAdapter extends RecyclerView.Adapter {

   // Constants.
   private static final int ANSWER_QUESTION_ITEM = 0;
   private static final int NEXT_ELEMENT_ITEM = 1;
   private static final int QUESTION_ITEM = 2;
   private static final int RESPONSE_ITEM = 3;
   private static final int COMMENT_RESPONSE = 4;

   // Fields.
   private List<ForumModelsFactory> factories;
   private Context context;
   private int responsePosition, responseCount = 0;

   // Listener.
   private OnButtonsLastItemClickListener buttonsLastItemClickListener;
   private OnResponseItemButtonClickListener responseItemButtonClickListener;
   private OnNextElementButtonClickListener nextElementButtonClickListener;
   private OnQuestionItemClickListener questionItemClickListener;

   public ForumAdapter(List<ForumModelsFactory> factories) {
      this.factories = factories;
      this.responsePosition = 1;
   }

   // ************************* Interfaces to managed the click listener.
   public interface OnButtonsLastItemClickListener {
      void onResponseButtonClick();
      void onQuestionButtonClick();
   }
   public interface OnResponseItemButtonClickListener {
      void onCommentsButtonClick(int position);
      void onValidateButtonClick(int position);
      void onImageClick(int position, View view);
   }
   public interface OnNextElementButtonClickListener {
      void onNextElementButtonClick();
   }
   public interface OnQuestionItemClickListener {
      void onImage1Click(int position, View view);
      void onImage2Click(int position, View view);
   }

   /**
    * Function to listen the click on the response or question button.
    */
   void setOnButtonsLastItemClickListener(OnButtonsLastItemClickListener listener) {
      buttonsLastItemClickListener = listener;
   }

   /**
    * Function to listen the click on the comments button.
    */
   void setOnResponseItemButtonClickListener (OnResponseItemButtonClickListener listener) {
      responseItemButtonClickListener = listener;
   }

   /**
    * Function to listen the click on the question item.
    */
   void setOnQuestionItemClickListener (OnQuestionItemClickListener listener) {
      questionItemClickListener = listener;
   }

   /**
    * Function to listen the click on the next element button.
    */
   void setOnNextElementButtonClickListener (OnNextElementButtonClickListener listener) {
      nextElementButtonClickListener = listener;
   }

   @NonNull
   @Override
   public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      context = parent.getContext();
      if (viewType==RESPONSE_ITEM)
         return new ResponseItemHolder(
               LayoutInflater.from(parent.getContext()).inflate(R.layout.item_forum_response, parent, false),
               responseItemButtonClickListener);
      else if (viewType==NEXT_ELEMENT_ITEM)
         return new NextElementItemHolder(
           LayoutInflater.from(parent.getContext()).inflate(R.layout.item_forum_display_next_elements, parent, false),
               nextElementButtonClickListener);
      else if (viewType==QUESTION_ITEM)
         return new QuestionItemHolder(
               LayoutInflater.from(parent.getContext()).inflate(R.layout.item_forum_question, parent, false)
               , questionItemClickListener);
      else if (viewType==COMMENT_RESPONSE)
         return new CommentViewHolder(
               LayoutInflater.from(parent.getContext()).inflate(R.layout.item_response_comment, parent, false)
         );
      else
         return new ButtonsAnswerQuestionItemHolder(
               LayoutInflater.from(parent.getContext()).inflate(R.layout.item_forum_answer_or_question, parent, false)
               , buttonsLastItemClickListener);
   }

   @Override
   public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

      ForumModelsFactory factory = factories.get(position);

      if (holder instanceof CommentViewHolder) // Comment of the response.
         setCommentView(holder, factory);
      else if (holder instanceof QuestionItemHolder)  // Question.
         setQuestionView(holder, factory);
      else if (holder instanceof ResponseItemHolder)  // Response of the question.
         setResponseView(holder, factory);
      else if (holder instanceof ButtonsAnswerQuestionItemHolder)
         if (new SQLiteUserManager(context).getUserInfo() instanceof Teacher) {
            ((ButtonsAnswerQuestionItemHolder) holder).ask_question_btn.setVisibility(View.GONE);
            return;
         }
      if (factories.size() >= responseCount ) {
         if (holder instanceof NextElementItemHolder)
            ((NextElementItemHolder)holder).list_end_btn.setVisibility(View.GONE);
      }

   }

   /**
    * Function to set the response view.
    * @param holder View holder.
    * @param factory Factory.
    */
   private void setResponseView(RecyclerView.ViewHolder holder, ForumModelsFactory factory) {
      ResponseItemHolder responseHolder = (ResponseItemHolder) holder;
      ForumResponse response = (ForumResponse) factory;

      String status = response.getStatus();
      if (status.equals("student"))
         StudentHelper.getStudent(response.getAuthorId()).addOnSuccessListener(success-> {
            Student student = success.toObject(Student.class);
            assert student != null;
            String authorName = student.getName() +" "+ student.getSurname();
            responseHolder.user_name_answer.setText(authorName);
         });
      else
         TeacherHelper.getTeacher(response.getAuthorId()).addOnSuccessListener(success-> {
            Teacher teacher = success.toObject(Teacher.class);
            assert teacher != null;
            String authorName = teacher.getName() +" "+ teacher.getSurname();
            responseHolder.user_name_answer.setText(authorName);
         });

      String date_push = Utils.getFullDateFormat(response.getAnswerDate()) + " " + context.getString(R.string.at) + " " +
            Utils.getFullTimeFormat(response.getAnswerDate());
      responseHolder.answer_date.setText(date_push);

      String responsePaginate = context.getString(R.string.response) + responsePosition + "/" + responseCount;
      responseHolder.response_paginate.setText(responsePaginate);
      responsePosition ++;

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

   /**
    * Function to set the comment view.
    * @param holder View Holder.
    * @param factory Factory.
    */
   private void setCommentView(RecyclerView.ViewHolder holder, ForumModelsFactory factory) {
      CommentViewHolder commentHolder = (CommentViewHolder) holder;
      ForumComment comment = (ForumComment) factory;

      String status = comment.getStatus();
      if (status.equals("student"))
         StudentHelper.getStudent(comment.getAuthorId()).addOnSuccessListener(success-> {
            Student student = success.toObject(Student.class);
            assert student != null;
            String authorName = student.getName() +" "+ student.getSurname();
            commentHolder.comment_author.setText(authorName);
         });
      else
         TeacherHelper.getTeacher(comment.getAuthorId()).addOnSuccessListener(success-> {
            Teacher teacher = success.toObject(Teacher.class);
            assert teacher != null;
            String authorName = teacher.getName() +" "+ teacher.getSurname();
            commentHolder.comment_author.setText(authorName);
         });

      String date_comment = Utils.getFullDateFormat(comment.getDate()) + " " + context.getString(R.string.at) + " " +
            Utils.getFullTimeFormat(comment.getDate());
      commentHolder.comment_date.setText(date_comment);

      commentHolder.comment_text.setText(comment.getComment());

   }

   /**
    * Function to set the question view.
    * @param holder view holder.
    * @param factory factory.
    */
   private void setQuestionView(RecyclerView.ViewHolder holder, ForumModelsFactory factory) {
      QuestionItemHolder questionHolder = (QuestionItemHolder) holder;
      ForumQuestion question = (ForumQuestion) factory;

      responseCount = question.getResponseCount();

      String date_last;
      if (question.getLastAnswerDate() == null)
         date_last = context.getString(R.string.no_answer);
      else
         date_last = Utils.getFullDateFormat(question.getLastAnswerDate()) + " " + context.getString(R.string.at) + " " +
               Utils.getFullTimeFormat(question.getLastAnswerDate());

      String date_push = Utils.getFullDateFormat(question.getPushDate()) + " " + context.getString(R.string.at) + " " +
            Utils.getFullTimeFormat(question.getPushDate());

      questionHolder.question_label.setText(question.getLabel());
      questionHolder.last_author.setText(
            question.getLastAnswerAuthorId().equals("null") ? context.getString(R.string.no_answer) : question
                  .getLastAnswerAuthorId()
      );
      questionHolder.last_date.setText(date_last);

      StudentHelper.getStudent(question.getAuthorId()).addOnSuccessListener(success-> {
         Student student = success.toObject(Student.class);
         assert student != null;
         String authorName = student.getName() + student.getSurname();
         questionHolder.push_author.setText(authorName);
      });
      questionHolder.push_date.setText(date_push);
      questionHolder.question_description.setText(question.getDescription());
      if (question.getImage1().equals("null"))
         questionHolder.contentImage1.setVisibility(View.GONE);
      else{
         questionHolder.imageDescription1.setText(question.getImageDescription1());

         Glide.with(context)
               .load(Uri.parse(question.getImage1()))
               .override(Target.SIZE_ORIGINAL)
               .centerCrop()
               .error(context.getResources().getDrawable(R.drawable.side_nav_bar))
               .placeholder(context.getResources().getDrawable(R.drawable.side_nav_bar))
               .into(questionHolder.image1);
      }
      if (question.getImage2().equals("null"))
         questionHolder.contentImage2.setVisibility(View.GONE);
      else{
         questionHolder.imageDescription2.setText(question.getImageDescription2());
         Glide.with(context)
               .load(Uri.parse(question.getImage2()))
               .override(Target.SIZE_ORIGINAL)
               .centerCrop()
               .error(context.getResources().getDrawable(R.drawable.side_nav_bar))
               .placeholder(context.getResources().getDrawable(R.drawable.side_nav_bar))
               .into(questionHolder.image2);
      }
   }

   @Override
   public int getItemCount() {
      return factories.size();
   }

   @Override
   public int getItemViewType(int position) {
      ForumModelsFactory factory = factories.get(position);

      if (factory instanceof ForumQuestion)
         if (factory.getId().equals("next_element"))
            return NEXT_ELEMENT_ITEM;
         else if (factory.getId().equals("buttons"))
            return ANSWER_QUESTION_ITEM;
         else
         return QUESTION_ITEM;
      else if (factory instanceof ForumResponse)
         return RESPONSE_ITEM;
      else
         return COMMENT_RESPONSE;

   }

   /**
    * Class holder to answer and question buttons item.
    */
   public static class ButtonsAnswerQuestionItemHolder extends RecyclerView.ViewHolder {

      Button answer_btn, ask_question_btn;

      ButtonsAnswerQuestionItemHolder(@NonNull View itemView, OnButtonsLastItemClickListener mListener) {
         super(itemView);
         answer_btn = itemView.findViewById(R.id.answer_btn);
         ask_question_btn = itemView.findViewById(R.id.ask_question_btn);

         answer_btn.setOnClickListener(v -> mListener.onResponseButtonClick());
         ask_question_btn.setOnClickListener(v -> mListener.onQuestionButtonClick());
      }
   }

   /**
    * Class holder to view next elements.
    */
   public static class NextElementItemHolder extends RecyclerView.ViewHolder {

      Button list_end_btn;

      NextElementItemHolder(@NonNull View itemView, OnNextElementButtonClickListener mListener) {
         super(itemView);

         list_end_btn = itemView.findViewById(R.id.list_end_btn);

         list_end_btn.setOnClickListener(v -> mListener.onNextElementButtonClick());

      }
   }

   /**
    * Class holder to question item.
    */
   public static class QuestionItemHolder extends RecyclerView.ViewHolder {

      MaterialCardView contentImage1, contentImage2;
      ImageView image1, image2;
      TextView question_label, last_author, last_date, push_author,
         push_date, question_description, imageDescription1, imageDescription2;

      QuestionItemHolder(@NonNull View itemView, OnQuestionItemClickListener mListener) {
         super(itemView);
         contentImage1 = itemView.findViewById(R.id.contentImage1);
         contentImage2 = itemView.findViewById(R.id.contentImage2);
         image1 = itemView.findViewById(R.id.image1);
         image2 = itemView.findViewById(R.id.image2);
         question_label = itemView.findViewById(R.id.question_label);
         last_author = itemView.findViewById(R.id.last_author);
         last_date = itemView.findViewById(R.id.last_date);
         push_author = itemView.findViewById(R.id.push_author);
         push_date = itemView.findViewById(R.id.push_date);
         question_description = itemView.findViewById(R.id.question_description);
         imageDescription1 = itemView.findViewById(R.id.imageDescription1);
         imageDescription2 = itemView.findViewById(R.id.imageDescription2);

         image1.setOnClickListener(v->{
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION)
               mListener.onImage1Click(position, v);
         });

         image2.setOnClickListener(v->{
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION)
               mListener.onImage2Click(position, v);
         });
      }
   }

   /**
    * Class holder to response item.
    */
   public static class ResponseItemHolder extends RecyclerView.ViewHolder {

      ImageView imageDescription;
      TextView user_name_answer, answer_date, response_paginate,
         validate_number, user_response, comment_count;
      Button commented_btn, validate_btn;

      ResponseItemHolder(@NonNull View itemView, OnResponseItemButtonClickListener mListener) {
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

   /**
    * Class holder to comments of the responses.
    */
   public static class CommentViewHolder extends RecyclerView.ViewHolder {

      TextView comment_author, comment_date, comment_text;

      CommentViewHolder(@NonNull View itemView) {
         super(itemView);
         comment_author = itemView.findViewById(R.id.comment_author);
         comment_date = itemView.findViewById(R.id.comment_date);
         comment_text = itemView.findViewById(R.id.comment_text);
      }
   }

   /**
    * Function to increment the response count.
    */
   void incrementResponseCount() {
      this.responseCount ++;
   }

}
