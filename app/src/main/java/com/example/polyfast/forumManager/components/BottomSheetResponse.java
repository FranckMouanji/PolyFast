package com.example.polyfast.forumManager.components;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.polyfast.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class BottomSheetResponse extends BottomSheetDialogFragment {

   private static final int PICK_IMAGE_REQUEST_CODE = 0;
   private BottomSheetListener mListener;
   private BottomSheetBehavior mBehavior;
   private TextInputLayout outlinedTextField;
   private TextInputEditText response_text;
   private ImageView imageView;
   private Bitmap image;
   private View v;

   @NonNull
   @Override
   public Dialog onCreateDialog(Bundle savedInstanceState) {
      BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

      v = View.inflate(getContext(), R.layout.bottomsheet_response, null);

      ConstraintLayout layout = v.findViewById(R.id.root);
      LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) layout.getLayoutParams();
      params.height = getScreenHeight() - 100;
      layout.setLayoutParams(params);


      Button validated = v.findViewById(R.id.send_comment);
      Button cancelled = v.findViewById(R.id.cancel_btn);
      ImageButton choseImage = v.findViewById(R.id.chose_image);
      ImageButton close_btn = v.findViewById(R.id.close_btn);
      ImageButton deleted_image = v.findViewById(R.id.deleted_image);

      response_text = v.findViewById(R.id.comment_text);
      outlinedTextField = v.findViewById(R.id.outlinedTextField);
      imageView = v.findViewById(R.id.image_response);

      deleted_image.setOnClickListener(v3 -> {
         v.findViewById(R.id.image).setVisibility(View.GONE);
         v.findViewById(R.id.container_btn_chose_image).setVisibility(View.VISIBLE);
      });

      choseImage.setOnClickListener(v3 -> pickImage());

      validated.setOnClickListener(v1 -> postResponse());

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

      responseViewWatcher();
   }

   public interface BottomSheetListener {
      void onButtonClicked(String text, @Nullable Bitmap image);
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

   /**
    * Function to check if the response is empty.
    */
   private boolean responseIsEmpty () {
      if (Objects.requireNonNull(response_text.getText()).toString().trim().isEmpty()) {
         outlinedTextField.setError(requireContext().getString(R.string.description_not_empty));
         return true;
      }
      return false;
   }

   /**
    * Function to checked if the length of response is up the max length.
    */
   private boolean responseLengthUpMax ()  {
      return Objects.requireNonNull(response_text.getText()).toString().trim().length() > 500;
   }

   /**
    * Function to get if the response input text is valid.
    */
   private boolean responseIsValid () {
      return !responseIsEmpty() && !responseLengthUpMax();
   }

   /**
    * Function to watched the changed in response edit text.
    */
   private void responseViewWatcher() {
      response_text.addTextChangedListener(new TextWatcher() {
         @Override
         public void beforeTextChanged(CharSequence s, int start, int count, int after) {

         }

         @Override
         public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() > 500)
               outlinedTextField.setError(requireContext().getString(R.string.text_too_long));
            else
               outlinedTextField.setError(null);

         }

         @Override
         public void afterTextChanged(Editable s) {

         }
      });

   }

   /**
    * Function to post the response.
    */
   private void postResponse () {

      if (responseIsValid()){

         String query = Objects.requireNonNull(response_text.getText()).toString().trim();
         mListener.onButtonClicked(query, image);
         dismiss();

      }

   }

   /**
    * Function to pick and image from gallery.
    */
   private void pickImage() {
      Intent pickIntent = new Intent(Intent.ACTION_GET_CONTENT);
      pickIntent.addCategory(Intent.CATEGORY_OPENABLE);
      pickIntent.setType("image/*");
      startActivityForResult(Intent.createChooser(pickIntent,
            getString(R.string.chose_an_app_image)), PICK_IMAGE_REQUEST_CODE);
   }

   @Override
   public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
      super.onActivityResult(requestCode, resultCode, data);

      if (resultCode == RESULT_OK) {
         Uri imageUri = Objects.requireNonNull(data).getData();

         if (requestCode == PICK_IMAGE_REQUEST_CODE){
            try {
               Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), imageUri);
               image = imageBitmap;
               imageView.setImageBitmap(imageBitmap);
               v.findViewById(R.id.image).setVisibility(View.VISIBLE);
               v.findViewById(R.id.container_btn_chose_image).setVisibility(View.GONE);
            } catch (IOException e) {
               e.printStackTrace();
            }
         }

      }
   }

   @Override
   public void onDismiss(@NonNull DialogInterface dialog) {
      super.onDismiss(dialog);
      image = null;
   }
}
