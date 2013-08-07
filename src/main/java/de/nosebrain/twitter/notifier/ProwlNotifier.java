package de.nosebrain.twitter.notifier;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import twitter4j.Status;
import de.nosebrain.prowl.Notification;
import de.nosebrain.prowl.ProwlClient;

/**
 * {@link Notifier} that notifies the user with {@link ProwlClient}
 * 
 * @author nosebrain
 */
public class ProwlNotifier implements Notifier {
  
  private static final Logger LOG = LoggerFactory.getLogger(ProwlNotifier.class);
  
  private static final ProwlClient CLIENT = new ProwlClient();

  private List<String> apiKeys;

  public ProwlNotifier(final Properties properties) {
    if(properties == null)
      throw new IllegalArgumentException("No properties specified!");
    
    this.apiKeys = Arrays.asList(properties.getProperty("prowl.apikey").split(","));
  }

  /* (non-Javadoc)
   * @see de.nosebrain.twitter.notifier.Notifier#notify(twitter4j.Status)
   */
  @Override
  public void notify(final Status status) {
    final Notification notification = new Notification();
    notification.setApplication("Twitter");
    notification.setEvent(status.getUser().getName());
    notification.setDescription(status.getText());

    try {
      CLIENT.sendNotification(notification, this.apiKeys);
    } catch (final IOException e) {
      LOG.error(e.getMessage(), e);
    }
  }
}
