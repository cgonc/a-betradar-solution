package example;

import com.sportradar.sdk.common.exceptions.SdkException;
import com.sportradar.sdk.feed.lcoo.interfaces.LcooFeed;
import com.sportradar.sdk.feed.lcoo.interfaces.LcooFeedListener;
import com.sportradar.sdk.feed.liveodds.interfaces.LiveOddsFeed;
import com.sportradar.sdk.feed.liveodds.interfaces.LiveOddsFeedListener;
import com.sportradar.sdk.feed.liveodds.interfaces.RaceFeed;
import com.sportradar.sdk.feed.liveodds.interfaces.RaceFeedListener;
import com.sportradar.sdk.feed.liveodds.interfaces.outrights.LiveOddsWithOutrightsFeed;
import com.sportradar.sdk.feed.liveodds.interfaces.outrights.LiveOddsWithOutrightsListener;
import com.sportradar.sdk.feed.livescout.interfaces.LiveScoutFeed;
import com.sportradar.sdk.feed.livescout.interfaces.LiveScoutFeedListener;
import com.sportradar.sdk.feed.oddscreator.entities.IdNameEntity;
import com.sportradar.sdk.feed.oddscreator.exceptions.OddsCreatorException;
import com.sportradar.sdk.feed.oddscreator.interfaces.OddsCreatorFeed;
import com.sportradar.sdk.feed.sdk.Sdk;
import example.listeners.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    private final static Logger logger = LoggerFactory.getLogger(Main.class);

    /**
     * The main entry point for the sdk-example
     *
     * @param args provided command line arguments
     */
    public static void main(String[] args) throws SdkException {

        final Sdk sdk = Sdk.getInstance();
        final LiveOddsFeed liveOddsFeed = sdk.getLiveOdds();
        final LiveOddsFeed liveOddsVblFeed = sdk.getLiveOddsVbl();
        final LiveOddsFeed liveOddsVflFeed = sdk.getLiveOddsVfl();
        final RaceFeed liveOddsVhcFeed = sdk.getLiveOddsVhc();
        final RaceFeed liveOddsVdrFeed = sdk.getLiveOddsVdr();
        final LiveOddsFeed liveOddsBetpal = sdk.getLiveOddsBetpal();
        final LiveOddsFeed liveOddsLivePlex = sdk.getLiveOddsLivePlex();
        final LiveOddsFeed liveOddsSoccerRouletteFeed = sdk.getLiveOddsSoccerRoulette();
        final LiveScoutFeed liveScoutFeed = sdk.getLiveScout();
        final LcooFeed lcooFeed = sdk.getLcoo();
        final OddsCreatorFeed oddsCreatorFeed = sdk.getOddsCreator();
        final LiveOddsWithOutrightsFeed liveOddsVfcFeed = sdk.getLiveOddsVfc();

        final LiveOddsFeedListener oddsMatchFeedListener = new LiveOddsMatchFeedListenerImpl();

        final RaceFeedListener oddsRaceFeedListener = new LiveOddsRaceFeedListenerImpl();


        final LiveScoutFeedListener scoutFeedListener = new LiveScoutFeedListenerImpl();

        final LcooFeedListener lcooFeedListener = new LcooFeedListenerImpl();

        final LiveOddsWithOutrightsListener liveOddsWithOutrightsListener = new LiveOddsOutrightsFeedListener();

        if (liveScoutFeed != null) {
            liveScoutFeed.open(scoutFeedListener);
        }

        if (liveOddsFeed != null) {
            liveOddsFeed.open(oddsMatchFeedListener);
        }

        if (liveOddsVblFeed != null) {
            liveOddsVblFeed.open(oddsMatchFeedListener);
        }

        if (liveOddsVflFeed != null) {
            liveOddsVflFeed.open(oddsMatchFeedListener);
        }

        if (liveOddsVhcFeed != null) {
            liveOddsVhcFeed.open(oddsRaceFeedListener);
        }

        if (liveOddsVdrFeed != null) {
            liveOddsVdrFeed.open(oddsRaceFeedListener);
        }

        if (liveOddsBetpal != null) {
            liveOddsBetpal.open(oddsMatchFeedListener);
        }

        if (liveOddsLivePlex != null) {
            liveOddsLivePlex.open(oddsMatchFeedListener);
        }

        if (liveOddsSoccerRouletteFeed != null) {
            liveOddsSoccerRouletteFeed.open(oddsMatchFeedListener);
        }

        if (lcooFeed != null) {
            lcooFeed.open(lcooFeedListener);
        }

        if (liveOddsVfcFeed != null) {
            liveOddsVfcFeed.open(liveOddsWithOutrightsListener);
        }


        if (oddsCreatorFeed != null) {
            try {
                System.out.println("Listing all sports :");
                for (IdNameEntity sports : oddsCreatorFeed.getSports()) {
                    System.out.println(sports.getName());
                }
            } catch (OddsCreatorException e) {
                e.printStackTrace();
            }
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));


        logger.info("The sdk is running. Hit any key to exit");
        try {
            reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info("Closing the sdk");
        sdk.close();
        logger.info("Sdk successfully closed. Main thread will now exit");
    }
}
