package com.epam.simplemessaging.service.impl;

/**
 * A simple interface, implementations of which should act like a blocking queue.
 * When the number of stored elements will reach some capacity limit, the threads
 * are trying to put an element will be blocked. And vice versa, when an amount of
 * elements will reach zero, the threads trying to peek an element will be blocked
 * until some other thread will peek an element from the queue. 
 * @param <T>
 */
public interface SimpleBlockingQueue<T> {
  /**
   * Adds an element to the queue, blocking the thread if capacity limit is reached.
   * @param element an element to be added to the queue
   */
  public void put(T element);
  /**
   * Returns an element from the queue, blocking the thread in case the queue is empty.
   * @return
   */
  public T take();
}
