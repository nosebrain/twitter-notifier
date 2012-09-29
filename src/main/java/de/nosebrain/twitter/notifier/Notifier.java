package de.nosebrain.twitter.notifier;

import java.util.Properties;

import twitter4j.Status;

/**
 * an interface for all notifiers to implement
 * 
 * @author nosebrain
 */
public interface Notifier {

	/**
	 * notify the user about a new status
	 * @param status
	 */
	public void notify(Status status);

	/**
	 * set up the notifier
	 * @param properties
	 */
	public void setupNotifier(final Properties properties);

}
