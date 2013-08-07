package de.nosebrain.twitter.notifier;

import twitter4j.Status;

/**
 * an interface for all notifiers to implement
 * 
 * @author nosebrain
 */
public interface Notifier {

  /**
   * notify the user about a new status
   * 
   * @param status
   */
  public void notify(Status status);
}
