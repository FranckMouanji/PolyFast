package com.example.polyfast.forumManager.models;

import androidx.annotation.NonNull;

import java.util.Date;

public class ForumComment extends ForumModelsFactory{

   private String id;
   private String authorId;
   private String comment;
   private Date date;
   private String responseId;
   private String status;

   public ForumComment() {
   }

   public ForumComment(String id, String authorId, String comment, Date date, String responseId, String status) {
      this.id = id;
      this.authorId = authorId;
      this.comment = comment;
      this.date = date;
      this.responseId = responseId;
      this.status = status;
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

   public String getStatus () {
      return status;
   }

   public void setStatus (String status) {
      this.status = status;
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
            ", status='" + status + '\'' +
            '}';
   }
}
