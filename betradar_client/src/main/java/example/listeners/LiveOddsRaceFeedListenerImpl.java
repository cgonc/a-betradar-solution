package example.listeners;

import com.sportradar.sdk.common.enums.FeedEventType;
import com.sportradar.sdk.feed.liveodds.classes.EventDataPackage;
import com.sportradar.sdk.feed.liveodds.entities.common.*;
import com.sportradar.sdk.feed.liveodds.entities.virtual.*;
import com.sportradar.sdk.feed.liveodds.interfaces.RaceFeed;
import com.sportradar.sdk.feed.liveodds.interfaces.RaceFeedListener;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class LiveOddsRaceFeedListenerImpl implements RaceFeedListener {

    private static final Logger logger = LoggerFactory.getLogger(LiveOddsRaceFeedListenerImpl.class);

    /**
     * Invoked by the observed live feed when the feed is closed.
     */
    @Override
    public void onClosed(RaceFeed sender) {
        logger.info("On closed");
    }

    @Override
    public void onFeedEvent(RaceFeed sender, FeedEventType feedEventType) {
        logger.info("Feed event occurred. Event: {}", feedEventType);
    }

    /**
     * Invoked by the observed live feed when the feed is opened.
     */
    @Override
    public void onOpened(RaceFeed sender) {
        logger.info("On opened");
    }

    /**
     * Invoked when the feed is initialized and you can accept bets.
     * It is invoked exactly once after the feed has been opened.
     *
     * @param sender - originating feed
     */
    @Override
    public void onInitialized(final RaceFeed sender) {
        logger.info("On initialized");

        //Any method call can block due to request limits, we don't want to block SDK dispatcher thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                sender.getEventList(
                        DateTime.now().minus(Duration.standardHours(12).getMillis()),
                        DateTime.now().plus(Duration.standardHours(12).getMillis()),
                        true);
            }
        }).start();
    }

    @Override
    public void onAliveReceived(RaceFeed sender, AliveEntity entity) {
        logger.info("On alive with {} races", entity.getEventHeaders().size());
    }

    @Override
    public void onRaceResultReceived(RaceFeed sender, RaceResultEntity entity) {
        Long parentId = ((RaceHeaderEntity) entity.getEventHeader()).getParentId();
        logger.info("On race result race id : {} with parent id: {}", entity.getEventId(), parentId);
    }

    @Override
    public void onBetCancel(RaceFeed sender, BetCancelEntity entity) {
        Long parentId = ((RaceHeaderEntity) entity.getEventHeader()).getParentId();
        logger.info("On bet cancel race id : {} with parent id: {}", entity.getEventId(), parentId);
    }

    @Override
    public void onBetCancelUndone(RaceFeed sender, BetCancelUndoEntity entity) {
        Long parentId = ((RaceHeaderEntity) entity.getEventHeader()).getParentId();
        logger.info("On bet cancel undone race id : {} with parent id: {}", entity.getEventId(), parentId);
    }

    @Override
    public void onBetClear(RaceFeed sender, BetClearEntity entity) {
        Long parentId = ((RaceHeaderEntity) entity.getEventHeader()).getParentId();
        logger.info("On bet clear race id : {} with parent id: {}", entity.getEventId(), parentId);
    }

    @Override
    public void onBetClearRollback(RaceFeed sender, BetClearRollbackEntity entity) {
        Long parentId = ((RaceHeaderEntity) entity.getEventHeader()).getParentId();
        logger.info("On bet clear rollback race id : {} with parent id: {}", entity.getEventId(), parentId);
    }

    @Override
    public void onBetStart(RaceFeed sender, BetStartEntity entity) {
        Long parentId = ((RaceHeaderEntity) entity.getEventHeader()).getParentId();
        logger.info("On bet start race id : {} with parent id: {}", entity.getEventId(), parentId);
    }

    @Override
    public void onBetStop(RaceFeed sender, BetStopEntity entity) {
        Long parentId = ((RaceHeaderEntity) entity.getEventHeader()).getParentId();
        logger.info("On bet stop(artificial : {}) race id : {} with parent id: {}",
                entity.isArtificial(),
                entity.getEventId(),
                parentId);
    }

    @Override
    public void onEventMessagesReceived(RaceFeed sender, EventDataPackage entity) {
        logger.info("On race messages. Received messages count {}", entity.getMessages().size());
    }

    @Override
    public void onEventStatusReceived(RaceFeed sender, EventDataPackage entity) {
        logger.info("On race status. Received messages count {}", entity.getMessages().size());
    }

    @Override
    public void onIrrelevantOddsChange(RaceFeed sender, IrrelevantOddsChangeEntity entity) {
        Long parentId = ((RaceHeaderEntity) entity.getEventHeader()).getParentId();
        logger.info("On irrelevant odds change race id : {} with parent id: {}", entity.getEventId(), parentId);
    }

    @Override
    public void onMetaInfoReceived(RaceFeed sender, MetaInfoEntity entity) {
        List<RaceDayEntity> raceDays = ((RaceMetaData) entity.getMetaInfoDataContainer()).getRaceDays();
        List<? extends RaceHeaderInfoEntity> races = ((RaceMetaData) entity.getMetaInfoDataContainer()).getRaceHeaders();
        logger.info("On meta info with {} race days and {} races", raceDays.size(), races.size());
    }

    @Override
    public void onOddsChange(RaceFeed sender, OddsChangeEntity entity) {
        Long parentId = ((RaceHeaderEntity) entity.getEventHeader()).getParentId();
        logger.info("On odds change race id : {} with parent id: {}", entity.getEventId(), parentId);
    }
}
