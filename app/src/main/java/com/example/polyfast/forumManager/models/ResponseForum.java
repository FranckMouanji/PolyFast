package com.example.polyfast.forumManager.models;

import androidx.annotation.NonNull;

import java.util.Date;

public class ResponseForum {

   private String id;
   private String userAnswerName;
   private Date answerDate;
   private String response;
   private String image1;
   private String questionId;

   public ResponseForum() {
   }

   public ResponseForum(String id, String userAnswerName, Date answerDate, String response,
                        String image1, String questionId) {
      this.id = id;
      this.userAnswerName = userAnswerName;
      this.answerDate = answerDate;
      this.response = response;
      this.image1 = image1;
      this.questionId = questionId;
   }

   public String getId() {
      return id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public String getUserAnswerName() {
      return userAnswerName;
   }

   public void setUserAnswerName(String userAnswerName) {
      this.userAnswerName = userAnswerName;
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

   @NonNull
   @Override
   public String toString() {
      return "ResponseForum{" +
            "id='" + id + '\'' +
            ", userAnswerName='" + userAnswerName + '\'' +
            ", answerDate=" + answerDate +
            ", response='" + response + '\'' +
            ", image1='" + image1 + '\'' +
            ", questionId='" + questionId + '\'' +
            '}';
   }
}
