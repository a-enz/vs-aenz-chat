package ch.ethz.inf.vs.android.nethz.chat;

import java.util.ArrayList;

import ch.ethz.inf.vs.android.nethz.chat.R;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

/**
 * DisplayMessageAdapter is a Custom class to implement custom row in ListView
 * 
 * @author Adil Soomro
 *
 */
public class DisplayMessageAdapter extends BaseAdapter {
	/**
	 * The context of the activity
	 */
	private Context mContext;
	
	/**
	 * ArrayList that contains the DisplayMessages
	 */
	private ArrayList<DisplayMessage> mDisplayMessages;

	/**
	 * Constructor
	 * @param context
	 * @param displayMessages
	 */
	public DisplayMessageAdapter(Context context,
			ArrayList<DisplayMessage> displayMessages) {
		super();
		this.mContext = context;
		this.mDisplayMessages = displayMessages;
	}

	@Override
	/**
	 * Getter for the number of messages in the list
	 */
	public int getCount() {
		return mDisplayMessages.size();
	}

	@Override
	/**
	 * Retrieving a message at a given position in the list
	 */
	public Object getItem(int position) {
		return mDisplayMessages.get(position);
	}

	@SuppressWarnings("deprecation")
	@Override
	/**
	 * Function to retrieve the current view of messages
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		DisplayMessage displayMessage = (DisplayMessage) this.getItem(position);

		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.message_row, parent, false);
			holder.message = (TextView) convertView
					.findViewById(R.id.message_text);
			convertView.setTag(holder);
		} else
			holder = (ViewHolder) convertView.getTag();

		holder.message.setText(displayMessage.getMessage() + "\nFrom: " + displayMessage.getUsername() + "\n" + displayMessage.getDate());

		LayoutParams lp = (LayoutParams) holder.message.getLayoutParams();
		// check if it is a status message then remove background, and change
		// text color.
		if (displayMessage.isStatusMessage()) {
			holder.message.setBackgroundDrawable(null);
			lp.gravity = Gravity.LEFT;
			holder.message.setTextColor(R.color.textFieldColor);
		} else {
			// Check whether message is mine to show green background and align
			// to right
			if (displayMessage.isMine()) {
				holder.message
						.setBackgroundResource(R.drawable.speech_bubble_green);
				lp.gravity = Gravity.RIGHT;
			}
			// If not mine then it is from sender to show orange background and
			// align to left
			else {
				holder.message
						.setBackgroundResource(R.drawable.speech_bubble_orange);
				lp.gravity = Gravity.LEFT;
			}
			holder.message.setLayoutParams(lp);
			holder.message.setTextColor(R.color.textColor);
		}
		return convertView;
	}
	
	private static class ViewHolder {
		TextView message;
	}

	@Override
	/**
	 * Not used
	 */
	public long getItemId(int position) {
		// Unimplemented, because we aren't using Sqlite.
		return position;
	}

}
