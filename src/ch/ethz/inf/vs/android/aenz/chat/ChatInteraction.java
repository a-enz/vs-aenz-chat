package ch.ethz.inf.vs.android.aenz.chat;

import org.json.JSONObject;

import ch.ethz.inf.vs.android.aenz.chat.Utils.ChatEventType;

// TODO Feel free to extend it to more classes...

/**
 * Abstraction of all interactions with the server
 * 
 * @author hong-an
 *
 */
public abstract class ChatInteraction {
	/**
	 * Type of interaction with the server
	 */
	protected ChatEventType eventType;
	
	/**
	 * The original JSON data received
	 */
	protected JSONObject jsonMap;
	
	/**
	 * Constructor
	 */
	public ChatInteraction() {
	}

	/**
	 * Constructor
	 * @param eventType
	 */
	public ChatInteraction(ChatEventType eventType) {
		this.eventType = eventType;
	}

	/**
	 * This function prepares the request so that it is ready to be sent in JSON.
	 */
	public abstract void prepareJSON();
}
