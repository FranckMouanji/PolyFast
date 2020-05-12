package com.example.polyfast.forumManager.models;

import androidx.annotation.NonNull;

import java.util.Date;

public class ForumComment extends ForumModelsFactory{

   private String id;
   private String authorId;
   private String comment;
   private Date date;
   private String responseId;

   public ForumComment() {
   }

   public ForumComment(String id, String authorId, String comment, Date date, String responseId) {
      this.id = id;
      this.authorId = authorId;
      this.comment = comment;
      this.date = date;
      this.responseId = responseId;
   }

   public String getId() {
      return id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public String getAuthorId() {
      return authorId;
   }

   public void setAuthorId(String authorId) {
      this.authorId = authorId;
   }

   public String getComment() {
      return comment;
   }

   public void setComment(String comment) {
      this.comment = comment;
   }

   public Date getDate() {
      return date;
   }

   public void setDate(Date date) {
      this.date = date;
   }

   public String getResponseId() {
      return responseId;
   }

   public void setResponseId(String responseId) {
      this.responseId = responseId;
   }

   @NonNull
   @Override
   public String toString() {
      return "ForumComment{" +
            "id='" + id + '\'' +
            ", authorId='" + authorId + '\'' +
            ", comment='" + comment + '\'' +
            ", date=" + date +
            ", responseId='" + responseId + '\'' +
            '}';
   }
}
