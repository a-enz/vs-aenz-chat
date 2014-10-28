package ch.ethz.inf.vs.android.aenz.chat;

import org.json.JSONException;
import org.json.JSONObject;

import ch.ethz.inf.vs.android.aenz.chat.Utils.ChatEventType;
import ch.ethz.inf.vs.android.aenz.chat.Utils.SyncType;

/**
 * This function represents a parsed JSON packet that contains
 * a message
 * @author hong-an
 *
 */
public class ChatMessage extends ChatInteraction {
	private String text;
	private int sender;
	private long timestamp;
	private Lamport lamportTime;
	private VectorClock vectorTime;
	private SyncType syncMethod;

	/**
	 * Constructor
	 */
	public ChatMessage() {
		super();
	}

	/**
	 * Constructor
	 * @param eventType
	 * @param sender
	 * @param text
	 * @param lamportTime
	 * @param vectorTime
	 * @param timestamp
	 * @param syncMethod
	 */
	public ChatMessage(ChatEventType eventType, int sender, String text,
			Lamport lamportTime, VectorClock vectorTime, long timestamp,
			SyncType syncMethod) {
		super(eventType);
		this.sender = sender;
		this.text = text;
		this.lamportTime = lamportTime;
		this.vectorTime = vectorTime;
		this.timestamp = timestamp;
		this.syncMethod = syncMethod;
	}

	/**
	 * This methods determines if a message is
	 * @param previous Previous message that was delivered
	 * @param sync Method for determining if deliverable
	 * @return
	 */
	public boolean isDeliverable(ChatMessage previous, SyncType sync) {
		// TODO Fill me depending on sync...
		return false;
	}

	/**
	 * String representation of the object.
	 */
	public String toString() {
		return "{\"cmd\": \"message\"" + ", \"time_vector\": "
				+ this.vectorTime.toString() + ", \"lamport\": "
				+ this.lamportTime.toString() + ", \"text\": " + this.text
				+ ", \"sender\": " + this.sender + "}";

	}
	
	public String getText() {
		return text;
	}
	
	public int getSender() {
		return sender;
	}
	
	public long getTimestamp() {
		return timestamp;
	}
	
	public Lamport getLamportTime() {
		return lamportTime;
	}
	
	public VectorClock getVectorTime() {
		return vectorTime;
	}
	
	public SyncType getSyncMethod() {
		return syncMethod;
	}

	/**
	 * This function remaps the object to a JSON object.
	 */
	@Override
	public void prepareJSON() {
		try {
			jsonMap = new JSONObject(this.toString());
		} catch (JSONException e) {
			// We fucked up creating a JSONObject
			e.printStackTrace();
		}
	}
	
	public JSONObject getJSON() {
		if (jsonMap == null) {
			prepareJSON();
		}
		return jsonMap;
	}

}
