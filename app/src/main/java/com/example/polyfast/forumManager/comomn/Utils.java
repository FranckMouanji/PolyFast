package com.example.polyfast.forumManager.comomn;

import java.text.DateFormat;
import java.util.Date;

public class Utils {

   /**
    * Function to get the date format.
    * @param pushDate Date.
    */
   public static String getFullDateFormat(Date pushDate) {
      return DateFormat.getDateInstance(DateFormat.FULL).format(pushDate);
   }

   /**
    * Function to get the time format.
    * @param pushDate Date.
    */
   public static String getFullTimeFormat(Date pushDate) {
      return DateFormat.getTimeInstance(DateFormat.SHORT).format(pushDate);
   }


}
