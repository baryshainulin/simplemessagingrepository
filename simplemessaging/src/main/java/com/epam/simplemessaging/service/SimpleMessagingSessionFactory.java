package com.epam.simplemessaging.service;

/**
 *  A factory for {@link SimpleMessagingSession}.
 *  
 */
public interface SimpleMessagingSessionFactory {
  /**
   * Generates a simple messaging session. Can user user id passed for identification.
   * @param userId current user id
   * @return
   */
  public SimpleMessagingSession getSession(String userId);
}
