package com.epam.simplemessaging;

import java.lang.reflect.InvocationTargetException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import com.epam.simplemessaging.service.Message;
import com.epam.simplemessaging.service.SimpleMessagingSession;
import com.epam.simplemessaging.service.SimpleMessagingSessionFactory;
import com.epam.simplemessaging.service.impl.SimpleBlockingQueue;
import com.epam.simplemessaging.service.impl.SimpleMessagingSessionImpl;

@Configuration
@EnableAutoConfiguration
@ComponentScan("com.epam.simplemessaging")
@PropertySource("classpath:/application.properties")
public class AppInitializer {
  private static final Logger theLogger = Logger.getLogger(AppInitializer.class.getName());

  @Value("${com.epam.simplemessaging.queue.impl}")
  private String queueImpl;

  @Value("${com.epam.simplemessaging.queue.capacity}")
  private Integer capacity;

  @Bean(autowire = Autowire.BY_TYPE)
  public SimpleMessagingSessionFactory getSession() throws InstantiationException, IllegalAccessException, ClassNotFoundException,
      IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
    theLogger.info(String.format("Initializing SimpleMessaging Session with the values: implClass %s, capacity: %s.", queueImpl, capacity));
    @SuppressWarnings("unchecked")
    final SimpleBlockingQueue<Message> processor = (SimpleBlockingQueue<Message>) Class.forName(queueImpl)
        .getConstructor(new Class[] { Integer.class }).newInstance(capacity);

    return new SimpleMessagingSessionFactory() {

      @Override
      public SimpleMessagingSession getSession(String userId) {
        return new SimpleMessagingSessionImpl(processor);
      }
    };
  }
  
  @Bean
  public static PropertySourcesPlaceholderConfigurer placeHolderConfigurer() {
   return new PropertySourcesPlaceholderConfigurer();
  }
}
