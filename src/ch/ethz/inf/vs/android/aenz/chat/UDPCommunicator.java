package ch.ethz.inf.vs.android.aenz.chat;

import org.json.JSONObject;

/**
 * This class should be used to interface with the server
 * using UDP
 * @author hong-an
 *
 */
public class UDPCommunicator {
	// TODO: Add the necessary objects

	/**
	 * Constructor
	 */
	public UDPCommunicator() {
		setupConnection();
	}

	/**
	 * This function should be used to setup the "connection" to the server
	 * Not crucial in task 1, but in task 2, the port should be bound.
	 * @return
	 */
	public boolean setupConnection() {
		// TODO Setup the connection with the server and make sure to bind the
		// socket
		return false;
	}

	/**
	 * This function should be used to send a request to the server
	 * @param request The request in JSON format
	 */
	public void sendRequest(JSONObject request) {
		// TODO Implement sending the JSONObject to the server
	}
}
