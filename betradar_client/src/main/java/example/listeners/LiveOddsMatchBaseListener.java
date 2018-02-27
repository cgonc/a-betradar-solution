package example.listeners;

import com.sportradar.sdk.common.enums.FeedEventType;
import com.sportradar.sdk.feed.liveodds.classes.EventDataPackage;
import com.sportradar.sdk.feed.liveodds.entities.common.*;
import com.sportradar.sdk.feed.liveodds.entities.liveodds.LiveOddsMetaData;
import com.sportradar.sdk.feed.liveodds.entities.liveodds.MatchHeaderEntity;
import com.sportradar.sdk.feed.liveodds.entities.liveodds.MatchHeaderInfoEntity;
import com.sportradar.sdk.feed.liveodds.interfaces.LiveOddsBasedFeed;
import com.sportradar.sdk.feed.liveodds.interfaces.LiveOddsBasedFeedListener;
import com.sportradar.sdk.feed.liveodds.interfaces.LiveOddsTestManager;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public abstract class LiveOddsMatchBaseListener<T extends LiveOddsBasedFeed> implements LiveOddsBasedFeedListener<T> {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Invoked by the observed live feed when the feed is closed.
     */
    @Override
    public void onClosed(T sender) {
        logger.info("On closed");
    }

    @Override
    public void onFeedEvent(T sender, FeedEventType feedEventType) {
        logger.info("Feed event occurred. Event: {}", feedEventType);
    }

    /**
     * Invoked by the observed live feed when the feed is opened.
     */
    @Override
    public void onOpened(T sender) {
        logger.info("On opened");
    }

    /**
     * Invoked when the feed is initialized and you can accept bets.
     * It is invoked exactly once after the feed has been opened.
     *
     * @param sender - originating feed
     */
    @Override
    public void onInitialized(final T sender) {
        logger.info("On initialized");

        //Any method call can block due to request limits, we don't want to block SDK dispatcher thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (sender.getTestManager() instanceof LiveOddsTestManager) {
                    ((LiveOddsTestManager) sender.getTestManager()).startAuto();
                }
                sender.getEventList(
                        DateTime.now().minus(Duration.standardHours(12).getMillis()),
                        DateTime.now().plus(Duration.standardHours(12).getMillis()),
                        true);
            }
        }).start();

    }

    @Override
    public void onBetCancel(T sender, BetCancelEntity entity) {
        Long matchTime = ((MatchHeaderEntity) entity.getEventHeader()).getMatchTime();
        logger.info("On bet cancel match id : {} with match time: {}", entity.getEventId(), matchTime);
    }

    @Override
    public void onBetCancelUndone(T sender, BetCancelUndoEntity entity) {
        Long matchTime = ((MatchHeaderEntity) entity.getEventHeader()).getMatchTime();
        logger.info("On bet cancel undone match id : {} with match time: {}", entity.getEventId(), matchTime);
    }

    @Override
    public void onBetClear(T sender, BetClearEntity entity) {
        Long matchTime = ((MatchHeaderEntity) entity.getEventHeader()).getMatchTime();
        logger.info("On bet clear match id : {} with match time: {}", entity.getEventId(), matchTime);
    }

    @Override
    public void onBetClearRollback(T sender, BetClearRollbackEntity entity) {
        Long matchTime = ((MatchHeaderEntity) entity.getEventHeader()).getMatchTime();
        logger.info("On bet clear rollback match id : {} with match time: {}", entity.getEventId(), matchTime);
    }

    @Override
    public void onBetStart(T sender, BetStartEntity entity) {
        Long matchTime = ((MatchHeaderEntity) entity.getEventHeader()).getMatchTime();
        logger.info("On bet start match id : {} with match time: {}", entity.getEventId(), matchTime);
    }

    @Override
    public void onBetStop(T sender, BetStopEntity entity) {
        Long matchTime = ((MatchHeaderEntity) entity.getEventHeader()).getMatchTime();
        logger.info("On bet stop(artificial : {}) match id : {} with match time: {}",
                entity.isArtificial(),
                entity.getEventId(),
                matchTime);
    }

    @Override
    public void onEventMessagesReceived(T sender, EventDataPackage entity) {
        logger.info("On match messages. Received messages count {}", entity.getMessages().size());
    }

    @Override
    public void onEventStatusReceived(T sender, EventDataPackage entity) {
        logger.info("On match status. Received messages count {}", entity.getMessages().size());
    }

    @Override
    public void onIrrelevantOddsChange(T sender, IrrelevantOddsChangeEntity entity) {
        Long matchTime = ((MatchHeaderEntity) entity.getEventHeader()).getMatchTime();
        logger.info("On irrelevant odds change match id : {} with match time: {}", entity.getEventId(), matchTime);
    }

    @Override
    public void onMetaInfoReceived(T sender, MetaInfoEntity entity) {
        List<MatchHeaderInfoEntity> matches = ((LiveOddsMetaData) entity.getMetaInfoDataContainer()).getMatchHeaderInfos();
        logger.info("On meta info with {} matches", matches.size());
    }

    @Override
    public void onOddsChange(T sender, OddsChangeEntity entity) {
        Long matchTime = ((MatchHeaderEntity) entity.getEventHeader()).getMatchTime();
        logger.info("On odds change match id : {} with match time: {}", entity.getEventId(), matchTime);
    }


}
