package com.example.polyfast.forumManager.utils;

import com.example.polyfast.data_class.Users;
import com.example.polyfast.forumManager.database.StudentHelper;
import com.example.polyfast.forumManager.database.TeacherHelper;
import com.example.polyfast.forumManager.models.Student;
import com.example.polyfast.forumManager.models.Teacher;

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

   /**
    * Function to add user in particular collection.
    * @param users users.
    */
   public static void addUser (Users users) {

      if (users.getStatut().equals("professeur")){
         Teacher teacher = new Teacher();
         teacher.setName(users.getName());
         teacher.setSurname(users.getSurname());
         teacher.setEmail(users.getEmail());
         teacher.setPassword(users.getPassword());
         teacher.setMaterial(users.getMatiere_enseigner());

         TeacherHelper.addTeacher(teacher);
      }
      else{
         Student student = new Student();
         student.setName(users.getName());
         student.setSurname(users.getSurname());
         student.setEmail(users.getEmail());
         student.setPassword(users.getPassword());
         student.setClassLevel(users.getClasse());
         student.setLevel(users.getNiveau());

         StudentHelper.addStudent(student);
      }

   }

   /**
    * Function to update the particular user.
    * @param users users.
    */
   public static void updateUser(Users users) {
      if (users.getStatut().equals("professeur"))
         TeacherHelper.updateTeacher(users.getEmail(), users.getPassword());
      else
         StudentHelper.updateStudent(users.getEmail(), users.getPassword());

   }
}
