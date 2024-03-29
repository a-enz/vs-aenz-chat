package ch.ethz.inf.vs.android.aenz.chat;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map.Entry;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/**
 * This class should be used to handle vector clocks
 * 
 * @author hong-an
 *
 */
public class VectorClock implements Comparable<VectorClock>, Serializable{
	/**
	 * This should contain the vector clock
	 */
	private HashMap<Integer, Integer> clock;
	
	/**
	 * This keeps track of the owner's index
	 */
	private int ownIndex;
	
	private static final String TAG = "VectorClock";
	
	/**
	 * Constructor
	 */
	public VectorClock() {
		this.clock = new HashMap<Integer, Integer>();
		this.ownIndex = -1;
	}
	
	/**
	 * Constructor
	 * @param initClock
	 * @param initIndex
	 */
	public VectorClock(HashMap<Integer, Integer> initClock, int initIndex) {
		this.clock = initClock;
		this.ownIndex = initIndex;
	}

	/**
	 * This converts the VectorClock to the appropriate JSON format
	 * @return
	 * @throws JSONException 
	 */
	public JSONObject convertToJSON() throws JSONException {
		String result = "{";
		int count = 1;
		int size = clock.size();
		
		for (Entry<Integer,Integer> e : clock.entrySet()){
			result += "\"" + e.getKey() + "\": " + e.getValue();
			if(count != size) result += ", ";
			count++;
		}
		
		result += "}";
		
		Log.d(TAG, result);
		
		// TODO Fill me
		return new JSONObject(result);
	}
	
	/**
	 * Use this to update the current vector clock to the newly received
	 * one
	 * @param toCompare The VectorClock that ours should be compared to
	 */
	public void update(VectorClock toCompare) {

		// TODO Fill me
	}

	@Override
	/**
	 * This function allows to compare VectorClocks
	 * @param another The VectorClock that ours should be compared to
	 */
	public int compareTo(VectorClock toCompare) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	/**
	 * This function should return a String representation of the
	 * VectorClock
	 * @return String that represents tha VectorClock
	 */
	public String toString(){
		return "Vector Clock" + clock.toString();
	}
	
	public int getOwnId(){
		return ownIndex;
	}
}
