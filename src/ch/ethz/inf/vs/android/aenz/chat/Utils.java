package ch.ethz.inf.vs.android.aenz.chat;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/**
 * This class contains all necessary constants and functions
 * that should be made available accros the code
 * @author hong-an
 *
 */
public class Utils {
	
	public final static String TAG = "Utils";
	/**
	 * Path to the log file
	 */
	public final static String LOG_PATH = "chat_log.txt";
	
	//private static final ChatLogic chatLogic = new 
	

	/**
	 * Useful for differentiating between selecting
	 * Lamport timestamps and vector clocks
	 * @author hong-an
	 *
	 */
	public enum SyncType {
		LAMPORT_SYNC, VECTOR_CLOCK_SYNC;
	}

	/*
	 * Change me... Some useful constants
	 */
	public final static String SERVER_ADDRESS = "129.132.75.194";
	public final static int SERVER_PORT = 4999;
	public final static int RECEIVE_BUFFER_SIZE = 4096;
	public final static int SOCKET_TIMEOUT = -1;
	public final static int RESPONSE_TIMEOUT = -1;
	public final static int MESSAGE_TIMEOUT = -1;

	// TODO Fill me with macros for the states
	
	public static JSONObject jsonRegister(String nethz, String number) throws JSONException{
		String str =  "{\"cmd\" : \"register\"," +
						"\"user\" : \"" + nethz + number + "\"}";
		return new JSONObject(str);
	}

	public static JSONObject jsonGetClients() throws JSONException{
		String str = "{\"cmd\" : \"get_clients\"}";
		return new JSONObject(str);
	}
	
	public static JSONObject jsonInfo() throws JSONException{
		String str = "{\"cmd\" : \"info\"}";
		return new JSONObject(str);
	}
	
	public static JSONObject jsonMessage(String text, VectorClock time_vector, Lamport lamport) throws JSONException{
		String str = "{\"cmd\" : \"message\"," +
					"\"text\" : \"" + text + "\", " +
//					"\"time_vector\" : " + time_vector.convertToJSON().toString() + "," +
					"\"lamport\" : " + lamport.toString() + "}";
		JSONObject result = new JSONObject(str);
		result.put("time_vector", time_vector.convertToJSON());
		return result;
	}
	
	public static JSONObject jsonDeregister() throws JSONException{
		String str = "{\"cmd\" : \"deregister\"}";
		return new JSONObject(str);
	}
	
	
	
	/** This function retrieve the current time and formats it
	 * @return Time in the appropriate format
	 */
    public static String getTime(){
    	Calendar cal = Calendar.getInstance();
    	cal.getTime();
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
    	return sdf.format(cal.getTime());
    }
	/**
	 * Useful for determining the state machine for
	 * handling the interactions with the server.
	 * Look at all JSON examples in the exercise sheet.
	 * @author hong-an
	 *
	 */
	public enum ChatEventType {
		REGISTER_SUCCESS,
		USERNAME_INVALID,
		NOT_REGISTERED,
		ALREADY_REGISTERED,
		USER_JOINED,
		USER_LEFT,
		PARTICIPATING_USERS,
		SERVER_INFO,
		MSG_SUCCESS,		//your msg was sent successful
		MSG_FAILURE, 		//your msg did not arrive
		MSG_BROADCAST,		//broadcast msg arrived
		DEREGISTER_SUCCESS,
		DEREGISTER_FAILURE,
		INVALID_JSON,
		ERROR,				//u fucked up
		WE_SEND				//we send a message
	}

	/**
	 * This function is used to parse VectorClocks from messages received.
	 * @param json The JSON received from the server ("time_vector" or "initial_time_vector" field)
	 * @return The data in the right format
	 * @throws JSONException 
	 */
	public static HashMap<Integer, Integer> parseVectorClockJSON(JSONObject json) throws JSONException {
		// TODO Fill me
		Log.d(TAG, "vector successfuly extracted. Length: " + json.length());
		HashMap<Integer, Integer> result = new HashMap<Integer, Integer>();
		Iterator<String> indexIter = json.keys();
		String index;
		
		while(indexIter.hasNext()){
			
			index = indexIter.next();
			
			result.put(Integer.decode(index), json.getInt(index));
		}
		return result;
	}

	/**
	 * This function is used to parse the clients from messages received.
	 * @param json The JSON received from the server ("client" field)
	 * @return The data in the right format
	 * @throws JSONException 
	 */
	public static HashMap<Integer, String> parseClientsJSON(JSONObject json) throws JSONException {
		// TODO Fill me
		HashMap<Integer, String> result = new HashMap<Integer, String>();
		
		Iterator<String> indexIter = json.keys();
		String index;
		
		while(indexIter.hasNext()){
			index = indexIter.next();
			
			result.put(Integer.decode(index), json.getString(index));
		}		
		return result;
	}
}
