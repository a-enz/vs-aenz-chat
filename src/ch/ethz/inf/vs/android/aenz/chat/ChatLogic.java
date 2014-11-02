package ch.ethz.inf.vs.android.aenz.chat;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
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
public class ChatLogic extends ChatEventSource implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2289981761365579252L;

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
	private Handler receiveHandler = new Handler(new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			ChatEvent e = (ChatEvent) msg.obj;
			e.dispatchEvent();
			return true;
		}
	});

	private static ChatLogic singleton;
	/**
	 * This object handles the UDP communication between the client and the chat
	 * server
	 */
	UDPCommunicator comm;
	
	private SyncType sync;
	
	/**
	 * This object should be used to log 
	 * deliverable messages.
	 */
	Logger log;
	
	private boolean listening;
	
	//concurrency issues
	private Lamport lamport;

	private ArrayList<ChatMessage> lamportBuffer = new ArrayList<ChatMessage>();
	
	private Lamport bufferClock;	//timestamp were waiting for
	
	private int id;

	private VectorClock vectorClock;
	
	private String userName;
	
	/**
	 * This function should initialize the logger as
	 * soon as the username is registered.
	 * @param username
	 */
	public void initLogger(String username) {
		this.log = new Logger(username, appContext);
	}
	
	/**
	 * returns the ChatLogic singleton
	 * if the chatlogic has already been createt, the parameter will have no effect
	 * @param context
	 * @param sync
	 * @deprecated
	 * @return
	 */
	public static ChatLogic getInstance(Context context, SyncType sync) {
		return (singleton == null ? (singleton = new ChatLogic(context, sync, "deprecated")) : singleton);
	}
	
	public static ChatLogic getInstance(Context context, SyncType sync, String userName){
		return (singleton == null ? (singleton = new ChatLogic(context, sync, userName)) : singleton);
	}
	
	//use this in registry
	public void setTime(Lamport lamport, VectorClock vectorClock) {
		this.lamport = lamport;
		this.vectorClock = vectorClock;
		this.id = vectorClock.getOwnId();
	}
	
	
	public void setSyncType(SyncType sync){
		this.sync = sync;
	}
	
	public void close() {
		listening = false;
		comm.close();
	}

	/**
	 * Constructor
	 * @param context Context of the Activity
	 * @param sync Indicates whether Lamport timestamps or Vector clocks should be used
	 */
	private ChatLogic(Context context, SyncType sync, String userName) {
		listening = false;
		comm = new UDPCommunicator();
		this.sync = sync;
		this.userName = userName;
		initLogger(userName);
		initReceiver();
	}
	
	/**
	 * 
	 * @param request
	 * @throws IOException
	 * @deprecated
	 */
	public void sendRequest(JSONObject request) throws IOException {
		//log.logReadyMsg(msg, isIncoming)
		comm.sendRequest(request);
	}
	
	public void sendRequest(ChatMessage message) throws IOException, JSONException{
		lamportBuffer.add(message.getLamportTime().getValue()-bufferClock.getValue(), message);
		comm.sendRequest(Utils.jsonMessage(message.getText(), message.getVectorTime(), message.getLamportTime()));
		log.logReadyMsg(message, false);
	}
	
	
	//Use this to get lamport time
	public Lamport tagLamport(){
		lamport.tick();
		return lamport;
	}

	/**
	 * This function should parse incoming JSON packets and trigger
	 * the necessary events.
	 * @param jsonMap Incoming JSON packet
	 * @return The type of event that took place.
	 * @throws JSONException
	 */
	public ChatEventType parseJSON(JSONObject jsonMap){
		// TODO : PLEAS GIF CHATMESSAGE
		// set it null if it is an error or a notification etc
		
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
	
	private ChatInteraction generateChatInteraction(ChatEventType type, JSONObject json) {
		return null;
	}
	
	private void initReceiver() {
		listening = true;
		new Thread() {
			private final String TAG = "Receiver";
			
			private final Handler handler = new Handler();
			
			private final Runnable timeout = new Runnable() {
				@Override
				public void run(){
					if(!lamportBuffer.isEmpty()){
						lamportBuffer.remove(0);
						bufferClock.tick();
					}
				}
			};
			
			/**
			 * Decides whether the incoming broadcast message is delivered instantly as event or buffered
			 * @param message
			 * @throws JSONException
			 */
			
			private void bufferOrDispatch(ChatMessage message) throws JSONException {
				int relation = message.getLamportTime().compareTo(bufferClock);
				int pos;
				Iterator<ChatMessage> cursor;
				
				if(relation >= 0) {	//the message we are waiting for or message
					pos = message.getLamportTime().getValue() - bufferClock.getValue();
					lamportBuffer.ensureCapacity(pos+1);
					lamportBuffer.add(pos, message);
					ChatMessage current;
					refreshTimer();
					while((current = lamportBuffer.remove(0)) != null){
						if(current.getSender() != id){
							ChatEvent e = new ChatEvent(this, Utils.ChatEventType.MSG_BROADCAST, null);
							Message msg = Message.obtain();
							msg.obj = e;
							bufferClock.tick();
							receiveHandler.sendMessage(msg);
							log.logReadyMsg(current, true);
						}
					}
				} else { //a message which should have come way earlier, display anyway
					ChatEvent e = new ChatEvent(this, Utils.ChatEventType.MSG_BROADCAST, message);
					Message msg = Message.obtain();
					msg.obj = e;
					receiveHandler.sendMessage(msg);
				}
			}
			
			private void refreshTimer() {
				handler.removeCallbacks(timeout);
				handler.postDelayed(timeout, Utils.MESSAGE_TIMEOUT);
			}
			
			public void run() {
				while(listening) {
					try {
						JSONObject in = comm.receiveAnswer();
						Log.d(TAG, "Packet received: " + in.getString("cmd"));
						
						//creating a chatmessage
						ChatMessage chatMessage;
						ChatEventType chatState = parseJSON(in);
						if(chatState == Utils.ChatEventType.MSG_BROADCAST){
							//fill chat message
							int sender = in.getInt("sender");
							String text = in.getString("text");
							Lamport lamport = new Lamport(in.getInt("lamport"));
							HashMap<Integer,Integer> vectorValues = Utils.parseVectorClockJSON(in.getJSONObject("time_vector"));
							VectorClock vector = new VectorClock(vectorValues, sender); //set the index of this vectorclock to sender ID
							
							chatMessage = new ChatMessage(chatState, //broadcast only
															sender, 
															text, 
															lamport, 
															vector, 
															System.currentTimeMillis(), 
															sync);
							
							lamport.update(chatMessage.getLamportTime());
							bufferOrDispatch(chatMessage);
						} else {
							ChatEvent e = new ChatEvent(this, chatState, null);
							Message msg = Message.obtain();
							msg.obj = e;
							receiveHandler.sendMessage(msg);
						}
						
					} catch (IOException | JSONException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}
	

}
