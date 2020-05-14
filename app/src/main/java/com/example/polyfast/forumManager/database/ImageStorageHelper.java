package com.example.polyfast.forumManager.database;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ImageStorageHelper {

   private static StorageReference getStorageReference () {
      return FirebaseStorage.getInstance().getReference("Images");
   }

   public static UploadTask add(Uri mImageUri, Context context) {
      if (mImageUri != null) {
         StorageReference fileReference = getStorageReference().child(System.currentTimeMillis()
               + "." + getFileExtension(mImageUri, context));
         return fileReference.putFile(mImageUri);
      } else {
         Toast.makeText(context, "No file selected", Toast.LENGTH_SHORT).show();
         return null;
      }
   }

   private static String getFileExtension(Uri uri, Context context) {
      ContentResolver cr = context.getContentResolver();
      MimeTypeMap mime = MimeTypeMap.getSingleton();
      return mime.getExtensionFromMimeType(cr.getType(uri));
   }

}
