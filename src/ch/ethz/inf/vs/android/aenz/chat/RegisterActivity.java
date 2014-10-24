package ch.ethz.inf.vs.android.aenz.chat;

import java.util.ArrayList;

import ch.ethz.inf.vs.android.nethz.chat.R;
import android.app.ListActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
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

		this.loginButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TO DO: if a connection is available proceed to the
				// registration.
				// Make sure to check display an appropriate error message in an
				// alert if it fails
				// Display an alert with appropriate error message if no
				// connection is available.
			}
		});
	}

	/**
	 * This function should check if a network interface is available
	 * and if the device is connected to it
	 * @return boolean indicating if the device is connected to a network interface
	 */
	private boolean isNetworkAvailable() {
		// TODO: Fill me
		return false;
	}

	/**
	 * This function should make sure that there is some connectivity.
	 * Think about pinging a website...
	 * @return boolean indicating if the device has Internet access
	 */
	private boolean isOnline() {
		// TODO: Fill me
		return false;
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
