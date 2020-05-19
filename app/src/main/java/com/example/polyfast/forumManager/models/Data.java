package com.example.polyfast.forumManager.models;

public class Data {

   private String sender;
   private String body;
   private String icon;
   private String title;

   public Data () {}

   public Data (String sender, String body, String icon, String title) {
      this.sender = sender;
      this.body = body;
      this.title = title;
      this.icon = icon;
   }

   public String getSender() {
      return sender;
   }

   public void setSender(String sender) {
      this.sender = sender;
   }

   public String getBody() {
      return body;
   }

   public void setBody(String body) {
      this.body = body;
   }

   public String getIcon() {
      return icon;
   }

   public void setIcon(String icon) {
      this.icon = icon;
   }

   public String getTitle() {
      return title;
   }

   public void setTitle(String title) {
      this.title = title;
   }
}
