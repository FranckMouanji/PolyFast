package com.example.polyfast.forumManager.bottomSheets;

import android.app.Dialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.polyfast.R;
import com.example.polyfast.forumManager.ForumAdapter;
import com.example.polyfast.forumManager.models.ResponseForum;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BottomSheetComments extends BottomSheetDialogFragment {

   private BottomSheetBehavior mBehavior;
   private List<ResponseForum> responses;
   private ForumAdapter adapter;
   private CommentPopup dialogComment;
   private ImageButton add_comment;
   private RecyclerView recyclerView;

   @NonNull
   @Override
   public Dialog onCreateDialog(Bundle savedInstanceState) {
      BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

      View v = View.inflate(getContext(), R.layout.bottomsheet_comment_response, null);

      ConstraintLayout layout = v.findViewById(R.id.root);
      LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) layout.getLayoutParams();
      params.height = getScreenHeight() - 100;
      layout.setLayoutParams(params);

      ImageButton close_btn = v.findViewById(R.id.close_btn);
       add_comment = v.findViewById(R.id.add_comment_btn);

      responses = new ArrayList<>();
      adapter = new ForumAdapter(responses);

      recyclerView = v.findViewById(R.id.recycler_comment);
      recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
      recyclerView.setHasFixedSize(true);
      recyclerView.setAdapter(adapter);

      setListOfComments();

      close_btn.setOnClickListener(v12 -> dismiss());

      dialog.setContentView(v);
      mBehavior = BottomSheetBehavior.from((View) v.getParent());
      return dialog;
   }

   /**
    * Function set the list of comment.
    */
   private void setListOfComments() {
      ResponseForum response = new ResponseForum();
      response.setId("comment_response");

      responses.add(response);
      responses.add(response);
      responses.add(response);
      responses.add(response);
      responses.add(response);
      responses.add(response);
      responses.add(response);
      responses.add(response);

      adapter.notifyDataSetChanged();
   }

   @Override
   public void onStart() {
      super.onStart();
      mBehavior.setPeekHeight(getScreenHeight() - 100);
      mBehavior.setSkipCollapsed(false);
      mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

      dialogComment = new CommentPopup(requireContext(), R.style.Theme_MaterialComponents_BottomSheetDialog);

      add_comment.setOnClickListener(v13 -> {

         dialogComment.show();

         // TODO managed the code to add the new comment.

         Toast.makeText(getContext(), "Add a new comment.", Toast.LENGTH_SHORT).show();
      });

      // Add new Comment.
      dialogComment.setOnSendCommentListener(comment -> {

         // TOdo managed the sending of the comment.

         ResponseForum responseForum = new ResponseForum();
         responseForum.setId("comment_response");
         responses.add(responseForum);
         adapter.notifyItemInserted(responses.size()-1);
         recyclerView.scrollToPosition(responses.size()-1);

         dialogComment.dismiss();

      });

   }

   private static int getScreenHeight() {
      return Resources.getSystem().getDisplayMetrics().heightPixels;
   }

}
