package de.nosebrain.twitter.notifier;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.Properties;

import twitter4j.FilterQuery;
import twitter4j.Status;
import twitter4j.StatusAdapter;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.PropertyConfiguration;

/**
 * the main class sets up all needed objects and starts streaming tweets
 * 
 * @author nosebrain
 */
public class TwitterNotifier {

  /**
   * @param args
   * @throws IOException
   */
  public static void main(final String[] args) throws Exception {
    
    String location = System.getProperty("user.home") + File.separator 
                                                      +  "twitter-notifier.properties";
    if(args.length > 0)
      location = args[0];

    final Properties properties = new Properties();
    properties.load(new FileInputStream(location));

    final TwitterStream twitterStream = new TwitterStreamFactory(
        new PropertyConfiguration(properties)).getInstance();

    Class<?> clazz = Class.forName(properties.getProperty("notifier"));
    Constructor<?> constructor = clazz.getConstructor(Properties.class);
 
    
    Notifier notifier = null;
    if(constructor != null)
      notifier = (Notifier) constructor.newInstance(properties);
    else {
      constructor = clazz.getConstructor();
      notifier = (Notifier) constructor.newInstance();
    }

    final boolean includeDirectMessages = Boolean.parseBoolean(
                                            properties.getProperty(
                                                "includeDirectMessages", "true"));
    twitterStream.addListener(
        new NotifierStatusAdapter(notifier, includeDirectMessages));

    final String twitterIdsString = properties.getProperty("twitterids");
    final String[] split = twitterIdsString.split(",");

    final long[] twitterIds = new long[split.length];
    for (int i = 0; i < split.length; i++) {
      twitterIds[i] = Long.parseLong(split[i]);
    }

    twitterStream.filter(new FilterQuery(twitterIds));
  }

  public static class NotifierStatusAdapter extends StatusAdapter {
    
    private Notifier notifier;
    private boolean includeDirectMessages;

    public NotifierStatusAdapter(Notifier notifier, boolean includeDirectMessages) {
      
      if(notifier == null)
        throw new IllegalArgumentException("Notifier is mandatory");
      
      this.notifier = notifier;
      this.includeDirectMessages = includeDirectMessages;
    }
    
    @Override
    public void onStatus(Status status) {
      final long userId = status.getInReplyToUserId();
      
      // check if tweet is a direct tweet to the user
      // don't notify the user about this kind of tweets
      if (!includeDirectMessages && (userId != -1)) return;
      
      notifier.notify(status);
    }
  }
}
