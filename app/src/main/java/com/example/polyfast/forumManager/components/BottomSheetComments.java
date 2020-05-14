package com.example.polyfast.forumManager.components;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.example.polyfast.forumManager.database.CommentHelper;
import com.example.polyfast.forumManager.database.ResponseHelper;
import com.example.polyfast.forumManager.database.SQLiteUserManager;
import com.example.polyfast.forumManager.models.ForumComment;
import com.example.polyfast.forumManager.models.ForumModelsFactory;
import com.example.polyfast.forumManager.models.Student;
import com.example.polyfast.forumManager.models.User;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;


public class BottomSheetComments extends BottomSheetDialogFragment {

   public static final String COMMENTED_ACTION = "comment_count";
   private BottomSheetBehavior mBehavior;
   private List<ForumModelsFactory> factories;
   private ForumAdapter adapter;
   private CommentPopup dialogComment;
   private ImageButton add_comment;
   private RecyclerView recyclerView;
   private String responseId;

   @NonNull
   @Override
   public Dialog onCreateDialog(Bundle savedInstanceState) {
      BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

      View v = View.inflate(getContext(), R.layout.bottomsheet_comment_response, null);

      assert getArguments() != null;
      responseId = getArguments().getString("response_id");

      ConstraintLayout layout = v.findViewById(R.id.root);
      LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) layout.getLayoutParams();
      params.height = getScreenHeight() - 100;
      layout.setLayoutParams(params);

      ImageButton close_btn = v.findViewById(R.id.close_btn);
       add_comment = v.findViewById(R.id.add_comment_btn);

      factories = new ArrayList<>();
      adapter = new ForumAdapter(factories);

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

      ProgressDialog progressDialog = ProgressDialog.show(getContext(), null, requireContext()
            .getString(R.string.loading));

      CommentHelper.getComments(responseId).addOnCompleteListener(complete->{
         if (complete.isSuccessful()){
            List<DocumentSnapshot> documents = Objects.requireNonNull(complete.getResult()).getDocuments();
            if (documents.size() == 0){
               Toast.makeText(getContext(), requireContext().getString(R.string.no_comment),
                     Toast.LENGTH_SHORT).show();
            }
            for (DocumentSnapshot document : documents){
               ForumComment comment = document.toObject(ForumComment.class);
               assert comment != null;
               comment.setId(document.getId());
               factories.add(comment);
               adapter.notifyItemInserted(factories.size()-1);
            }
         }
         progressDialog.dismiss();
      });

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
         Toast.makeText(getContext(), "Add a new comment.", Toast.LENGTH_SHORT).show();
      });

      // Add new Comment.
      dialogComment.setOnSendCommentListener(comment -> {
         User user = new SQLiteUserManager(getContext()).getUserInfo();

         ForumComment forumComment = new ForumComment(null, user.getId(), comment, Calendar
               .getInstance().getTime(), responseId, user instanceof Student ? "student" : "teacher");
         ProgressDialog progressDialog = ProgressDialog.show(getContext(), null,
               requireContext().getString(R.string.loading_send));
         CommentHelper.addComment(forumComment).addOnCompleteListener(complete->{
            if (complete.isSuccessful()){
               ResponseHelper.updateCommentCount(responseId); // Update the comment count of this response.
               factories.add(forumComment);
               adapter.notifyItemInserted(factories.size()-1);
               recyclerView.scrollToPosition(factories.size()-1);
            }
            progressDialog.dismiss();
         });

         dialogComment.dismiss();

      });

   }

   private static int getScreenHeight() {
      return Resources.getSystem().getDisplayMetrics().heightPixels;
   }

   @Override
   public void onDismiss(@NonNull DialogInterface dialog) {
      super.onDismiss(dialog);
      Intent intentBroadcast = new Intent();
      intentBroadcast.putExtra("comment_count", factories.size());
      intentBroadcast.setAction(COMMENTED_ACTION);
      requireContext().sendBroadcast(intentBroadcast);
   }
}
