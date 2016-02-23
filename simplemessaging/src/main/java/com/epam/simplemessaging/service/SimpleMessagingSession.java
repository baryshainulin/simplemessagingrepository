package com.epam.simplemessaging.service;

/**
 *  The session in interaction with SimpleMessaging service.
 */
public interface SimpleMessagingSession {
	/**
	 * Sends a message for a user.
	 * @param message and message object to be sent.
	 */
	public void sendMessage(Message message);
	/**
	 * Synchronously receives a message for current user.
	 * Waits until any message appears if the inbox is empty.
	 * @return the message received.
	 */
	public Message receiveMessage();
}
