package ch.ethz.inf.vs.android.nethz.chat;

import java.util.ArrayList;

import ch.ethz.inf.vs.android.nethz.chat.R;
import android.app.ListActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.widget.Button;
import android.widget.EditText;

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
	
	final Handler callbackHandler = new Handler();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

		//Retrieve ChatLogic object from ConnectionActivity
		this.chat = (ChatLogic) getIntent().getSerializableExtra("ChatLogic");
	}
	
	@Override
	public Handler getCallbackHandler() {
		return this.callbackHandler;
	}
	

	public void onBackPressed() {
		// TODO Make sure to deregister when the user presses on Back and to quit the app cleanly.
	}
}
