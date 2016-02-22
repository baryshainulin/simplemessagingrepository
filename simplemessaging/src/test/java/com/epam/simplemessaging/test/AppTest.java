package com.epam.simplemessaging.test;

import org.apache.log4j.Logger;

import com.epam.simplemessaging.service.Message;
import com.epam.simplemessaging.service.SimpleMessagingSession;
import com.epam.simplemessaging.service.SimpleMessagingSessionFactory;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for testing of simple messaging service concurrently.
 */
public class AppTest extends TestCase {
  private static final Logger theLogger = Logger.getLogger(AppTest.class.getName());

  private static final int NUMBER_OF_SENDER_THREADS = 10;
  private static final int NUMBER_OF_RECEIVER_THREADS = 10;
  private static final int QUEUE_CAPACITY = 5;

  private SimpleMessagingSessionFactory factory = SimpleMessagingSessionFactory.getInstance();

  private class SenderTask implements Runnable {

    private String userId;

    public SenderTask(String userId) {
      super();
      this.userId = userId;
    }

    public void run() {
      SimpleMessagingSession session = factory.getSession(userId, "1", false);
      String textMessage = "Content:" + userId;
      Message message = new Message();
      message.setTo("toUser");
      message.setBody(textMessage);
      theLogger
          .info("Sending the message from user " + userId + ", number of succeeed sender threads is " + numberOfSenderThreadsSucceeded);
      session.sendMessage(message);
      numberOfSenderThreadsSucceeded++;
      theLogger.info(String.format("Finished sending a message by user %s. Number of succeeed sender threads is %s", userId,
          numberOfSenderThreadsSucceeded));
    }
  }

  private class ReceiverTask implements Runnable {

    private String userId;

    public ReceiverTask(String userId) {
      super();
      this.userId = userId;
    }

    public void run() {
      SimpleMessagingSession session = factory.getSession(userId, "1", false);
      theLogger
          .info("Receiving messages by user " + userId + ", number of succeeded receiver threads is " + numberOfReceiverThreadsSucceeded);
      session.receiveMessage(0);
      numberOfReceiverThreadsSucceeded++;
      theLogger.info(String.format("Finished receiving a message by user %s. Number of succeeed receiver threads is %s", userId,
          numberOfReceiverThreadsSucceeded));
    }
  }

  // a number of succeeded threads is put into these variables
  private volatile int numberOfSenderThreadsSucceeded;
  private volatile int numberOfReceiverThreadsSucceeded;

  /**
   * Create the test case
   *
   * @param testName
   *          name of the test case
   */
  public AppTest(String testName) {
    super(testName);
  }

  /**
   * @return the suite of tests being tested
   */
  public static Test suite() {
    return new TestSuite(AppTest.class);
  }

  /**
   * The main entry to the test.
   */
  public void testApp() {

    theLogger.info("Start executing simple messaging service test.");

    // run a set of sender threads to test the service
    // the amount of thread is greater than queue capacity
    for (int i = 0; i < NUMBER_OF_SENDER_THREADS; i++) {
      String userId = generateUserId(i);
      new Thread(new SenderTask(userId)).start();
    }

    doPause();

    // some threads are finished because the started before the capacity limit
    // has reached
    // other threads are still blocked, let's check the number of succeeded
    // sender threads
    assertEquals("Number of finished sender threads.", QUEUE_CAPACITY, numberOfSenderThreadsSucceeded);

    theLogger.info("Starting receiver threads.");
    // run a set of receiver threads to test the service
    // the amount of threads is less than queue capacity
    for (int i = 0; i < NUMBER_OF_RECEIVER_THREADS; i++) {
      String userId = generateUserId(i);
      new Thread(new ReceiverTask(userId)).start();
    }

    doPause();
    
    // some number of receiver threads did finish work because they emptied the
    // queue
    // other threads are still blocked, let's check the number of succeeded
    // receiver threads
    assertEquals("Number of finished receiver threads", NUMBER_OF_RECEIVER_THREADS - QUEUE_CAPACITY, numberOfReceiverThreadsSucceeded);
    
    // TODO: add tests to check an order of messages
  }

  private void doPause() {
    try {
      Thread.sleep(200);
    } catch (InterruptedException e) {
      theLogger.error(e);
    }
  }

  private String generateUserId(int i) {
    return "user:" + i;
  }

}
