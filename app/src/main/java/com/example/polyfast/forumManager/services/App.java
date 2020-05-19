package com.example.polyfast.forumManager.services;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class App extends Application {

   public static final String CHANNEL_ID = "PolyFast.forumManager.App";

   @Override
   public void onCreate() {
      super.onCreate();

      createNotificationChannel();
   }

   private void createNotificationChannel() {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

         NotificationChannel channel = new NotificationChannel(
               CHANNEL_ID, "PolyFast notification", NotificationManager.IMPORTANCE_HIGH);

         channel.setDescription("PolyFast Forum Notification");

         NotificationManager manager = getSystemService(NotificationManager.class);
         assert manager != null;

         manager.createNotificationChannel(channel);
      }
   }

}
