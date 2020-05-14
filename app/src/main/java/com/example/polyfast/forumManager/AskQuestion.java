package com.example.polyfast.forumManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.polyfast.R;
import com.example.polyfast.forumManager.database.ImageStorageHelper;
import com.example.polyfast.forumManager.database.QuestionHelper;
import com.example.polyfast.forumManager.database.SQLiteUserManager;
import com.example.polyfast.forumManager.models.ForumQuestion;
import com.example.polyfast.forumManager.models.Student;
import com.example.polyfast.forumManager.models.User;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;
import java.util.Objects;

import static android.view.View.GONE;

public class AskQuestion extends AppCompatActivity {

   private static final String TAG = "AskQuestion";
   private static final int LABEL_MAX_TEXT_LENGTH = 100;
   private static final int DESCRIPTION_MAX_TEXT_LENGTH = 1000;
   private static final int PICK_IMAGE_REQUEST_CODE_1 = 0;
   private static final int PICK_IMAGE_REQUEST_CODE_2 = 1;

   private int[] numberImage = new int[2];
   private Uri[] imageUris = new Uri[2];
   MaterialCardView containerImage1, containerImage2;
   TextInputEditText label_question, description_question;
   TextInputLayout outline_label_question, outline_description_question, layout_material;
   AutoCompleteTextView material_view;
   TextView error_image_description1, error_image_description2;
   EditText image_description1, image_description2;
   ImageButton deleted_image1, deleted_image2;
   Button posted_btn;
   ImageView imageView1, imageView2, chose_image;
   private ProgressBar progressBar1, progressBar2;

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

      imageView1.setOnClickListener(v->{
         ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat
               .makeSceneTransitionAnimation(this,imageView1,"image");
         Intent fullImageIntent = new Intent(this, FullImageView.class);
         fullImageIntent.putExtra("imageUri", imageUris[0].toString());
         startActivity(fullImageIntent, activityOptionsCompat.toBundle());
      });

