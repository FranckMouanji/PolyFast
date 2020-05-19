package com.example.polyfast.forumManager.services;

import com.example.polyfast.forumManager.models.MyResponse;
import com.example.polyfast.forumManager.models.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiService {

   @Headers (
         {
            "Content-Type:application/json",
                  "Authorization:key=AAAAW5mFAAc:APA91bGHHvYW01lf73KrJdy_82SRb_Lmr_BxtDMd5hSBMiVVEWwYTyDBYCaljkHhD0pvnXUKFSYEqksUcBQjAt-s6_odpGuTycofUwuX_XxT_eBsZoxA9mzguVnmRCRaXr7bfwfHrTZM"
         }
      )

   @POST ("fcm/send")
   Call<MyResponse> sendNotification(@Body Sender body);

}
