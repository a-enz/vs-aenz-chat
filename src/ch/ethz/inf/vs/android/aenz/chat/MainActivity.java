package ch.ethz.inf.vs.android.aenz.chat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import ch.ethz.inf.vs.android.aenz.chat.ChatEventSource.ChatEvent;
import ch.ethz.inf.vs.android.aenz.chat.Utils.ChatEventType;
import ch.ethz.inf.vs.android.aenz.chat.Utils.SyncType;
import ch.ethz.inf.vs.android.nethz.chat.R;
import android.app.ListActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class MainActivity extends ListActivity implements ChatEventListener {
	private ChatLogic chat;
	ArrayList<DisplayMessage> displayMessages;
	//Not needed i guess
	//SyncType sync;
	DisplayMessageAdapter adapter;
	EditText text;
	String sender;
	String ownUsername;
	String ownNethz;
	String ownUsernameNumber;
	boolean stayLoggedIn;
	
	Button loginButton;
	EditText nethzText;
	EditText numberText;
	/*
	VectorClock vecClock;
	Lamport lamport;
	*/
	HashMap<Integer,String> userList;
	
	final Handler callbackHandler = new Handler();
	
	private static final String TAG = "Main Activity";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Bundle extras = getIntent().getExtras();
        if (extras == null){
        	//oh boy fucked up
        	Log.d(TAG, "extras not received");
        } else {
        	this.ownNethz = extras.getString("ownNethz");
        	this.ownUsernameNumber = extras.getString("ownUsernameNumber");
        	this.ownUsername = this.ownNethz + this.ownUsernameNumber;
        	this.stayLoggedIn = extras.getBoolean("stayLoggedIn");
        	//Retrieve ChatLogic object from ConnectionActivity
        	chat = ChatLogic.getInstance(this, null,ownUsername); //sync should be declared already so it doesn't matter
        }
        /*
        lamport = chat.tagLamport();
        HashMap<Integer,Integer> temp = new HashMap<Integer,Integer>();
        vecClock = new VectorClock(temp, 007);
        */
	}
	
	public void fetchTheUserList() {
		try {
			chat.sendRequest(Utils.jsonGetClients());
		} catch (IOException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public String whoIs(int identifier) {
		String name = userList.get(identifier);
		if (name == null || name.isEmpty()) {
			fetchTheUserList();
			name = Integer.toString(identifier);
		}
		return name;
	}
	
	
	public void sendMessage(View v){
		text = ((EditText) findViewById(R.id.text));
		String text_to_send = text.getText().toString();
		int senderNumber = Integer.parseInt(this.ownUsernameNumber);
		ChatMessage message = null;
		if(!text_to_send.isEmpty()) {
			message = new ChatMessage(Utils.ChatEventType.WE_SEND,senderNumber,text_to_send,chat.tagLamport(),new VectorClock(), 007,chat.getSyncType());
			try {
				chat.sendRequest(message);
			} catch (IOException | JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		DisplayMessage displ = new DisplayMessage(text_to_send, ownUsername, true);
		adapter.add(displ);
		
		ListView mListView = (ListView) findViewById(android.R.id.list);
		mListView.setAdapter(adapter);
	}
	
	@Override
	public Handler getCallbackHandler() {
		return this.callbackHandler;
	}
	

	public void onBackPressed() {
		super.onBackPressed();
		try {
			chat.sendRequest(Utils.jsonDeregister());
		} catch (IOException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onReceiveChatEvent(ChatEvent e) {
		// Update the ListView
		if (e.getType() == Utils.ChatEventType.USER_JOINED) {
			registerNewUser(e.request);
		} else if (e.getType() == Utils.ChatEventType.USER_LEFT) {
			// is it okay if I just let it like this?
		} else if (e.getType() == Utils.ChatEventType.PARTICIPATING_USERS) {
			try {
				userList = Utils.parseClientsJSON(e.request);
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} else if (e.getType() == Utils.ChatEventType.MSG_BROADCAST) {
			boolean me = false;
			String text = e.chatMessage.getText();
			int identifier = e.chatMessage.getSender();
			String name;
			if (identifier == Integer.parseInt(ownUsernameNumber)){
				me = true;
				name = ownUsername; // Is this correct or should it just be the nethz?
			} else {
				name = whoIs(identifier);
			}
			DisplayMessage displ = new DisplayMessage(text, name, me);
			adapter.add(displ);
			
			ListView mListView = (ListView) findViewById(android.R.id.list);
			mListView.setAdapter(adapter);
		}
	}
	/*
	 * Parse the join into usable data
	 */
	public void registerNewUser(JSONObject object){
		if (object != null) {
			try {
				if (object.getString("cmd") == "notification") {
					String text = object.getString("text");
					String name = text.substring(0, text.indexOf(" "));
					String index = text.substring(text.indexOf("("),text.indexOf(""));
					index = index.substring(index.indexOf(" "));
					userList.put(Integer.valueOf(index), name);
				}
			} catch (NumberFormatException | JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
