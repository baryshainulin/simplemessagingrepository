package com.epam.simplemessaging.service.impl;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.apache.log4j.Logger;

/**
 * This class is an implementation of {@link SimpleBlockingQueue} interface.
 * An {@link ArrayBlockingQueue} implementation was used due to absence of time.
 * @param <T> the type of an elements the queue serves.
 */
public class ReentrantLockBlockingQueue<T> implements SimpleBlockingQueue<T> {
  private static final Logger theLogger = Logger.getLogger(ReentrantLockBlockingQueue.class); 

  private BlockingQueue<T> queue; 
  
  public ReentrantLockBlockingQueue(Integer capacity) {
    this.queue = new ArrayBlockingQueue<T>(capacity);
  }
  
  @Override
  public void put(T element) {
    try {
      queue.put(element);
    } catch (InterruptedException e) {
      // ignore this as we are not expecting this one
      theLogger.error("An unexpected interrupted exception aroused.", e);
    }
  }

  @Override
  public T take() {
    try {
      return queue.take();
    } catch (InterruptedException e) {
      // ignore this as we are not expecting this one
      theLogger.error("An unexpected interrupted exception aroused.", e);
      return null;
    }
  }

}
