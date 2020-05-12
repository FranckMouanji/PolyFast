package com.example.polyfast.forumManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.polyfast.R;
import com.example.polyfast.forumManager.models.ForumModelsFactory;
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
   private List<ForumModelsFactory> forumResponses;

   // Listener.
   private OnButtonsLastItemClickListener buttonsLastItemClickListener;
   private OnResponseItemButtonClickListener responseItemButtonClickListener;
   private OnNextElementButtonClickListener nextElementButtonClickListener;

   public ForumAdapter(List<ForumModelsFactory> forumResponses) {
      this.forumResponses = forumResponses;
   }

   // ************************* Interfaces to managed the click listener.
   public interface OnButtonsLastItemClickListener {
      void onResponseButtonClick();
      void onQuestionButtonClick();
   }
   public interface OnResponseItemButtonClickListener {
      void onCommentsButtonClick(int position);
      void onValidateButtonClick(int position);
   }
   public interface OnNextElementButtonClickListener {
      void onNextElementButtonClick();
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
    * Function to listen the click on the next element button.
    */
   void setOnNextElementButtonClickListener (OnNextElementButtonClickListener listener) {
      nextElementButtonClickListener = listener;
   }

   @NonNull
   @Override
   public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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
         );
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
      // TODO managed this content.
   }

   @Override
   public int getItemCount() {
      return forumResponses.size();
   }

   @Override
   public int getItemViewType(int position) {
      ForumModelsFactory response = forumResponses.get(position);
      switch (response.getId()) {
         case "question":
            return QUESTION_ITEM;
         case "next_element":
            return NEXT_ELEMENT_ITEM;
         case "response":
            return RESPONSE_ITEM;
         case "comment_response":
            return COMMENT_RESPONSE;
         default:
            return ANSWER_QUESTION_ITEM;
      }
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

      QuestionItemHolder(@NonNull View itemView) {
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

}
