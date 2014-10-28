package ch.ethz.inf.vs.android.aenz.chat;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONException;

import ch.ethz.inf.vs.android.aenz.chat.ChatEventSource.ChatEvent;
import ch.ethz.inf.vs.android.nethz.chat.R;
import android.app.ListActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class MainActivity extends ListActivity implements ChatEventListener {
	private ChatLogic chat;
	ArrayList<DisplayMessage> displayMessages;
	DisplayMessageAdapter adapter;
	EditText text;
	String sender;
	String ownUsername;
	String ownNethz;
	String ownUsernameNumber;
	
	Button loginButton;
	EditText nethzText;
	EditText numberText;
	
	VectorClock vecClock;
	Lamport lamport;
	
	final Handler callbackHandler = new Handler();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Bundle extras = getIntent().getExtras();
        if (extras == null){
        	//oh boy fucked up
        } else {
        	this.ownNethz = extras.getString(ownNethz);
        	this.ownUsernameNumber = extras.getString(ownUsernameNumber);
        	//Retrieve ChatLogic object from ConnectionActivity
        	this.chat = (ChatLogic) getIntent().getSerializableExtra("ChatLogic");
        }
	}
	
	public void sendMessage() {
		try {
			text = ((EditText) findViewById(R.id.text));
			String text_to_send = text.getText().toString();
			if(!text_to_send.isEmpty())
				chat.sendRequest(Utils.jsonMessage(text_to_send, vecClock, lamport));
		} catch (IOException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public Handler getCallbackHandler() {
		return this.callbackHandler;
	}
	

	public void onBackPressed() {
		try {
			chat.sendRequest(Utils.jsonDeregister());
		} catch (IOException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onReceiveChatEvent(ChatEvent e) {
		// TODO : Update the ListView
		// but where are the messages?
		ListView mListView = (ListView) findViewById(android.R.id.list);
		mListView.setAdapter(adapter);
		//
	}
}
