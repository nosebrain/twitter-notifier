package de.nosebrain.twitter.notifier;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import twitter4j.FilterQuery;
import twitter4j.Status;
import twitter4j.StatusAdapter;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.PropertyConfiguration;

/**
 * the main class
 * sets up all needed objects and starts streaming tweets
 * @author nosebrain
 */
public class TwitterNotifier {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(final String[] args) throws Exception {
	    final String location = System.getProperty("user.home") + "/twitter-notifier.properties";
		final InputStream config = new FileInputStream(location);
	    final Properties properties = new Properties();
	    properties.load(config);
		final Configuration conf = new PropertyConfiguration(properties);
		final TwitterStream twitterStream = new TwitterStreamFactory(conf).getInstance();
	    
	    final Notifier notifier = (Notifier) Class.forName(properties.getProperty("notifier")).newInstance();
	    notifier.setupNotifier(properties);
	    twitterStream.addListener(new StatusAdapter() {
			@Override
			public void onStatus(final Status status) {
				notifier.notify(status);
			}
		});
	    
	    final String twitterIdsString = properties.getProperty("twitterids");
	    final String[] split = twitterIdsString.split(",");
	    
	    final long[] twitterIds = new long[split.length];
		for (int i = 0; i < split.length; i++) {
			twitterIds[i] = Long.parseLong(split[i]);
		}
	    
	    twitterStream.filter(new FilterQuery(twitterIds));
	}

}
