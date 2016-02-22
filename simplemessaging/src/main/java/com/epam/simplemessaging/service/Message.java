package com.epam.simplemessaging.service;

/**
 * A simple bean used to pass an message information.
 */
public class Message {
  
  private String body;
  private String sender;
  private String to;
  
  public String getBody() {
    return body;
  }
  public void setBody(String body) {
    this.body = body;
  }
  public String getSender() {
    return sender;
  }
  public void setSender(String sender) {
    this.sender = sender;
  }
  public String getTo() {
    return to;
  }
  public void setTo(String to) {
    this.to = to;
  }
  
}
