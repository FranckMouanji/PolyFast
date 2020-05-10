package com.example.polyfast.forumManager.bottomSheets;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;

import com.example.polyfast.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class CommentPopup extends Dialog {

   private OnSendCommentListener listener;

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
      TextInputEditText comment_view = findViewById(R.id.comment_text);

      close.setOnClickListener(v -> dismiss());

      send_comment.setOnClickListener(v -> {

         String comment = Objects.requireNonNull(comment_view.getText()).toString();
         comment_view.setText("");

         listener.onSendComment(comment);

      });

   }

   void setOnSendCommentListener(OnSendCommentListener mListener) {
      listener = mListener;
   }


}
