package com.example.polyfast.forumManager.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.polyfast.R;
import com.example.polyfast.activities.PlateForm;
import com.example.polyfast.forumManager.activities.QuestionDetail;
import com.example.polyfast.forumManager.database.SQLiteUserManager;
import com.example.polyfast.forumManager.database.StudentHelper;
import com.example.polyfast.forumManager.database.TeacherHelper;
import com.example.polyfast.forumManager.models.Teacher;
import com.example.polyfast.forumManager.models.User;
import com.example.polyfast.fragment.Forum;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;
import java.util.Objects;

public class PolyFastFCM extends FirebaseMessagingService {

   private static final String TAG = "PolyFastFCM";
   private static final String GROUP_KEY = "Notification Group";
   private static final int SUMMARY_ID = 0;

   public static int notification_id = 1;

   @Override
   public void onNewToken(@NonNull String token) {

      User user = new SQLiteUserManager(this).getUserInfo();

      if (user instanceof Teacher)
         TeacherHelper.updateToken(token, user.getId());
      else
         StudentHelper.updateToken(token, user.getId());

      Log.i(TAG, "Token : " + token);

   }

   @Override
   public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {

      super.onMessageReceived(remoteMessage);

      Log.i(TAG, "Message receive : " + remoteMessage.getData());

      sendNotification(remoteMessage.getData());

   }

   private void sendNotification(Map<String, String> data) {

      String title = data.get("title");
      String[] content = Objects.requireNonNull(data.get("body")).split("/");
      String body = content[0];
      String question_id = content[1];
      String material = data.get("icon");
      String sender = data.get("sender");

      assert title != null;
      sendNotification(title, body, material, sender, question_id);
   }

   /**
    * Function to send the notification.
    */
   private void sendNotification(String title, String body, String material, String sender,
                                 String questionId) {

      Intent intention =  new Intent(this, QuestionDetail.class);
      Bundle bundle = new Bundle();
      bundle.putString(Forum.QUESTION_ID, questionId);

      if (title.equals("question"))
         title = getResources().getString(R.string.new_question) +" "+ getResources().getString(R.string.en) +" "+ material +" "+getResources().getString(R.string.from) + " " +getResources().getString(R.string.student)+ sender;
      else if (title.equals("comment")) {
         bundle.putString("Type", "comment");
         bundle.putString("responseId", material);
         title = sender + " " + getResources().getString(R.string.has_comment);
      }else
         title = getResources().getString(R.string.new_response) +" " + getResources().getString(R.string.on) +" "+ material +" "+getResources().getString(R.string.by) + " " + sender;


      intention.putExtras(bundle);
      intention.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
      PendingIntent pendingIntent = PendingIntent.getActivity(this,
            notification_id,intention, PendingIntent.FLAG_UPDATE_CURRENT);

      Notification notification;

      NotificationCompat.Builder notificationCompat = new NotificationCompat
            .Builder(this, App.CHANNEL_ID)
            .setSmallIcon(R.drawable.polifast)
            .setContentTitle(title)
            .setContentText(body)
            .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.account_icon))
            .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setGroup(GROUP_KEY)
            .setAutoCancel(true)
            .setColor(getResources().getColor(R.color.colorAccent))
            .setContentIntent(pendingIntent)
            .setVibrate(new long[] {0, 1000, 500, 1000})
            .setSound(Settings.System.DEFAULT_NOTIFICATION_URI);

      notification = notificationCompat.build();

      NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
      assert nm != null;
      nm.notify(notification_id, notification);

      notification_id++;

      sendNotificationSummary(title, body);

   }

   /**
    * Function to send the summary notification.
    */
   private void sendNotificationSummary (String title, String body) {

      Intent intent = new Intent(this, PlateForm.class);
      intent.putExtra("fragment_id", R.id.nav_forum);
      PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT);

      Notification notification = new NotificationCompat.Builder(this, App.CHANNEL_ID)
            .setSmallIcon(R.drawable.polifast)
            .setContentTitle(title)
            .setContentText(body)
            .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setGroup(GROUP_KEY)
            .setAutoCancel(true)
            .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.account_icon))
            .setColor(getResources().getColor(R.color.colorAccent))
            .setContentIntent(pendingIntent)
            .setStyle(new NotificationCompat.InboxStyle()
                  .setBigContentTitle(title)
                  .addLine("")
                  .setSummaryText("PolyFast "+getResources().getString(R.string.new_interaction))
            )
            .setGroupAlertBehavior(NotificationCompat.GROUP_ALERT_CHILDREN)
            .setGroupSummary(true)
            .build();

      NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
      assert nm != null;
      nm.notify(SUMMARY_ID, notification);
   }
}
