package com.example.polyfast.forumManager.utils;

import android.content.Context;
import android.util.Log;

import com.example.polyfast.data_class.Users;
import com.example.polyfast.forumManager.database.SQLiteUserManager;
import com.example.polyfast.forumManager.database.StudentHelper;
import com.example.polyfast.forumManager.database.TeacherHelper;
import com.example.polyfast.forumManager.models.Student;
import com.example.polyfast.forumManager.models.Teacher;
import com.example.polyfast.forumManager.models.User;

import java.text.DateFormat;
import java.util.Date;

public class Utils {

   private static final String TAG = "Utils";

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

   /**
    * Function to add user in particular collection.
    * @param users users.
    */
   public static void addUser (Users users, Context context) {

      if (users.getStatut().equals("professeur")){
         Teacher teacher = new Teacher();
         teacher.setName(users.getName());
         teacher.setSurname(users.getSurname());
         teacher.setEmail(users.getEmail());
         teacher.setPassword(users.getPassword());
         teacher.setMaterial(users.getMatiere_enseigner());

         TeacherHelper.addTeacher(teacher).addOnSuccessListener(success->{
            users.setId(success.getId());
            addToSQL(users, context);
         });
      }
      else{
         Student student = new Student();
         student.setName(users.getName());
         student.setSurname(users.getSurname());
         student.setEmail(users.getEmail());
         student.setPassword(users.getPassword());
         student.setClassLevel(users.getClasse());
         student.setLevel(users.getNiveau());

         StudentHelper.addStudent(student).addOnSuccessListener(success->{
            users.setId(success.getId());
            addToSQL(users, context);
         });
      }

      Log.i(TAG, "Added to firebase.");

   }

   /**
    * Function to update the particular user.
    * @param users users.
    */
   public static void updateUser(Users users, Context context) {
      if (users.getStatut().equals("professeur"))
         TeacherHelper.updateTeacher(users.getEmail(), users.getPassword())
               .addOnSuccessListener(success-> updateToSQL(users, context));
      else
         StudentHelper.updateStudent(users.getEmail(), users.getPassword())
               .addOnSuccessListener(success->updateToSQL(users, context));

      Log.i(TAG, "Update to firebase.");
   }

   /**
    * Function to add user information to the sql database.
    * @param users Users.
    * @param context Application context.
    */
   public static void addToSQL (Users users, Context context) {
      if (users.getStatut().equals("professeur")){
         Teacher teacher = new Teacher();
         teacher.setName(users.getName());
         teacher.setSurname(users.getSurname());
         teacher.setEmail(users.getEmail());
         teacher.setPassword(users.getPassword());
         teacher.setMaterial(users.getMatiere_enseigner());

         TeacherHelper.getTeacherByMail(users.getEmail()).addOnSuccessListener(success->{
            teacher.setId(success.getDocuments().get(0).getId());
            SQLiteUserManager db = new SQLiteUserManager(context);
            db.addUserInfo(teacher);
            Log.i(TAG, "teacher : "+users+" is added To the sql data base.");
         });

      }
      else{
         Student student = new Student();
         student.setName(users.getName());
         student.setSurname(users.getSurname());
         student.setEmail(users.getEmail());
         student.setPassword(users.getPassword());
         student.setClassLevel(users.getClasse());
         student.setLevel(users.getNiveau());

         StudentHelper.getStudentByMail(users.getEmail()).addOnSuccessListener(success->{
            student.setId(success.getDocuments().get(0).getId());
            SQLiteUserManager db = new SQLiteUserManager(context);
            db.addUserInfo(student);
            Log.i(TAG, "student " + users + " is added To the sql data base.");
         });
      }
   }

   /**
    * Function to update the user information in sql database.
    * @param users User
    * @param context Application context.
    */
   private static void updateToSQL(Users users, Context context) {

      User user = new User();
      user.setEmail(users.getEmail());
      user.setPassword(users.getPassword());

      SQLiteUserManager db = new SQLiteUserManager(context);

      if (db.userInfoExist())
         db.updateUserInfo(user);
      else
         addToSQL(users, context);

      Log.i(TAG, "Updated to sql.");

   }

}
