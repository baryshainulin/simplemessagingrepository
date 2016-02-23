package com.epam.simplemessaging;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import com.epam.simplemessaging.service.Message;
import com.epam.simplemessaging.service.SimpleMessagingSession;
import com.epam.simplemessaging.service.SimpleMessagingSessionFactory;

/**
 * The application is a client for the Simple Messaging service.
 * It uses the service factory provided by container. 
 */
@Component
public class App
{
  private static final Logger theLogger = Logger.getLogger(App.class);
  
  @Autowired(required = true)
  SimpleMessagingSessionFactory factory;
  
  public static void main( String[] args)
  {
      theLogger.info("Entered the app.");
      ConfigurableApplicationContext appContext = SpringApplication.run(AppInitializer.class, args);
      
      App app = appContext.getBean(App.class);
      app.doTheTask();
  }
  
  public App() {
    super();
  }

  public void doTheTask() {
    theLogger.info("Session factory is " + (factory == null ? null : "not null."));
    
    SimpleMessagingSession session = factory.getSession("1");
    Message message = new Message();
    message.setBody("A");
    session.sendMessage(message);
  }
}
