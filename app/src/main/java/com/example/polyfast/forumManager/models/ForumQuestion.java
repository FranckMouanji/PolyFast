package com.example.polyfast.forumManager.models;

import androidx.annotation.NonNull;

import java.util.Date;

public class ForumQuestion extends ForumModelsFactory {

   private String id;
   private String label;
   private String description;
   private String material;
   private String className;
   private Date pushDate;
   private String image1;
   private String imageDescription1;
   private String image2;
   private String imageDescription2;
   private int responseCount;
   private String authorId;
   private String lastAnswerAuthorId;
   private Date lastAnswerDate;

   public ForumQuestion() {
   }

   public ForumQuestion(String id, String label, String description, String material,
                        String className, Date pushDate, String image1, String imageDescription1,
                        String image2, String imageDescription2, int responseCount, String authorId,
                        String lastAnswerAuthorId, Date lastAnswerDate) {
      this.id = id;
      this.label = label;
      this.description = description;
      this.material = material;
      this.className = className;
      this.pushDate = pushDate;
      this.image1 = image1;
      this.imageDescription1 = imageDescription1;
      this.image2 = image2;
      this.imageDescription2 = imageDescription2;
      this.responseCount = responseCount;
      this.authorId = authorId;
      this.lastAnswerAuthorId = lastAnswerAuthorId;
      this.lastAnswerDate = lastAnswerDate;
   }

   public String getId() {
      return id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public String getLabel() {
      return label;
   }

   public void setLabel(String label) {
      this.label = label;
   }

   public String getDescription() {
      return description;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public String getMaterial() {
      return material;
   }

   public void setMaterial(String material) {
      this.material = material;
   }

   public String getClassName() {
      return className;
   }

   public void setClassName(String className) {
      this.className = className;
   }

   public Date getPushDate() {
      return pushDate;
   }

   public void setPushDate(Date pushDate) {
      this.pushDate = pushDate;
   }

   public String getImage1() {
      return image1;
   }

   public void setImage1(String image1) {
      this.image1 = image1;
   }

   public String getImageDescription1() {
      return imageDescription1;
   }

   public void setImageDescription1(String imageDescription1) {
      this.imageDescription1 = imageDescription1;
   }

   public String getImage2() {
      return image2;
   }

   public void setImage2(String image2) {
      this.image2 = image2;
   }

   public String getImageDescription2() {
      return imageDescription2;
   }

   public void setImageDescription2(String imageDescription2) {
      this.imageDescription2 = imageDescription2;
   }

   public int getResponseCount () {
      return responseCount;
   }

   public void setResponseCount (int responseCount) {
      this.responseCount = responseCount;
   }

   public String getAuthorId() {
      return authorId;
   }

   public void setAuthorId(String authorId) {
      this.authorId = authorId;
   }

   public String getLastAnswerAuthorId () {
      return lastAnswerAuthorId;
   }

   public void setLastAnswerAuthorId (String lastAnswerAuthorId) {
      this.lastAnswerAuthorId = lastAnswerAuthorId;
   }

   public Date getLastAnswerDate () {
      return lastAnswerDate;
   }

   public void setLastAnswerDate (Date lastAnswerDate) {
      this.lastAnswerDate = lastAnswerDate;
   }

   @NonNull
   @Override
   public String toString() {
      return "ForumQuestion{" +
            "id='" + id + '\'' +
            ", label='" + label + '\'' +
            ", description='" + description + '\'' +
            ", material='" + material + '\'' +
            ", className='" + className + '\'' +
            ", pushDate=" + pushDate +
            ", image1='" + image1 + '\'' +
            ", imageDescription1='" + imageDescription1 + '\'' +
            ", image2='" + image2 + '\'' +
            ", imageDescription2='" + imageDescription2 + '\'' +
            ", authorId='" + authorId + '\'' +
            ", lastAnswerAuthorId='" + lastAnswerAuthorId + '\'' +
            ", lastAnswerDate='" + lastAnswerDate + '\'' +
            '}';
   }
}
