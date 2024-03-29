package ch.ethz.inf.vs.android.aenz.chat;

import java.util.EventListener;

import ch.ethz.inf.vs.android.aenz.chat.ChatEventSource.ChatEvent;

import android.os.Handler;

/**
 * This class is intercepting events triggered by the logic
 * @author hong-an
 *
 */
public interface ChatEventListener extends EventListener {
	/**
	 * Handler for the events stemming from the chat
	 * logic.
	 */
	final Handler callbackHandler = new Handler();
	
	/**
	 * This functions returns the handler.
	 * @return
	 */
	public Handler getCallbackHandler();
	
	// TODO Add all necessary event triggers
	
	public abstract void onReceiveChatEvent(ChatEvent e);
}
