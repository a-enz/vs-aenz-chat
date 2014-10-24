package ch.ethz.inf.vs.android.aenz.chat;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import ch.ethz.inf.vs.android.aenz.chat.Utils.ChatEventType;
import ch.ethz.inf.vs.android.aenz.chat.Utils.SyncType;

@SuppressLint("UseSparseArrays")
/**
 * This class handles all the logic for the communication
 * between the chat client and the server.
 * 
 * @author hong-an
 *
 */
public class ChatLogic extends ChatEventSource{

	/**
	 * Context of the activity
	 */
	Context appContext;

	/**
	 * Handler for outgoing requests.
	 */
	private Handler requestHandler;
	/**
	 * Handler for incoming requests.
	 */
	private Handler receiveHandler;

	/**
	 * This object handles the UDP communication between the client and the chat
	 * server
	 */
	UDPCommunicator comm;
	
	/**
	 * This object should be used to log 
	 * deliverable messages.
	 */
	Logger log;

	/**
	 * This function should initialize the logger as
	 * soon as the username is registered.
	 * @param username
	 */
	public void initLogger(String username) {
		this.log = new Logger(username, appContext);
	}

	/**
	 * Constructor
	 * @param context Context of the Activity
	 * @param sync Indicates whether Lamport timestamps or Vector clocks should be used
	 */
	public ChatLogic(Context context, SyncType sync) {

	}

	/**
	 * This function should parse incoming JSON packets and trigger
	 * the necessary events.
	 * @param jsonMap Incoming JSON packet
	 * @return The type of event that took place.
	 * @throws JSONException
	 */
	public ChatEventType parseJSON(JSONObject jsonMap) throws JSONException {
		// TODO Fill me
		return ChatEventType.SOME_STATE;
	}

}
