package ch.ethz.inf.vs.android.aenz.chat;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;

import ch.ethz.inf.vs.android.aenz.chat.ChatEventSource.ChatEvent;
import ch.ethz.inf.vs.android.aenz.chat.Utils.SyncType;
import ch.ethz.inf.vs.android.nethz.chat.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/**
 * This activity is launched at the startup of the app.
 * This handles the registration to the server.
 * @author hong-an
 *
 */
public class RegisterActivity extends ListActivity implements ChatEventListener{
	
	private static final String TAG = "RegisterActivity";
	
	private UDPCommunicator comm; //for testing purposes only
	/**
	 * The login button
	 */
	private Button loginButton;
	
	/**
	 * The send button
	 */
	private Button sendButton;
	
	/**
	 * The text field containing the nethz
	 */
	private EditText nethzText;
	
	/**
	 * The text field containing the optional digits
	 */
	private EditText numberText;
	
	private RadioGroup stateSelect;
	
	private CheckBox rememberMeCheck;
	
	

	/**
	 * The class handling the logic of the chat interactions
	 * with the server.
	 */
	private ChatLogic chat;
	
	/**
	 * This array contains the DisplayMessages to be displayed 
	 * in the main_layout
	 */
	ArrayList<DisplayMessage> displayMessages;
	 
	/**
	 * The adapter for the messages to be displayed
	 */
	DisplayMessageAdapter adapter;
	
	/**
	 * Things needed for the register message
	 */
	private String nethz;
	private String number;
	private SyncType sync;
	private boolean stayLoggedIn;
	
	private boolean listening = false;
	
	/**
	 * This handles the callbacks between the chatLogic and 
	 * the activity
	 */
	final Handler callbackHandler = new Handler();

	/**
	 * This function is called as the activity is created and
	 * will handle its initializations
	 */
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
		.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		setContentView(R.layout.activity_register);
		this.loginButton = (Button) findViewById(R.id.login);
		this.nethzText = (EditText) findViewById(R.id.username);
		this.numberText = (EditText) findViewById(R.id.number);
		this.stateSelect = (RadioGroup) findViewById(R.id.radioGroup);
		this.rememberMeCheck = (CheckBox) findViewById(R.id.remember);

		//TODO is this okay? not really i think
		if(!listening){
			chat = ChatLogic.getInstance(this, null);
			Log.d(TAG, "adding this as Listener");
			chat.addChatEventListener(this);
			listening = true;
		}
		
		
		this.loginButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(haveNetworkConnection()){ //check network connectivity
					
					//read login data from UI
					nethz = nethzText.getText().toString();
					number = numberText.getText().toString();
					stayLoggedIn = rememberMeCheck.isChecked();
	
					//create ChatLogic in correct mode
					if(stateSelect.getCheckedRadioButtonId() == R.id.lamportRadio){
						sync = Utils.SyncType.LAMPORT_SYNC;
						Log.d(TAG, "ChatLogic initialized with LAMPORT");
					} else {
						sync = Utils.SyncType.VECTOR_CLOCK_SYNC;
						Log.d(TAG, "ChatLogic initialized with VECTOR CLOCK");
					}
					
					chat.setSyncType(sync);
					
					try {
						//register as new user
						Log.d(TAG, "Trying to register as: " +nethz + number);
						chat.sendRequest(Utils.jsonRegister(nethz, number));
						
					} catch (IOException e1) {
						e1.printStackTrace();
					} catch (JSONException e1) {
						e1.printStackTrace();
					}
					
				} else{
					errorMessage("No working Internet Connection").show();
				}
				// TO DO: if a connection is available proceed to the
				// registration.
				// Make sure to check display an appropriate error message in an
				// alert if it fails
				// Display an alert with appropriate error message if no
				// connection is available.

			}
		});
	}
	
	private Dialog errorMessage(String text){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("ERROR")
			.setMessage(text)
			.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
				}
			});
		return builder.create();
	}

	/**
	 * This function should check if a network interface is available
	 * and if the device is connected to it
	 * @return boolean indicating if the device is connected to a network interface
	 */
	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager 
			= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	/**
	 * This function should make sure that there is some connectivity.
	 * Think about pinging a website...
	 * @return boolean indicating if the device has Internet access
	 * @throws IOException 
	 */
	private boolean isOnline() {
		boolean available = false;
		try{
			URL url = new URL("http://www.google.com");
			HttpURLConnection urlConnect = (HttpURLConnection) url.openConnection();
			urlConnect.setConnectTimeout(600);
			urlConnect.getContent();
			available = true;
		} catch(NullPointerException np){
			np.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		}
		
		Log.d(TAG, "Google website is available: " + available);
		return available;
	}

	/**
	 * This function verifies that the device has working Internet 
	 * connectivity.
	 * @return boolean indicating if the device has working Internet
	 */
	private boolean haveNetworkConnection() {
		return isNetworkAvailable() && isOnline();
	}

	/**
	 * This function adds messages to the ListActivity.
	 */
	private void addMessage(){
		// TODO Fill me
	}
	/**
	 * This function should be called when the back button is pressed. 
	 * Make sure everything is closed / quit properly.
	 */
	public void onBackPressed() {
		// TODO Make sure to deregister when the user presses on Back and to quit the app cleanly.
		super.onBackPressed();
		try{
			chat.sendRequest(Utils.jsonDeregister());
		} catch (JSONException e){
			e.printStackTrace();
			Log.d(TAG, "JSON: deregistering failed");
		} catch (IOException e) {
			e.printStackTrace();
			Log.d(TAG, "IO: deregistering failed");
		}
	}
	@Override
	/**
	 * This function returns the handler that returns the interaction
	 * between ChatLogic and the activity.
	 */
	public Handler getCallbackHandler() {
		return callbackHandler;
	}

	@Override
	public void onReceiveChatEvent(ChatEvent e) {
		Log.d(TAG, "Event: " + e.getType().toString());
		
		switch(e.getType()){
			case REGISTER_SUCCESS: 
				//initialize lamport and vector
				Log.d(TAG, "null? " + (e.request == null));
				JSONObject response = e.request;
				Lamport lamport = null;
				VectorClock vClock = null;
				
				try {
					//extract and create initial lamport
					lamport = new Lamport(response.getInt("init_lamport"));
					
					//extract and create initial vector clock
					HashMap<Integer, Integer> initVectorClock = Utils.parseVectorClockJSON(response.getJSONObject("init_time_vector"));
					int index = Integer.parseInt(response.getString("index"));
					vClock = new VectorClock(initVectorClock, index);
					
					chat.setTime(lamport, vClock);
					
				} catch (JSONException e1) {
				// TODO Auto-generated catch block
					e1.printStackTrace();
					errorMessage("Extracting initial TimeVector failed").show();
					break;
				}
				
				Intent intent = new Intent(getInstance(), MainActivity.class);
				intent.putExtra("ownNethz", nethz);
				intent.putExtra("ownUsernameNumber", number);
				intent.putExtra("stayLoggedIn", stayLoggedIn);
				startActivity(intent);
				
				break;
			case USERNAME_INVALID:
				errorMessage("You may not be on ETH subnet or trying to " +
						"register with an invalid username").show();
				break;
			case NOT_REGISTERED:
				errorMessage("Registering failed.").show();
				break;
			case ALREADY_REGISTERED:
				errorMessage("You are already registered").show();
				break;
			default:
			break;
		}
	}
	
	private RegisterActivity getInstance(){
		return this;
	}
}
