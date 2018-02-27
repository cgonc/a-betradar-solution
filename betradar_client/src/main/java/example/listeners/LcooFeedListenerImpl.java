package example.listeners;

import com.sportradar.sdk.common.enums.FeedEventType;
import com.sportradar.sdk.feed.lcoo.entities.BatchCompleted;
import com.sportradar.sdk.feed.lcoo.entities.MatchEntity;
import com.sportradar.sdk.feed.lcoo.entities.OutrightsEntity;
import com.sportradar.sdk.feed.lcoo.entities.ThreeBallEntity;
import com.sportradar.sdk.feed.lcoo.interfaces.LcooFeed;
import com.sportradar.sdk.feed.lcoo.interfaces.LcooFeedListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LcooFeedListenerImpl implements LcooFeedListener {

    private final static Logger logger = LoggerFactory.getLogger(LcooFeedListenerImpl.class);

    @Override
    public void onBatchCompleted(LcooFeed lcooFeed, BatchCompleted batchCompleted) {
        //Only used for two phase commit.
    }

    /**
     * Invoked by the observed {@link com.sportradar.sdk.feed.lcoo.interfaces.LcooFeed} instance when new {@link com.sportradar.sdk.feed.lcoo.entities.MatchEntity} is received from the server.
     *
     * @param sender The {@link com.sportradar.sdk.feed.lcoo.interfaces.LcooFeed} instance which received the entity.
     * @param match  The {@link com.sportradar.sdk.feed.lcoo.entities.MatchEntity} representing the received match
     */
    @Override
    public void onMatchReceived(LcooFeed sender, MatchEntity match) {
        logger.info("On match with match id: {}", match.getMatchId());
    }

    /**
     * Invoked by the observed {@link com.sportradar.sdk.feed.lcoo.interfaces.LcooFeed} instance when new {@link com.sportradar.sdk.feed.lcoo.entities.OutrightsEntity} is received from the server.
     *
     * @param sender    The {@link com.sportradar.sdk.feed.lcoo.interfaces.LcooFeed} instance which received the entity.
     * @param outrights The {@link com.sportradar.sdk.feed.lcoo.entities.OutrightsEntity} representing the received outrights.
     */
    @Override
    public void onOutrightsReceived(LcooFeed sender, OutrightsEntity outrights) {
        logger.info("On outrights with nr of {} outrights", outrights.getOutrightEntities().size());
    }

    /**
     * Invoked by the observed {@link com.sportradar.sdk.feed.lcoo.interfaces.LcooFeed} instance when new {@link com.sportradar.sdk.feed.lcoo.entities.ThreeBallEntity} is received from the server.
     *
     * @param sender    The {@link com.sportradar.sdk.feed.lcoo.interfaces.LcooFeed} instance which received the entity.
     * @param threeBall The {@link com.sportradar.sdk.feed.lcoo.entities.ThreeBallEntity} representing the received threeBall
     */
    @Override
    public void onThreeBallReceived(LcooFeed sender, ThreeBallEntity threeBall) {
        logger.info("On three ball with three ball id: {}", threeBall.getId());
    }

    @Override
    public void onClosed(LcooFeed sender) {
        logger.info("On closed");
    }

    /**
     * Invoked by the observed live feed when it encounters an special event related to the behavior of the server.
     *
     * @param eventType The {@link com.sportradar.sdk.common.enums.FeedEventType} member specifying the type of the occurred event.
     */
    @Override
    public void onFeedEvent(LcooFeed sender, FeedEventType eventType) {
        logger.info("Lcoo feed event occurred. Event: {}", eventType);
    }

    @Override
    public void onOpened(LcooFeed sender) {
        logger.info("On opened");
    }
}