      imageView2.setOnClickListener(v->{
         ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat
               .makeSceneTransitionAnimation(this,imageView2,"image");
         Intent fullImageIntent = new Intent(this, FullImageView.class);
         fullImageIntent.putExtra("imageUri", imageUris[1].toString());
         startActivity(fullImageIntent, activityOptionsCompat.toBundle());
      });

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
      if (questionMaterialIsValid() && questionLabelIsValid() && questionDescriptionIsValid()){
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
    * Function to listen if the material value is not empty.
    */
   private boolean questionMaterialIsValid() {
      if (material_view.getText().toString().trim().isEmpty()){
         layout_material.setError(getString(R.string.description_not_empty));
         return false;
      }
      layout_material.setError(null);
      return true;
   }

   /**
    * Function to posted question with images.
    */
   private void postWithImages(boolean hasImage1, boolean hasImage2) {

      posted_btn.setClickable(false);
      posted_btn.setActivated(false);
      posted_btn.setBackgroundColor(getResources().getColor(R.color.darkLight));

      User user = new SQLiteUserManager(this).getUserInfo();
      String authorId = user.getId();
      String classLevel = ((Student)user).getClassLevel();

      ForumQuestion question = new ForumQuestion();
      question.setAuthorId(authorId);
      question.setLabel(Objects.requireNonNull(label_question.getText()).toString().trim());
      question.setDescription(Objects.requireNonNull(description_question.getText()).toString().trim());
      question.setClassName(classLevel);
      question.setImage1("null");
      question.setImageDescription1("null");
      question.setImage2("null");
      question.setImageDescription2("null");
      question.setLastAnswerAuthorId("null");
      question.setLastAnswerDate(null);
      question.setPushDate(Calendar.getInstance().getTime());
      question.setResponseCount(0);
      question.setMaterial(material_view.getText().toString().trim());

      if (hasImage1) {
         if (storageTask1 != null && storageTask1.isInProgress()) {
            Toast.makeText(this, "Upload in progress", Toast.LENGTH_SHORT).show();
         } else {
            uploadImage1(question, hasImage2);
         }
      }else if (hasImage2) {
         if (storageTask2 != null && storageTask2.isInProgress()) {
            Toast.makeText(this, "Upload in progress", Toast.LENGTH_SHORT).show();
         } else {
            uploadImage2(question);
         }
      }
   }

   /**
    * Function to posted question without image.
    */
   private void postWithoutImages() {

      ForumQuestion forumQuestion = getForumQuestionWithoutImage();
      ProgressDialog progressDialog = ProgressDialog.show(this, null, getString(R.string.loading_send));

      QuestionHelper.addQuestion(forumQuestion)
            .addOnCompleteListener(complete -> {
               if (complete.isSuccessful())
                  Toast.makeText(this, getString(R.string.post_is_success), Toast.LENGTH_SHORT).show();
               else
                  Toast.makeText(this, getString(R.string.error_has_provided), Toast.LENGTH_SHORT).show();

               progressDialog.dismiss();

               finish();
            });
   }

   /**
    * Function to get the question fields without images.
    */
   private ForumQuestion getForumQuestionWithoutImage() {

      User user = new SQLiteUserManager(this).getUserInfo();
      String authorId = user.getId();
      String classLevel = ((Student)user).getClassLevel();

      ForumQuestion question = new ForumQuestion();
      question.setAuthorId(authorId);
      question.setLabel(Objects.requireNonNull(label_question.getText()).toString().trim());
      question.setDescription(Objects.requireNonNull(description_question.getText()).toString().trim());
      question.setClassName(classLevel);
      question.setImage1("null");
      question.setImageDescription1("null");
      question.setImage2("null");
      question.setImageDescription2("null");
      question.setLastAnswerAuthorId("null");
      question.setLastAnswerDate(null);
      question.setPushDate(Calendar.getInstance().getTime());
      question.setResponseCount(0);
      question.setMaterial(material_view.getText().toString().trim());

      return question;
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
      material_view = findViewById(R.id.material);
      outline_description_question = findViewById(R.id.outline_description_question);
      outline_label_question = findViewById(R.id.outline_label_question);
      layout_material = findViewById(R.id.layout_material);
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
      progressBar1 = findViewById(R.id.progressBar1);
      progressBar2 = findViewById(R.id.progressBar2);

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

      String[] materialList = getResources().getStringArray(R.array.nom_matiere);

      ArrayAdapter<String> adapter =
            new ArrayAdapter<>(
                  this,
                  R.layout.item_drop_down_material,
                  materialList);

      material_view.setAdapter(adapter);

   }
   /**
    * Function hide the container 1.
    */
   private void hideContainer1() {
      containerImage1.setVisibility(GONE);
      image_description1.setText(null);
      imageUris[0] = null;
   }
   /**
    * Function hide the container 2.
    */
   private void hideContainer2() {
      containerImage2.setVisibility(GONE);
      image_description2.setText(null);
      imageUris[1] = null;
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
            // Set the image 1.
            imageUris[0] = imageUri;
            Glide.with(this)
                  .load(imageUri)
                  .centerCrop()
                  .into(imageView1);
            containerImage1.setVisibility(View.VISIBLE);
         }
         if (requestCode == PICK_IMAGE_REQUEST_CODE_2) {
            // Set the image 2.
            imageUris[1] = imageUri;
            Glide.with(this)
                  .load(imageUri)
                  .centerCrop()
                  .into(imageView2);
            containerImage2.setVisibility(View.VISIBLE);
         }
      }
   }

   private StorageTask storageTask1, storageTask2;

   /**
    * Function to upload the image1 to fireStorage.
    */
   private void uploadImage1 (ForumQuestion question, boolean hasImage2) {

      UploadTask uploadTask = ImageStorageHelper.add(imageUris[0], this);
      if (uploadTask != null) {
         progressBar1.setVisibility(View.VISIBLE);
         storageTask1 = uploadTask
               .addOnProgressListener(taskSnapshot -> {
                  double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                        .getTotalByteCount());
                  progressBar1.setProgress((int) progress);
               })
               .addOnFailureListener(e -> Toast.makeText(this, e.getMessage(),
                     Toast.LENGTH_SHORT).show())
               .addOnSuccessListener(success -> {

                  Handler handler = new Handler();
                  handler.postDelayed(() -> progressBar1.setProgress(0), 100);

                  Toast.makeText(this, "Upload successful", Toast.LENGTH_LONG).show();

                  success.getStorage().getDownloadUrl().addOnSuccessListener(taskSuccess->{
                     String imageUrl = taskSuccess.toString();
                     String query = Objects.requireNonNull(image_description1.getText()).toString().trim();
                     question.setImage1(imageUrl);
                     question.setImageDescription1(query);

                     if (hasImage2)
                        uploadImage2(question);
                     else
                        sendTheQuestion(question);

                  }).addOnFailureListener(taskFailure-> Toast.makeText(this,
                        getString(R.string.error_has_provided), Toast.LENGTH_SHORT).show());

               });

      }
   }


   /**
    * Function to upload the image2 to fireStorage.
    */
   private void uploadImage2 (ForumQuestion question) {

      UploadTask uploadTask = ImageStorageHelper.add(imageUris[1], this);
      if (uploadTask != null) {
         progressBar2.setVisibility(View.VISIBLE);
         storageTask2 = uploadTask
               .addOnProgressListener(taskSnapshot -> {
                  double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                        .getTotalByteCount());
                  progressBar2.setProgress((int) progress);
               })
               .addOnFailureListener(e -> Toast.makeText(this, e.getMessage(),
                     Toast.LENGTH_SHORT).show())
               .addOnSuccessListener(success -> {

                  Handler handler = new Handler();
                  handler.postDelayed(() -> progressBar2.setProgress(0), 100);

                  Toast.makeText(this, "Upload successful", Toast.LENGTH_LONG).show();

                  success.getStorage().getDownloadUrl().addOnSuccessListener(taskSuccess->{
                     String imageUrl = taskSuccess.toString();
                     String query = Objects.requireNonNull(image_description2.getText()).toString().trim();
                     question.setImage2(imageUrl);
                     question.setImageDescription2(query);

                     sendTheQuestion(question);

                  }).addOnFailureListener(taskFailure-> Toast.makeText(this,
                        getString(R.string.error_has_provided), Toast.LENGTH_SHORT).show());

               });

      }
   }

   /**
    * Function to send the question to firebase database.
    * @param question Question.
    */
   private void sendTheQuestion(ForumQuestion question) {

      ProgressDialog progressDialog = ProgressDialog.show(this, null, getString(R.string.loading_send));

      QuestionHelper.addQuestion(question)
            .addOnCompleteListener(complete -> {

               if (complete.isSuccessful())
                  Toast.makeText(this, getString(R.string.post_is_success), Toast.LENGTH_SHORT).show();
               else
                  Toast.makeText(this, getString(R.string.error_has_provided), Toast.LENGTH_SHORT).show();

               progressDialog.dismiss();

               finish();
            });

      Log.i(TAG, "Question is posted with image.");
   }

}
