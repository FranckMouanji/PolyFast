package com.example.polyfast.forumManager.models;

import androidx.annotation.NonNull;

public class Student extends User {

   private String classLevel;
   private String level;

   public Student() {
   }

   public Student(String id, String name, String surname, String email,
                  String password, String classLevel, String level) {
      super(id, name, surname, email, password, null);
      this.classLevel = classLevel;
      this.level = level;
   }

   public String getClassLevel() {
      return classLevel;
   }

   public void setClassLevel(String classLevel) {
      this.classLevel = classLevel;
   }

   public String getLevel() {
      return level;
   }

   public void setLevel(String level) {
      this.level = level;
   }

   @NonNull
   @Override
   public String toString() {
      return "Student{" +
            "id='" + getId() + '\'' +
            ", name='" + getName() + '\'' +
            ", surname='" + getSurname() + '\'' +
            ", email='" + getEmail() + '\'' +
            ", password='" + getPassword() + '\'' +
            ", token='" + getToken() + '\'' +
            ", classLevel='" + classLevel + '\'' +
            ", level='" + level + '\'' +
            '}';
   }
}
