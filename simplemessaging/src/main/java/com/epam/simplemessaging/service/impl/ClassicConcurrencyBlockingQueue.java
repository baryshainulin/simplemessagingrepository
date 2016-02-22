package com.epam.simplemessaging.service.impl;

import java.util.LinkedList;
import java.util.Queue;

import org.apache.log4j.Logger;

public class ClassicConcurrencyBlockingQueue<T> implements SimpleBlockingQueue<T> {

  private static final Logger theLogger = Logger.getLogger(ClassicConcurrencyBlockingQueue.class); 
  
  /**
   * The objects are locks for add/remove operations.
   * TODO: carefully analyze if one lock can be used for both.
   */
  private Object removeOperationLock = new Object();
  private Object addOperationLock = new Object();
  
  private Queue<T> queue = new LinkedList<T>();
  private int capacity;
  
  public ClassicConcurrencyBlockingQueue(Integer capacity) {
    this.capacity = capacity;
  }
  
  /**
   * Adds an element to the queue, blocking the thread
   * if number of queue elements equals or more capacity.
   * @param object
   */
  @Override
  public void put(T object) {
    synchronized(addOperationLock) {
      if (queue.size() > capacity) {
        // in this case wait for the queue to be under the limit of capacity
        theLogger.info("Queue size " + queue.size() + " reached capacity " + capacity + ", applying a lock.");
        try {
          addOperationLock.wait();
          queue.offer(object);
        } catch (InterruptedException e) {
          // the thread is supposed to be interrupted only by some untypical circumstances
        }
      } else {
        // the capacity allows to insert one more element
        queue.offer(object);
        if (queue.size() == 1) {
          // there was no elements before, so, there could be threads waiting to receive a message
          // need to unlock those by calling to notify
          theLogger.info("Queue size " + queue.size() + " is no more zero. Removing a lock.");
          removeOperationLock.notify();
        }
      }
    }
  }
  
  @Override
  public T take() {
    synchronized (removeOperationLock) {
      if (queue.size() == 0) {
        try {
          // wait the thread by this object until it will be woken up by notify method called by other thread
          theLogger.info("Queue size " + queue.size() + " reached zero. Applying a lock.");
          removeOperationLock.wait();
          return queue.peek();
        } catch (InterruptedException e) {
          // the thread is supposed to be interrupted only by some untypical circumstances
          return null;
        }
      } else {
        T result = queue.poll();
        if (queue.size() == capacity - 1) {
          // queue size was over capacity, let's unlock the thread waiting for the lock.
          theLogger.info("Queue size " + queue.size() + " is now less than capacity. Removing a lock.");
          addOperationLock.notify();
        }
        return result;
      }
    }
  }
}
