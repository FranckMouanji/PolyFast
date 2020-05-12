package com.example.polyfast.forumManager.models;

import androidx.annotation.NonNull;

public class Teacher extends User {

   private String material;

   public Teacher() {
   }

   public Teacher(String id, String name, String surname, String email, String password,
                  String material) {
      super(id, name, surname, email, password);
      this.material = material;
   }

   public String getMaterial() {
      return material;
   }

   public void setMaterial(String material) {
      this.material = material;
   }

   @NonNull
   @Override
   public String toString() {
      return "Teacher{" +
            "material='" + material + '\'' +
            '}';
   }
}
