package ch.ethz.inf.vs.android.aenz.chat;

/**
 * This class should be used for abstracting Lamport timestamps
 * @author hong-an
 *
 */
public class Lamport implements Comparable<Lamport> {
	/**
	 * The timestamp
	 */
	private int value;

	/**
	 * Constructor
	 */
	public Lamport() {
		this.value = -1;
	}

	/**
	 * Setter for the timestamp
	 * @param initValue The value
	 */
	public Lamport(int initValue) {
		this.value = initValue;
	}

	/**
	 * This function updates our own timestamp
	 * to the newly received one
	 * @param toCompare The newly received Lamport timestamp
	 */
	public void update(Lamport toCompare) {
		if (this.value < toCompare.value)
			this.value = toCompare.value;
	}

	@Override
	/**
	 * This function compares two Lamport timestamps.
	 * @param toCompare The newly received Lamport timestamp
	 */
	public int compareTo(Lamport toCompare) {
		int result = 0;
		if(this.value < toCompare.value) {
			result = -1;
		} else if (this.value > toCompare.value) {
			result = 1;
		}
		return result;
	}

	@Override
	/**
	 * This function returns the String representation of the
	 * class
	 * @return String representation of Lamport timestamps
	 */
	public String toString() {
		return "Lamport: " + Integer.toString(value);
	}
}
