package example.listeners;

import com.sportradar.sdk.feed.liveodds.classes.EventDataPackage;
import com.sportradar.sdk.feed.liveodds.entities.outrights.*;
import com.sportradar.sdk.feed.liveodds.interfaces.outrights.LiveOddsWithOutrightsFeed;
import com.sportradar.sdk.feed.liveodds.interfaces.outrights.LiveOddsWithOutrightsListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LiveOddsOutrightsFeedListener extends LiveOddsMatchBaseListener<LiveOddsWithOutrightsFeed> implements LiveOddsWithOutrightsListener {

    private static final Logger logger = LoggerFactory.getLogger(LiveOddsOutrightsFeedListener.class);

    @Override
    public void onAliveReceived(LiveOddsWithOutrightsFeed sender, AliveWithOutrightsEntity entity) {
        logger.info("On alive with {} matches and {} outrights", entity.getEventHeaders().size(), entity.getOutrightHeaders().size());
    }

    @Override
    public void onOutrightBetCancel(LiveOddsWithOutrightsFeed sender, OutrightBetCancelEntity entity) {
        logger.info("Outright bet cancel for {}", entity.getOutrightHeader().getId());
    }

    @Override
    public void onOutrightBetClear(LiveOddsWithOutrightsFeed sender, OutrightBetClearEntity entity) {
        logger.info("Outright bet clear for {}", entity.getOutrightHeader().getId());
    }

    @Override
    public void onOutrightBetStart(LiveOddsWithOutrightsFeed sender, OutrightBetStartEntity entity) {
        logger.info("Outright bet start for {}", entity.getOutrightHeader().getId());
    }

    @Override
    public void onOutrightBetStop(LiveOddsWithOutrightsFeed sender, OutrightBetStopEntity entity) {
        logger.info("Outright bet stop for {}", entity.getOutrightHeader().getId());
    }

    @Override
    public void onOutrightOddsChange(LiveOddsWithOutrightsFeed sender, OutrightOddsChangeEntity entity) {
        logger.info("Outright odds change for {}", entity.getOutrightHeader().getId());
    }

    @Override
    public void onOutrightStatusReceived(LiveOddsWithOutrightsFeed sender, EventDataPackage entity) {
        logger.info("Outright status with {} of messages", entity.getMessages().size());
    }
}
