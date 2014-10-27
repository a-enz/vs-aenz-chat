package ch.ethz.inf.vs.android.aenz.chat;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import ch.ethz.inf.vs.android.nethz.chat.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

/**
 * This activity is launched at the startup of the app.
 * This handles the registration to the server.
 * @author hong-an
 *
 */
public class RegisterActivity extends ListActivity implements ChatEventListener{
	// TODO add more... Look at activity_register.xml
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

		// TODO: Verify that a connection is available and proceed to register.
		comm = new UDPCommunicator();

		this.loginButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(haveNetworkConnection()){
						
					try {
						comm.sendRequest(Utils.jsonRegister("aenz", "9"));
						JSONObject response = comm.receiveAnswer();
						comm.sendRequest(Utils.jsonGetClients());
						JSONObject clientResp = comm.receiveAnswer();
						
						comm.sendRequest(Utils.jsonDeregister());
						
						Log.d(TAG, "answer successfuly received" + response.toString());
						JSONObject vector = response.getJSONObject("init_time_vector");
						
						Log.d(TAG, "answer successfuly received" + clientResp.toString());
						JSONObject clientVec = clientResp.getJSONObject("clients");
						
						HashMap<Integer, Integer> vMap = Utils.parseVectorClockJSON(vector);
						HashMap<Integer, String> cMap = Utils.parseClientsJSON(clientVec);
						VectorClock vClock = new VectorClock(vMap, response.getInt("index"));
						Log.d(TAG, vClock.toString());
						Log.d(TAG, cMap.toString());
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						Log.d(TAG, "something with udp connection went wrong");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						Log.d(TAG, "creating json failed");
					}
				} else{
					
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
		return null;
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
		try {
			boolean available = InetAddress.getByName("8.8.8.8").isReachable(1000);
			Log.d(TAG, "google dns reachable");
			return available;
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
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
	}
	@Override
	/**
	 * This function returns the handler that returns the interaction
	 * between ChatLogic and the activity.
	 */
	public Handler getCallbackHandler() {
		return callbackHandler;
	}
}
