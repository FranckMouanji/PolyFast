package com.example.polyfast.forumManager.models;

import androidx.annotation.NonNull;

import java.util.Date;

public class ForumResponse extends ForumModelsFactory {

   private String id;
   private String authorId;
   private Date answerDate;
   private String response;
   private String image1;
   private String questionId;
   private int commentCount;
   private int validateCount;

   public ForumResponse() {
   }

   public ForumResponse(String id, String authorId, Date answerDate, String response,
                        String image1, String questionId, int commentCount, int validateCount) {
      this.id = id;
      this.authorId = authorId;
      this.answerDate = answerDate;
      this.response = response;
      this.image1 = image1;
      this.questionId = questionId;
      this.commentCount = commentCount;
      this.validateCount = validateCount;
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

   public Date getAnswerDate() {
      return answerDate;
   }

   public void setAnswerDate(Date answerDate) {
      this.answerDate = answerDate;
   }

   public String getResponse() {
      return response;
   }

   public void setResponse(String response) {
      this.response = response;
   }

   public String getImage1() {
      return image1;
   }

   public void setImage1(String image1) {
      this.image1 = image1;
   }

   public String getQuestionId() {
      return questionId;
   }

   public void setQuestionId(String questionId) {
      this.questionId = questionId;
   }

   public int getCommentCount () {
      return commentCount;
   }

   public void setCommentCount (int commentCount) {
      this.commentCount = commentCount;
   }

   public int getValidateCount () {
      return validateCount;
   }

   public void setValidateCount (int validateCount) {
      this.validateCount = validateCount;
   }

   @NonNull
   @Override
   public String toString() {
      return "ResponseForum{" +
            "id='" + id + '\'' +
            ", authorId='" + authorId + '\'' +
            ", answerDate=" + answerDate +
            ", response='" + response + '\'' +
            ", image1='" + image1 + '\'' +
            ", questionId='" + questionId + '\'' +
            ", commentCount='" + commentCount + '\'' +
            ", validateCount='" + validateCount + '\'' +
            '}';
   }
}
