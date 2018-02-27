package example.listeners;

import com.sportradar.sdk.feed.liveodds.entities.common.AliveEntity;
import com.sportradar.sdk.feed.liveodds.entities.liveodds.MatchHeaderEntity;
import com.sportradar.sdk.feed.liveodds.entities.liveodds.ScoreCardSummaryEntity;
import com.sportradar.sdk.feed.liveodds.interfaces.LiveOddsFeed;
import com.sportradar.sdk.feed.liveodds.interfaces.LiveOddsFeedListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LiveOddsMatchFeedListenerImpl extends LiveOddsMatchBaseListener<LiveOddsFeed> implements LiveOddsFeedListener {

    private static final Logger logger = LoggerFactory.getLogger(LiveOddsMatchFeedListenerImpl.class);


    @Override
    public void onAliveReceived(LiveOddsFeed sender, AliveEntity entity) {
        logger.info("On alive with {} matches", entity.getEventHeaders().size());
    }

    @Override
    public void onScoreCardReceived(LiveOddsFeed sender, ScoreCardSummaryEntity entity) {
        Long matchTime = ((MatchHeaderEntity) entity.getEventHeader()).getMatchTime();

        logger.info("On score card match id : {} with match time: {} number of cards : {} number of scores : {}",
                entity.getEventId(), matchTime, entity.getCardSummaryByTime().size(), entity.getScoreSummaryByTime().size());
    }
}
