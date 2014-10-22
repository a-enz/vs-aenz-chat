package ch.ethz.inf.vs.android.nethz.chat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import org.json.JSONObject;

/**
 * This class contains all necessary constants and functions
 * that should be made available accros the code
 * @author hong-an
 *
 */
public class Utils {
	/**
	 * Path to the log file
	 */
	public final static String LOG_PATH = "chat_log.txt";

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
	public final static String SERVER_ADDRESS = "";
	public final static int SERVER_PORT = -1;
	public final static int RECEIVE_BUFFER_SIZE = -1;
	public final static int SOCKET_TIMEOUT = -1;
	public final static int RESPONSE_TIMEOUT = -1;
	public final static int MESSAGE_TIMEOUT = -1;

	// TODO Fill me with macros for the states

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
		SOME_STATE;
	}

	/**
	 * This function is used to parse VectorClocks from messages received.
	 * @param json The JSON received from the server
	 * @return The data in the right format
	 */
	public static HashMap<Integer, Integer> parseVectorClockJSON(JSONObject json) {
		// TODO Fill me

		HashMap<Integer, Integer> result = new HashMap<Integer, Integer>();
		return result;
	}

	/**
	 * This function is used to parse the clients from messages received.
	 * @param json The JSON received from the server
	 * @return The data in the right format
	 */
	public static HashMap<Integer, String> parseClientsJSON(JSONObject json) {
		// TODO Fill me

		HashMap<Integer, String> result = new HashMap<Integer, String>();
		return result;
	}
}
