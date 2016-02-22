package com.epam.simplemessaging;

import org.apache.log4j.Logger;

import com.epam.simplemessaging.service.SimpleMessagingSession;
import com.epam.simplemessaging.service.SimpleMessagingSessionFactory;

/**
 * The application is a client for the Simple Messaging service.
 * It uses the service factory provided by container. 
 *
 */
//@Configuration
//@EnableAutoConfiguration
//@ComponentScan("com.epam.simplemessaging")
//@PropertySource("classpath:/application.properties")
public class App  
{
  private static final Logger theLogger = Logger.getLogger(App.class);
  
//  @Autowired(required = true)
//  @Qualifier("messaging")
  SimpleMessagingSessionFactory sessionFactory;
  
  public static void main( String[] args )
  {
      theLogger.info("Entered the app.");
//      SpringApplication.run(App.class, args);
      
      App app = new App();
      app.doTheTask();
  }
  
  public App() {
    super();
    this.sessionFactory = SimpleMessagingSessionFactory.getInstance();
  }

  public void doTheTask() {
    
  }
}
