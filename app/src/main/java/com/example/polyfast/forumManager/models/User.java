package com.example.polyfast.forumManager.models;

import androidx.annotation.NonNull;

public class User {

   private String id;
   private String name;
   private String surname;
   private String email;
   private String password;

   User() {
   }

   User(String id, String name, String surname, String email, String password) {
      this.id = id;
      this.name = name;
      this.surname = surname;
      this.email = email;
      this.password = password;
   }

   public String getId() {
      return id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getSurname() {
      return surname;
   }

   public void setSurname(String surname) {
      this.surname = surname;
   }

   public String getEmail() {
      return email;
   }

   public void setEmail(String email) {
      this.email = email;
   }

   public String getPassword() {
      return password;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   @NonNull
   @Override
   public String toString() {
      return "User{" +
            "id='" + id + '\'' +
            ", name='" + name + '\'' +
            ", surname='" + surname + '\'' +
            ", email='" + email + '\'' +
            ", password='" + password + '\'' +
            '}';
   }
}
