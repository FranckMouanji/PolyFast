package com.example.polyfast.forumManager.bottomSheets;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.polyfast.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class BottomSheetResponse extends BottomSheetDialogFragment {

   private BottomSheetListener mListener;
   private BottomSheetBehavior mBehavior;
    private TextInputLayout outlinedTextField;
   private TextInputEditText response_text;
   private ImageView imageView;
   private Bitmap image;

   @NonNull
   @Override
   public Dialog onCreateDialog(Bundle savedInstanceState) {
      BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

      View v = View.inflate(getContext(), R.layout.bottomsheet_response, null);

      ConstraintLayout layout = v.findViewById(R.id.root);
      LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) layout.getLayoutParams();
      params.height = getScreenHeight() - 100;
      layout.setLayoutParams(params);


      Button validated = v.findViewById(R.id.send_comment);
      Button cancelled = v.findViewById(R.id.cancel_btn);
      ImageButton choseImage = v.findViewById(R.id.chose_image);
      ImageButton close_btn = v.findViewById(R.id.close_btn);

      response_text = v.findViewById(R.id.comment_text);
      outlinedTextField = v.findViewById(R.id.outlinedTextField);
      imageView = v.findViewById(R.id.image);

      choseImage.setOnClickListener(v3 -> {
         // TODO Managed the chose of the image description.
         image = BitmapFactory.decodeResource(getResources(), R.drawable.ic_account);
         imageView.setImageBitmap(image);
      });

      validated.setOnClickListener(v1 -> {
         String query = Objects.requireNonNull(response_text.getText()).toString();

         // TODO Managed the query of the response.

         mListener.onButtonClicked(query);
         dismiss();
      });

      cancelled.setOnClickListener(v12 -> dismiss());
      close_btn.setOnClickListener(v12 -> dismiss());

      dialog.setContentView(v);
      mBehavior = BottomSheetBehavior.from((View) v.getParent());
      return dialog;
   }

   @Override
   public void onStart() {
      super.onStart();
      mBehavior.setSkipCollapsed(false);
      mBehavior.setPeekHeight(getScreenHeight() - 100);
      mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
   }

   public interface BottomSheetListener {
      void onButtonClicked(String text);
   }

   @Override
   public void onAttach(@NonNull Context context) {
      super.onAttach(context);

      try {
         mListener = (BottomSheetListener) context;
      } catch (ClassCastException e) {
         throw new ClassCastException(context.toString()
               + " must implement BottomSheetListener");
      }
   }

   private static int getScreenHeight() {
      return Resources.getSystem().getDisplayMetrics().heightPixels;
   }
}
