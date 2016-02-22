package com.epam.simplemessaging.service;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.epam.simplemessaging.service.impl.SimpleBlockingQueue;
import com.epam.simplemessaging.service.impl.SimpleMessagingSessionImpl;

/**
 * A factory class for retrieving instances of {@link SimpleMessagingSession}.
 * The property com.epam.simplemessaging.service should be configured.
 */
@SuppressWarnings("unchecked")
public class SimpleMessagingSessionFactory {

  private static final String QUEUE_IMPL_CLASS_PROPERTY = "com.epam.simplemessaging.queue.impl";
  private static final String QUEUE_CAPACITY_PROPERTY = "com.epam.simplemessaging.queue.capacity";

  private static final String APPLICATION_PROPERTIES_LOCATION = "application.properties";

  private static final Logger theLogger = Logger.getLogger(SimpleMessagingSessionFactory.class);

  private static SimpleMessagingSessionFactory theInstance;
  
  // this field contains an implementation of blocking queue
  private SimpleBlockingQueue<Message> blockingQueue;

  private SimpleMessagingSessionFactory(SimpleBlockingQueue<Message> queueImpl) {
    super();
    this.blockingQueue = queueImpl;
  }

  public static SimpleMessagingSessionFactory getInstance() {
    if (theInstance == null) {
      SimpleBlockingQueue<Message> queueImpl = getQueueImpl();
      theInstance = new SimpleMessagingSessionFactory(queueImpl);
    }
    return theInstance;
  }

  public SimpleMessagingSession getSession(String name, String password, boolean isNew) {
    // the queue is shared between all the session
    return new SimpleMessagingSessionImpl(blockingQueue);
  }

  private static SimpleBlockingQueue<Message> getQueueImpl() {
    theLogger.info("An instance of SimpleMessagingSessionFactory is to be created.");
    
    InputStream propertiesResource = SimpleMessagingSessionFactory.class.getClassLoader().getResourceAsStream(APPLICATION_PROPERTIES_LOCATION);
    if (propertiesResource != null) {
      Properties props = new Properties();
      String className = null;
      try {
        props.load(propertiesResource);
        className = props.getProperty(QUEUE_IMPL_CLASS_PROPERTY);
        if (className != null && !"".equals(className)) {
          Class<?> clazz = Class.forName(className);
          if (SimpleBlockingQueue.class.isAssignableFrom(clazz)) {
            // get the constructor with capacity
            Constructor<?> constructor = clazz.getConstructor(new Class[] {Integer.class});
            // TODO: handle the case the prop is absent
            String capacity = props.getProperty(QUEUE_CAPACITY_PROPERTY);
            // TODO: additionally check the type parameter
            return (SimpleBlockingQueue<Message>) constructor.newInstance(Integer.parseInt(capacity));
          } else {
            throw new IllegalStateException(String.format("The class %s is not a simple blocking queue.", className));
          }
        } else {
          theLogger.error(
              String.format("The properties file %s does not contain the %s property needed to initialize simple messaging service.",
                  APPLICATION_PROPERTIES_LOCATION, QUEUE_IMPL_CLASS_PROPERTY));
          throw new IllegalStateException("Unable to load queue implementation.");
        }
      } catch (IOException e) {
        theLogger.error(String.format("An error while initializing the system from properties file %s", APPLICATION_PROPERTIES_LOCATION),
            e);
        throw new IllegalStateException("Unable to load queue implementation.");
      } catch (ClassNotFoundException e) {
        theLogger.error(String.format("The class %s is not found.", className), e);
        throw new IllegalStateException("Unable to load queue implementation.");
      } catch (Exception e) {
        theLogger.error("The errror while constructing the queue.", e);
        throw new IllegalStateException("Unable to load queue implementation.");
      } finally {
        try {
          propertiesResource.close();
        } catch (IOException e) {
          theLogger.error(String.format("The error while closing the file ", APPLICATION_PROPERTIES_LOCATION), e);
        }
      }
    } else {
      theLogger.error(String.format("The properties file %s could not be found.", APPLICATION_PROPERTIES_LOCATION));
      throw new IllegalStateException("Unable to load queue implementation.");
    }
  }
}
