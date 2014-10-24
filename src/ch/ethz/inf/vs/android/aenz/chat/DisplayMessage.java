package ch.ethz.inf.vs.android.aenz.chat;

import java.io.Serializable;

import android.provider.Settings.System;
import android.text.format.Time;

/**
 * Message is a Custom Object to encapsulate message information/fields
 * 
 * @author Adil Soomro
 *
 */
public class DisplayMessage implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4959875512486267237L;

	/**
	 * The content of the message
	 */
	private String message;

	/**
	 * The name of the sender
	 */
	private String username;
	
	/**
	 * boolean to determine, who is the sender of the message
	 */
	private boolean isMine;
	
	/**
	 * boolean to determine, whether the message is a status message or not. it
	 * reflects the changes/updates about the sender is writing, have entered
	 * text etc
	 */
	private boolean isStatusMessage;
	
	/**
	 * String for the time when the
	 * message is delivered.
	 */
	private String date;

	/**
	 * Constructor to make a Message object
	 */
	public DisplayMessage(String message, String username, boolean isMine) {
		super();
		this.message = message;
		this.username = username;
		this.isMine = isMine;
		this.isStatusMessage = false;
		this.date = Utils.getTime();
	}

	/**
	 * Constructor to make a status Message object consider the parameters are
	 * swaped from default Message constructor, not a good approach but have to
	 * go with it.
	 */
	public DisplayMessage(boolean status, String message, String username) {
		super();
		this.message = message;
		this.username = username;
		this.isMine = false;
		this.isStatusMessage = status;
		this.date = Utils.getTime();
	}

	/**
	 * Getter for the message
	 * @return String that contains the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Setter for the message
	 * @param message String that contains the message
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Getter for the indicator of whether the message belongs to the owner of the device
	 * @return a boolean that indicates wheter the message belongs to the owner of the device
	 */
	public boolean isMine() {
		return isMine;
	}

	/**
	 * Setter for the indicator of whether the message belongs to the owner of the device
	 * @param isMine a boolean that indicates wheter the message belongs to the owner of the device
	 */
	public void setMine(boolean isMine) {
		this.isMine = isMine;
	}

	/**
	 * Getter for the status of message (currently being written)... Not useful in this exercise
	 * @return a boolean that indicates the status of the message
	 */
	public boolean isStatusMessage() {
		return isStatusMessage;
	}

	/**
	 * Setter for the status of message (currently being written)... Not useful in this exercise
	 * @param isStatusMessage a boolean that indicates the status of the message
	 */
	public void setStatusMessage(boolean isStatusMessage) {
		this.isStatusMessage = isStatusMessage;
	}

	/**
	 * Getter for the username of the sender of the message
	 * 
	 * @return The message sender's username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Setter for the username of the message
	 * 
	 * @param username
	 *            The message sender's username
	 */
	public void setUsername(String username) {
		this.username = username;
	}


	/**
	 * Getter for the time message is displayed
	 * @return String for the time
	 */
	public String getDate() {
		return date;
	}

	/**
	 * Setter for the time the message is displayed
	 * @param date String for the time
	 */
	public void setDate(String date) {
		this.date = date;
	}
}
