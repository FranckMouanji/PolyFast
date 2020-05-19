package com.example.polyfast.forumManager.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.polyfast.R;
import com.example.polyfast.forumManager.database.StudentHelper;
import com.example.polyfast.forumManager.database.TeacherHelper;
import com.example.polyfast.forumManager.models.ForumComment;
import com.example.polyfast.forumManager.models.Student;
import com.example.polyfast.forumManager.models.Teacher;
import com.example.polyfast.forumManager.utils.Utils;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class CommentAdapter extends FirestoreRecyclerAdapter<ForumComment, CommentAdapter.CommentHolder> {

   private Context context;

   public CommentAdapter(@NonNull FirestoreRecyclerOptions<ForumComment> options) {
      super(options);
   }

   @Override
   protected void onBindViewHolder(@NonNull CommentHolder commentHolder, int position,
                                   @NonNull ForumComment comment) {

      String status = comment.getStatus();
      if (status.equals("student"))
         StudentHelper.getStudent(comment.getAuthorId()).addOnSuccessListener(success-> {
            Student student = success.toObject(Student.class);
            assert student != null;
            String authorName = context.getString(R.string.student) + student.getName() +" "+ student.getSurname();
            commentHolder.comment_author.setText(authorName);
         });
      else
         TeacherHelper.getTeacher(comment.getAuthorId()).addOnSuccessListener(success-> {
            Teacher teacher = success.toObject(Teacher.class);
            assert teacher != null;
            String authorName = context.getString(R.string.M) + teacher.getName() +" "+ teacher.getSurname();
            commentHolder.comment_author.setText(authorName);
         });

      String date_comment = Utils.getFullDateFormat(comment.getDate())
            + " " + context.getString(R.string.at) + " " +
            Utils.getFullTimeFormat(comment.getDate());

      commentHolder.comment_date.setText(date_comment);

      commentHolder.comment_text.setText(comment.getComment());

   }

   @NonNull
   @Override
   public CommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      context = parent.getContext();
      return new CommentHolder(LayoutInflater.from(parent.getContext()).inflate(
            R.layout.item_response_comment, parent, false
      ));
   }

   /**
    * Class holder to comments of the responses.
    */
   static class CommentHolder extends RecyclerView.ViewHolder {

      TextView comment_author, comment_date, comment_text;

      CommentHolder(@NonNull View itemView) {
         super(itemView);
         comment_author = itemView.findViewById(R.id.comment_author);
         comment_date = itemView.findViewById(R.id.comment_date);
         comment_text = itemView.findViewById(R.id.comment_text);
      }
   }


}
