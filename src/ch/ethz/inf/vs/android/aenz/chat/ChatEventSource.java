package ch.ethz.inf.vs.android.aenz.chat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

import org.json.JSONObject;

import ch.ethz.inf.vs.android.aenz.chat.Utils.ChatEventType;

/**
 * This class provides a wrapper for events to be triggered
 * by the chat logic.
 * @author hong-an
 *
 */
public class ChatEventSource {
	/**
	 * Listeners of events
	 */
	protected List<ChatEventListener> eventListenerList = new ArrayList<ChatEventListener>();
	
	/**
	 * Adding listeners
	 * @param listener
	 */
	public void addChatEventListener(ChatEventListener listener) {
		eventListenerList.add(listener);
	}
	
	/**
	 * Removing listener
	 * @param listener
	 */
	public void removeChatEventListener(ChatEventListener listener) {
		eventListenerList.remove(listener);
	}
	
	/**
	 * Class representing events as notifiers for the activity
	 * @author hong-an
	 *
	 */
	public class ChatEvent extends EventObject implements Serializable{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1930041885003089779L;
		/**
		 * Content of the message to be transmitted
		 */
		public final String message;
		/**
		 * Original JSON packet
		 */
		public JSONObject request;
		/**
		 * Type of event
		 */
		protected ChatEventType type;
		
		/**
		 * Getter for the event type
		 * @return
		 */
		public ChatEventType getType() {
			return type;
		}
		public ChatEvent(Object source, ChatEventType type, String message, JSONObject request) {
			super(source);
			this.type = type;
			this.message = message;
			this.request = request;
		}
		
		/**
		 * Function that dispatches an event 
		 * that should be listened to by the
		 * an event listener.
		 */
		public void dispatchEvent() {
			// TODO Fill me with events to dispatch to the listener
			switch(this.type) {
			case SOME_STATE:
				// TODO Do something...
				break;
				
			default:
				break;
			}
		}
	}
	
	
}
