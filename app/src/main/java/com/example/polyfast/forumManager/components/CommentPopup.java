package com.example.polyfast.forumManager.components;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;

import com.example.polyfast.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class CommentPopup extends Dialog {

   private OnSendCommentListener listener;
   private TextInputEditText comment_view;
   private TextInputLayout outLineComment_view;

   public interface OnSendCommentListener {
      void onSendComment(String comment);
   }

   CommentPopup(@NonNull Context context, int themeResId) {
      super(context, themeResId);
   }

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.form_comment_layout);
      setCancelable(false);

      ImageButton close = findViewById(R.id.close_btn);
      Button send_comment = findViewById(R.id.send_comment);
      comment_view = findViewById(R.id.comment_text);
      outLineComment_view = findViewById(R.id.outlinedTextField);

      close.setOnClickListener(v -> dismiss());

      send_comment.setOnClickListener(v -> postComment());

      commentViewWatcher();

   }

   void setOnSendCommentListener(OnSendCommentListener mListener) {
      listener = mListener;
   }

   /**
    * Function to post the comment.
    */
   private void postComment () {
      if (commentIsValid()) {
         String comment = Objects.requireNonNull(comment_view.getText()).toString();
         comment_view.setText("");
         listener.onSendComment(comment);
      }
   }

   /**
    * Function to check if the comment is empty.
    */
   private boolean commentIsEmpty () {
      if (Objects.requireNonNull(comment_view.getText()).toString().trim().isEmpty()) {
         outLineComment_view.setError(getContext().getString(R.string.description_not_empty));
         return true;
      }
      return false;
   }

   /**
    * Function to checked if the length of comment is up the max length.
    */
   private boolean commentLengthUpMax ()  {
      return Objects.requireNonNull(comment_view.getText()).toString().trim().length() > 200;
   }

   /**
    * Function to get if the comment input text is valid.
    */
   private boolean commentIsValid () {
      return !commentIsEmpty() && !commentLengthUpMax();
   }

   /**
    * Function to watched the changed in label edit text.
    */
   private void commentViewWatcher() {
      comment_view.addTextChangedListener(new TextWatcher() {
         @Override
         public void beforeTextChanged(CharSequence s, int start, int count, int after) {

         }

         @Override
         public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() > 200)
               outLineComment_view.setError(getContext().getString(R.string.text_too_long));
            else
               outLineComment_view.setError(null);

         }

         @Override
         public void afterTextChanged(Editable s) {

         }
      });

   }

   @Override
   public void onBackPressed() {
      dismiss();
   }

   @Override
   public void onDetachedFromWindow() {
      super.onDetachedFromWindow();

      comment_view.clearFocus();
      comment_view.setText(null);
      outLineComment_view.clearFocus();
   }
}
