package com.epam.simplemessaging.service.impl;

import static java.util.logging.Level.INFO;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.epam.simplemessaging.service.Message;
import com.epam.simplemessaging.service.SimpleMessagingSession;

/**
 *  An implementation of {@link SimpleMessagingSession}.
 *  TODO: add to/from user routing.
 */
public class SimpleMessagingSessionImpl implements SimpleMessagingSession {

  private static final Logger theLogger = Logger.getLogger(SimpleMessagingSessionImpl.class.getName());
  
  private String userName;
  private SimpleBlockingQueue<Message> processor;
  
  public SimpleMessagingSessionImpl(SimpleBlockingQueue<Message> processor) {
    super();
    this.processor = processor;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public void sendMessage(Message message) {
    if (theLogger.isLoggable(Level.INFO)) {
      theLogger.log(INFO, String.format("Sending a message, to: %s, from: %s, body: %s.", message.getTo(), message.getSender(), message.getBody()));
    }
    processor.put(message);
  }

  public Message receiveMessage() {
    Message message = processor.take();
    if (theLogger.isLoggable(Level.INFO)) {
      if (message != null) {
        theLogger.log(INFO, String.format("Returing a message, to: %s, from: %s, body: %s.", message.getTo(), message.getSender(), message.getBody()));
      } else {
        theLogger.log(INFO, String.format("Returing a null message, to: %s.", userName));
      }
    }
    return message;
  }

  String getUserName() {
    return userName;
  }
  
}
