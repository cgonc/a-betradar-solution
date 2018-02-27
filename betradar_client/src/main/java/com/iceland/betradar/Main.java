package com.iceland.betradar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iceland.betradar.listeners.LcooFeedListenerImpl;
import com.iceland.betradar.listeners.LiveOddsMatchFeedListenerImpl;
import com.sportradar.sdk.common.exceptions.SdkException;
import com.sportradar.sdk.feed.lcoo.interfaces.LcooFeed;
import com.sportradar.sdk.feed.lcoo.interfaces.LcooFeedListener;
import com.sportradar.sdk.feed.liveodds.interfaces.LiveOddsFeed;
import com.sportradar.sdk.feed.liveodds.interfaces.LiveOddsFeedListener;
import com.sportradar.sdk.feed.sdk.Sdk;

public class Main {

    private final static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws SdkException {
        final Sdk sdk = Sdk.getInstance();
        final LiveOddsFeed liveOddsFeed = sdk.getLiveOdds();
        final LiveOddsFeedListener oddsMatchFeedListener = new LiveOddsMatchFeedListenerImpl();
        final LcooFeed lcooFeed = sdk.getLcoo();
        final LcooFeedListener lcooFeedListener = new LcooFeedListenerImpl();
        if (liveOddsFeed != null) {
            liveOddsFeed.open(oddsMatchFeedListener);
        }
        if (lcooFeed != null) {
            lcooFeed.open(lcooFeedListener);
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        logger.info("ICELAND BETRADAR CLIENT IS RUNNING. HIT ANY KEY TO EXIT");
        try {
            reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info("CLOSING THE CLIENT");
        sdk.close();
        logger.info("EXIT");
    }
}
