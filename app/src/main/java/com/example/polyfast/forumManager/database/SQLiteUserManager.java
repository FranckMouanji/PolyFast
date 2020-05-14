package com.example.polyfast.forumManager.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.polyfast.forumManager.models.Student;
import com.example.polyfast.forumManager.models.Teacher;
import com.example.polyfast.forumManager.models.User;

public class SQLiteUserManager extends SQLiteOpenHelper {

   private static final String DB_NAME = "PolyFastDB";
   private static final int DB_VERSION = 1;
   private static final String USER_TABLE = "UserInfo";
   private static final String TAG = "SQLiteUserManager";

   public SQLiteUserManager(@Nullable Context context) {
      super(context, DB_NAME, null, DB_VERSION);
   }

   @Override
   public void onCreate(SQLiteDatabase db) {
      String query = "create table " + USER_TABLE + "(" +
            "id text primary key" +
            ",name text" +
            ",surname text" +
            ",email text" +
            ",password text" +
            ",material text" +
            ",classLevel text" +
            ",level text" +
            ",status text"+
            ");";
      db.execSQL(query);
   }

   @Override
   public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

   }

   /**
    * Function to add user in the database.
    * @param user User.
    */
   public void addUserInfo (User user) {

      if (!deletedAllData())
         return;

      String status;
      String classLevel = "classLevel";
      String level = "level";
      String material = "material";

      if (user instanceof Student) {
         Student student = (Student) user;
         status = "student";
         classLevel = student.getClassLevel();
         level = student.getLevel();
      }
      else {
         Teacher teacher = (Teacher) user;
         material = teacher.getMaterial();
         status = "teacher";
      }

      try {
         String query = "insert into "+ USER_TABLE +" (id, name, surname, " +
               "email, password, material, classLevel, level, status) values ('"
               + user.getId() +"','"
               + user.getName() +"','"
               + user.getSurname() +"','"
               + user.getEmail() +"','"
               + user.getPassword() +"','"
               + material +"','"
               + classLevel +"','"
               + level +"','"
               + status +"')";

         this.getWritableDatabase().execSQL( query );

      }catch (SQLException e){
         Log.e(TAG, " Error : ", e);
      }
   }

   /**
    * Function to update the user info.
    * @param user User.
    */
   public void updateUserInfo (User user) {
      try {
         String query = "update "+ USER_TABLE +" set email = '"+user.getEmail()+"', " +
               "password = '"+user.getPassword()+"';";

         this.getWritableDatabase().execSQL( query );

      }catch (SQLException e){
         Log.e(TAG, "Error", e);
      }
   }

   /**
    * Function to get the user info.
    */
   public User getUserInfo () {

      try {
         String[] columns = {"id", "name", "surname", "email", "password", "material", "classLevel",
               "level", "status"};

         Cursor cursor = this.getReadableDatabase().query(USER_TABLE, columns, null,
               null
               , null, null, null);

         cursor.moveToFirst();

         String id = cursor.getString(cursor.getColumnIndex("id"));
         String name = cursor.getString(cursor.getColumnIndex("name"));
         String surname = cursor.getString(cursor.getColumnIndex("surname"));
         String email = cursor.getString(cursor.getColumnIndex("email"));
         String password = cursor.getString(cursor.getColumnIndex("password"));
         String material = cursor.getString(cursor.getColumnIndex("material"));
         String classLevel = cursor.getString(cursor.getColumnIndex("classLevel"));
         String level = cursor.getString(cursor.getColumnIndex("level"));
         String status = cursor.getString(cursor.getColumnIndex("status"));

         cursor.close();

         if (status.equals("student"))
            return new Student(id, name, surname, email, password, classLevel, level);
         else
            return new Teacher(id, name, surname, email, password, material);
      }
      catch (SQLiteException e) {
         Log.e(TAG, "Error : ", e);
         return null;
      }
   }

   /**
    * Function to listen if the user information is present.
    */
   public boolean userInfoExist () {
      try {
         String[] columns = {"id", "name"};

         Cursor cursor = this.getReadableDatabase().query(USER_TABLE, columns, null,
               null
               , null, null, null);

         if (cursor.getCount() == 0){
            cursor.close();
            return false;
         }
         else {
            cursor.close();
            return true;
         }

      }
      catch (SQLiteException e) {
         e.printStackTrace();
         return false;
      }
   }

   /**
    * Function to deleted data to the table.
    */
   private boolean deletedAllData () {
      try {
         String query = "delete from " + USER_TABLE ;
         this.getWritableDatabase().execSQL(query);
         return true;
      }
      catch (SQLiteException e){
         e.printStackTrace();
         return false;
      }
   }

}
