package ch.ethz.inf.vs.android.aenz.chat;

import java.io.IOException;
import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
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
	private Handler receiveHandler = new Handler() {
		
		@Override
		public void handleMessage(Message msg) {
			
		}
	};

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
	
	private boolean listening;

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
	 * Use only after Internet connection has been setup
	 * @param context Context of the Activity
	 * @param sync Indicates whether Lamport timestamps or Vector clocks should be used
	 */
	public ChatLogic(Context context, SyncType sync) {
		listening = false;
		initReceiver();
	}

	/**
	 * This function should parse incoming JSON packets and trigger
	 * the necessary events.
	 * @param jsonMap Incoming JSON packet
	 * @return The type of event that took place.
	 * @throws JSONException
	 */
	public ChatEventType parseJSON(JSONObject jsonMap){
		String cmd;
		try {
			cmd = jsonMap.getString("cmd");
			switch(cmd) {
			case "register":
				if(jsonMap.getString("status").equals("success")) {
					return Utils.ChatEventType.REGISTER_SUCCESS;
				} else if(jsonMap.getString("status").equals("failure")){	//failure
					if(jsonMap.getString("text").length() > 20) {	//the text must be the long one of listing 4 (username already used...)
						return Utils.ChatEventType.USERNAME_INVALID;
					} else if(jsonMap.getString("text").equals("Not registered")) {
						return Utils.ChatEventType.NOT_REGISTERED;
					} else if(jsonMap.getString("text").equals("Already registered")){
						return Utils.ChatEventType.ALREADY_REGISTERED;
					} else {
						return Utils.ChatEventType.ERROR;
					}
				} else {
					return Utils.ChatEventType.ERROR;
				}
			case "notification":
				if(jsonMap.getString("text").contains("has joined (index")) {
					return Utils.ChatEventType.USER_JOINED;
				} else if(jsonMap.getString("text").contains("has left (index")){
					return Utils.ChatEventType.USER_LEFT;
				} else {
					return Utils.ChatEventType.ERROR;
				}
			case "get_clients":
				return Utils.ChatEventType.PARTICIPATING_USERS;
			case "info":
				return Utils.ChatEventType.SERVER_INFO;
			case "message":
				if(jsonMap.has("status")) {	//its a status message
					if(jsonMap.getString("status").equals("success")){
						return Utils.ChatEventType.MSG_SUCCESS;
					} else if(jsonMap.getString("status").equals("failure")) {
						return Utils.ChatEventType.MSG_FAILURE;
					} else {
						return Utils.ChatEventType.ERROR;
					}
				} else if(jsonMap.has("text")){
					return Utils.ChatEventType.MSG_BROADCAST;
				} else {
					return Utils.ChatEventType.ERROR;
				}
			case "deregister":
				if(jsonMap.getString("status").equals("success")){
					return Utils.ChatEventType.DEREGISTER_SUCCESS;
				} else if(jsonMap.getString("status").equals("failure")) {
					return Utils.ChatEventType.DEREGISTER_FAILURE;
				} else {
					return Utils.ChatEventType.ERROR;
				}
			case "unknown":
				return Utils.ChatEventType.INVALID_JSON;
			default:
				return Utils.ChatEventType.ERROR;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Utils.ChatEventType.ERROR;
		}

	}
	
	private void initReceiver() {
		listening = true;
		new Thread() {
			public void run() {
				while(listening) {
					try {
						JSONObject in = comm.receiveAnswer();
						ChatEvent e = new ChatEvent(this, parseJSON(in), (in.has("text") ? in.getString("text") : ""), in);
						e.dispatchEvent();
					} catch (IOException | JSONException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}

}
