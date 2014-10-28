package ch.ethz.inf.vs.android.aenz.chat;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.Charset;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/**
 * This class should be used to interface with the server
 * using UDP
 * @author hong-an
 *
 */

public class UDPCommunicator {
	// TODO: Add the necessary objects
	private static final String TAG = "UDPCommunicator";
	
	private DatagramSocket udpSocket;

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
	 * @throws SocketException 
	 */
	public boolean setupConnection() {
		// TODO Setup the connection with the server and make sure to bind the
		// socket
		try{
			udpSocket = new DatagramSocket();//already binds to some free port
			udpSocket.connect(InetAddress.getByName(Utils.SERVER_ADDRESS), Utils.SERVER_PORT);
		} catch (SocketException e){
			e.printStackTrace();
			Log.d(TAG, "UDP Socket initialization error");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return udpSocket.isConnected() && udpSocket.isBound();
	}

	/**
	 * This function should be used to send a request to the server
	 * @param request The request in JSON format
	 * @throws IOException 
	 */
	public void sendRequest(JSONObject request) throws IOException {
		byte[] payload = request.toString().getBytes(Charset.defaultCharset());
		
		DatagramPacket packet = new DatagramPacket(payload, payload.length);
		udpSocket.send(packet);
	}
	
	public JSONObject receiveAnswer() throws IOException {
		byte[] buffer = new byte[Utils.RECEIVE_BUFFER_SIZE];
		
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
		udpSocket.receive(packet);
		
		String response = new String(packet.getData(), Charset.defaultCharset());
		
		JSONObject jsonResponse = null;
		try{
			jsonResponse = new JSONObject(response);
		} catch (JSONException e){
			e.printStackTrace();
		}
		return jsonResponse;
	}
	
	public void close() {
		udpSocket.close();
	}
}
