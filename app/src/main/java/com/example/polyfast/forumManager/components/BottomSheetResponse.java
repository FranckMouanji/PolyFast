package com.example.polyfast.forumManager.components;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityOptionsCompat;

import com.bumptech.glide.Glide;
import com.example.polyfast.R;
import com.example.polyfast.forumManager.FullImageView;
import com.example.polyfast.forumManager.database.ImageStorageHelper;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class BottomSheetResponse extends BottomSheetDialogFragment {

   private static final int PICK_IMAGE_REQUEST_CODE = 0;
   private BottomSheetListener mListener;
   private BottomSheetBehavior mBehavior;
   private TextInputLayout outlinedTextField;
   private TextInputEditText response_text;
   private ImageView imageView;
   private TextView text_loading;
   private ProgressBar imageProgress;
   private String imageUrl;
   private View v;
   private Button validated;
   private Uri imageUri;

   @NonNull
   @Override
   public Dialog onCreateDialog(Bundle savedInstanceState) {
      BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

      assert getArguments() != null;

      v = View.inflate(getContext(), R.layout.bottomsheet_response, null);

      ConstraintLayout layout = v.findViewById(R.id.root);
      LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) layout.getLayoutParams();
      params.height = getScreenHeight() - 100;
      layout.setLayoutParams(params);


      validated = v.findViewById(R.id.send_comment);
      Button cancelled = v.findViewById(R.id.cancel_btn);
      ImageButton choseImage = v.findViewById(R.id.chose_image);
      ImageButton close_btn = v.findViewById(R.id.close_btn);
      ImageButton deleted_image = v.findViewById(R.id.deleted_image);
      imageProgress = v.findViewById(R.id.imageUploadProgress);

      response_text = v.findViewById(R.id.comment_text);
      outlinedTextField = v.findViewById(R.id.outlinedTextField);
      imageView = v.findViewById(R.id.image_response);
      text_loading = v.findViewById(R.id.text_loading);

      imageView.setOnClickListener(v->{
         ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat
               .makeSceneTransitionAnimation(requireActivity(),imageView,"image");
         Intent fullImageIntent = new Intent(requireContext(), FullImageView.class);
         fullImageIntent.putExtra("imageUri", imageUri.toString());
         startActivity(fullImageIntent, activityOptionsCompat.toBundle());
      });

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
      void onButtonClicked(String text, @Nullable String image);
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

         validated.setClickable(false);
         validated.setActivated(false);
         validated.setBackgroundColor(requireContext().getResources().getColor(R.color.darkLight));

         if (imageUri == null){
            String query = Objects.requireNonNull(response_text.getText()).toString().trim();
            mListener.onButtonClicked(query, null);
            dismiss();
         }
         else if (storageTask != null && storageTask.isInProgress()) {
            Toast.makeText(requireContext(), "Upload in progress", Toast.LENGTH_SHORT).show();
         } else {
            uploadImage();
         }

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
         imageUri = Objects.requireNonNull(data).getData();

         if (requestCode == PICK_IMAGE_REQUEST_CODE){
            Glide.with(requireContext())
                  .load(imageUri)
                  .centerCrop()
                  .into(imageView);
            v.findViewById(R.id.image).setVisibility(View.VISIBLE);
            v.findViewById(R.id.container_btn_chose_image).setVisibility(View.GONE);
//            try {
//               Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), imageUri);
//               imageView.setImageBitmap(imageBitmap);
//
//            } catch (IOException e) {
//               e.printStackTrace();
//            }
         }

      }
   }

   @Override
   public void onDismiss(@NonNull DialogInterface dialog) {
      super.onDismiss(dialog);
      imageUrl = null;
   }

   private StorageTask storageTask;

   /**
    * Function to upload the image to fireStorage.
    */
   private void uploadImage () {

      UploadTask uploadTask = ImageStorageHelper.add(imageUri, requireContext());
      if (uploadTask != null) {
         imageProgress.setVisibility(View.VISIBLE);
         text_loading.setVisibility(View.VISIBLE);
         storageTask = uploadTask
               .addOnProgressListener(taskSnapshot -> {
                  double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                        .getTotalByteCount());
                  imageProgress.setProgress((int) progress);
               })
               .addOnFailureListener(e -> Toast.makeText(requireContext(), e.getMessage(),
                     Toast.LENGTH_SHORT).show())
               .addOnSuccessListener(success -> {

                  Handler handler = new Handler();
                  handler.postDelayed(() -> imageProgress.setProgress(0), 100);

                  Toast.makeText(getContext(), "Upload successful", Toast.LENGTH_LONG).show();

                  success.getStorage().getDownloadUrl().addOnSuccessListener(taskSuccess->{
                     imageUrl = taskSuccess.toString();
                     String query = Objects.requireNonNull(response_text.getText()).toString().trim();
                     mListener.onButtonClicked(query, imageUrl);
                     dismiss();
                  }).addOnFailureListener(taskFailure-> Toast.makeText(getContext(),
                        requireContext().getString(R.string.error_has_provided), Toast.LENGTH_SHORT).show());

               });

      }
   }
}
