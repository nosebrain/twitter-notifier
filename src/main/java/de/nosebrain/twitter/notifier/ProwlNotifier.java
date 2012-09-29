package de.nosebrain.twitter.notifier;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import twitter4j.Status;
import de.nosebrain.prowl.Notification;
import de.nosebrain.prowl.ProwlClient;

/**
 * {@link Notifier} that notifies the user with {@link ProwlClient}
 * 
 * @author nosebrain
 */
public class ProwlNotifier implements Notifier {
  private static final ProwlClient CLIENT = new ProwlClient();

  private List<String> apiKeys;

  @Override
  public void setupNotifier(final Properties properties) {
    this.apiKeys = Arrays.asList(properties.getProperty("prowl.apikey").split(","));
  }

  @Override
  public void notify(final Status status) {
    final Notification notification = new Notification();
    notification.setApplication("Twitter");
    notification.setEvent(status.getUser().getName());
    notification.setDescription(status.getText());

    try {
      CLIENT.sendNotification(notification, this.apiKeys);
    } catch (final IOException e) {
      e.printStackTrace();
    }
  }
}
