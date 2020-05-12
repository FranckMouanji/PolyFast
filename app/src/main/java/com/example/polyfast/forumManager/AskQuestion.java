package com.example.polyfast.forumManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.polyfast.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

import static android.view.View.GONE;

public class AskQuestion extends AppCompatActivity {

   private static final String TAG = "AskQuestion";
   private static final int LABEL_MAX_TEXT_LENGTH = 100;
   private static final int DESCRIPTION_MAX_TEXT_LENGTH = 1000;
   private static final int PICK_IMAGE_REQUEST_CODE_1 = 0;
   private static final int PICK_IMAGE_REQUEST_CODE_2 = 1;

   private int[] numberImage = new int[2];
   private Bitmap[] bitmaps = new Bitmap[2];
   MaterialCardView containerImage1, containerImage2;
   TextInputEditText label_question, description_question;
   TextInputLayout outline_label_question, outline_description_question;
   TextView error_image_description1, error_image_description2;
   EditText image_description1, image_description2;
   ImageButton deleted_image1, deleted_image2;
   Button posted_btn;
   ImageView imageView1, imageView2, chose_image;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_ask_question);

      MaterialToolbar toolbar = findViewById(R.id.toolbar);
      setSupportActionBar(toolbar);
      if (getSupportActionBar() != null) {
         getSupportActionBar().setDisplayHomeAsUpEnabled(true);
         getSupportActionBar().setDisplayShowHomeEnabled(true);
         getSupportActionBar().setTitle(R.string.new_question);
      }

      numberImage[0] = 0;
      numberImage[1] = 0;

      initViews(); // init views.

      posted_btn.setOnClickListener(v -> postQuestion());

      labelWatcher ();
      descriptionWatcher();
      imageDescriptionWatcher ();

   }

   /**
    * Function to watched changing on the description fields image.
    */
   private void imageDescriptionWatcher() {

      image_description1.addTextChangedListener(new TextWatcher() {
         @Override
         public void beforeTextChanged(CharSequence s, int start, int count, int after) {

         }

         @Override
         public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length()>0)
               error_image_description1.setVisibility(GONE);
            else
               error_image_description1.setVisibility(View.VISIBLE);
         }

         @Override
         public void afterTextChanged(Editable s) {

         }
      });

      image_description2.addTextChangedListener(new TextWatcher() {
         @Override
         public void beforeTextChanged(CharSequence s, int start, int count, int after) {

         }

         @Override
         public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length()>0)
               error_image_description2.setVisibility(GONE);
            else
               error_image_description2.setVisibility(View.VISIBLE);
         }

         @Override
         public void afterTextChanged(Editable s) {

         }
      });

   }

   /**
    * Function to watched the changed in label edit text.
    */
   private void labelWatcher() {
      label_question.addTextChangedListener(new TextWatcher() {
         @Override
         public void beforeTextChanged(CharSequence s, int start, int count, int after) {

         }

         @Override
         public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() > LABEL_MAX_TEXT_LENGTH)
               outline_label_question.setError(getString(R.string.text_too_long));
            else
               outline_label_question.setError(null);

         }

         @Override
         public void afterTextChanged(Editable s) {

         }
      });

   }
   /**
    * Function to watched the changed in description edit text.
    */
   private void descriptionWatcher() {
      description_question.addTextChangedListener(new TextWatcher() {
         @Override
         public void beforeTextChanged(CharSequence s, int start, int count, int after) {

         }

         @Override
         public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() > DESCRIPTION_MAX_TEXT_LENGTH)
               outline_description_question.setError(getString(R.string.text_too_long));
            else
               outline_description_question.setError(null);

         }

         @Override
         public void afterTextChanged(Editable s) {

         }
      });

   }

   /**
    * Function to post the question.
    */
   private void postQuestion() {
      if (questionLabelIsValid() && questionDescriptionIsValid()){
         if (imageIsSet()){
            boolean hasImage1 = false, hasImage2 = false, error = false;

            if (numberImage[0] == 1) {
               if (imageDescription1IsValid())
                  hasImage1 = true;
               else
                  error = true;
            }
            if (numberImage[1] == 2) {
               if (imageDescription2IsValid())
                  hasImage2 = true;
               else
                  error = true;
            }
            if (!error)
               postWithImages(hasImage1, hasImage2);
         }
         else
            postWithoutImages();

      }
   }

   /**
    * Function to posted question with images.
    */
   private void postWithImages(boolean hasImage1, boolean hasImage2) {

      // TODO managed the posted of the question.

      if (hasImage1) {
         Bitmap image1 = bitmaps[0];
      }

      if (hasImage2) {
         Bitmap image2 = bitmaps[1];
      }

      Toast.makeText(this, "With image : " + Arrays.toString(bitmaps),
            Toast.LENGTH_SHORT).show();

      Log.i(TAG, "Question is posted with image.");
   }

   /**
    * Function to posted question without image.
    */
   private void postWithoutImages() {

      // TODO managed the posted of the question.

      Toast.makeText(this, "without image.", Toast.LENGTH_SHORT).show();

      Log.i(TAG, "Question is posted without image.");
   }

   /**
    * Function to check if the question label is empty.
    */
   private boolean questionLabelIsEmpty () {
      if (Objects.requireNonNull(label_question.getText()).toString().trim().isEmpty()) {
         outline_label_question.setError(getString(R.string.description_not_empty));
         return true;
      }
      return false;
   }

   /**
    * Function to checked if the length of label question is up the max length.
    */
   private boolean questionLabelLengthUpMax ()  {
      return Objects.requireNonNull(label_question.getText()).toString().trim().length() > LABEL_MAX_TEXT_LENGTH;
   }

   /**
    * Function to checked validation of the question label.
    */
   private boolean questionLabelIsValid () {
      return !questionLabelIsEmpty() && !questionLabelLengthUpMax();
   }

   /**
    * Function to check if the question description is empty.
    */
   private boolean questionDescriptionIsEmpty () {
      if (Objects.requireNonNull(description_question.getText()).toString().trim().isEmpty()) {
         outline_description_question.setError(getString(R.string.description_not_empty));
         return true;
      }
      return false;
   }

   /**
    * Function to checked if the length of description question is up the max length.
    */
   private boolean questionDescriptionLengthUpMax ()  {
      return Objects.requireNonNull(label_question.getText()).toString().trim().length() > DESCRIPTION_MAX_TEXT_LENGTH;
   }

   /**
    * Function to checked validation of the question description.
    */
   private boolean questionDescriptionIsValid () {
      return !questionDescriptionIsEmpty() && !questionDescriptionLengthUpMax();
   }

   /**
    * Function to check if the user give and image.
    */
   private boolean imageIsSet () {
      return numberImage[0] == 1 || numberImage[1] == 2;
   }

   /**
    * Function to initialized views.
    */
   private void initViews() {
      containerImage1 = findViewById(R.id.container_image1);
      containerImage2 = findViewById(R.id.container_image2);
      label_question = findViewById(R.id.label_question);
      description_question = findViewById(R.id.description_question);
      outline_description_question = findViewById(R.id.outline_description_question);
      outline_label_question = findViewById(R.id.outline_label_question);
      error_image_description1 = findViewById(R.id.error_image_description1);
      error_image_description2 = findViewById(R.id.error_image_description2);
      image_description1 = findViewById(R.id.image_description1);
      image_description2 = findViewById(R.id.image_description2);
      deleted_image1 = findViewById(R.id.deleted_image1);
      deleted_image2 = findViewById(R.id.deleted_image2);
      posted_btn = findViewById(R.id.posted_btn);
      imageView1 = findViewById(R.id.imageView1);
      imageView2 = findViewById(R.id.imageView2);
      chose_image = findViewById(R.id.chose_image);

      // Deleting listener.
      findViewById(R.id.deleted_image1).setOnClickListener(v -> {
         numberImage[0] = 0;
         hideContainer1();
         hideChoseImageBtn();
      });
      findViewById(R.id.deleted_image2).setOnClickListener(v -> {
         numberImage[1] = 0;
         hideChoseImageBtn();
         hideContainer2();
      });

   }
   /**
    * Function hide the container 1.
    */
   private void hideContainer1() {
      containerImage1.setVisibility(GONE);
      image_description1.setText(null);
      bitmaps[0] = null;
   }
   /**
    * Function hide the container 2.
    */
   private void hideContainer2() {
      containerImage2.setVisibility(GONE);
      image_description2.setText(null);
      bitmaps[1] = null;
   }

   /**
    * Function to checked if the image description 1 is valid.
    */
   private boolean imageDescription1IsValid () {
      if (image_description1.getText().toString().trim().isEmpty()) {
         error_image_description1.setVisibility(View.VISIBLE);
         return false;
      }
      return true;
   }
   /**
    * Function to checked if the image description 2 is valid.
    */
   private boolean imageDescription2IsValid () {
      if (image_description2.getText().toString().trim().isEmpty()) {
         error_image_description2.setVisibility(View.VISIBLE);
         return false;
      }
      return true;
   }

   @Override
   public boolean onOptionsItemSelected(@NonNull MenuItem item) {
      finish();
      return true;
   }

   /**
    * Listener when click on button chose image.
    */
   public void choseImageListener(View view) {

      if (numberImage[0] == 0) {
         numberImage[0] = 1;
         pickImage(PICK_IMAGE_REQUEST_CODE_1);
      }
      else if (numberImage[1] == 0) {
         numberImage[1] = 2;
         pickImage(PICK_IMAGE_REQUEST_CODE_2);
      }

      hideChoseImageBtn();
   }

   /**
    * Function to pick and image from gallery.
    */
   private void pickImage(int requestCode) {
      Intent pickIntent = new Intent(Intent.ACTION_GET_CONTENT);
      pickIntent.addCategory(Intent.CATEGORY_OPENABLE);
      pickIntent.setType("image/*");
      startActivityForResult(Intent.createChooser(pickIntent,
            getString(R.string.chose_an_app_image)), requestCode);
   }

   private void hideChoseImageBtn() {
      if (numberImage[0] == 1 && numberImage[1] == 2)
         chose_image.setVisibility(GONE);
      else
         chose_image.setVisibility(View.VISIBLE);
   }

   @Override
   protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
      super.onActivityResult(requestCode, resultCode, data);
      if (resultCode == RESULT_OK) {
         Uri imageUri = Objects.requireNonNull(data).getData();

         if (requestCode == PICK_IMAGE_REQUEST_CODE_1){
            try {
               Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
               bitmaps[0] = imageBitmap;
               imageView1.setImageBitmap(imageBitmap);
               containerImage1.setVisibility(View.VISIBLE);
            } catch (IOException e) {
               e.printStackTrace();
            }
            // Set the image 1.
         }
         if (requestCode == PICK_IMAGE_REQUEST_CODE_2) {
            // Set the image 2.
            try {
               Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
               bitmaps[1] = imageBitmap;
               imageView2.setImageBitmap(imageBitmap);
               containerImage2.setVisibility(View.VISIBLE);
            } catch (IOException e) {
               e.printStackTrace();
            }
         }
      }
   }
}
