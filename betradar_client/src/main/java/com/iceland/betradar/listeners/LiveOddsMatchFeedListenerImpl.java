package com.iceland.betradar.listeners;

import com.iceland.betradar.model.ProtocolConstants;
import com.iceland.betradar.service.BetRadarState;
import com.iceland.betradar.service.DataContainer;
import com.iceland.betradar.service.IcelandSocket;
import com.iceland.betradar.service.JSON;
import com.sportradar.sdk.feed.liveodds.entities.common.AliveEntity;
import com.sportradar.sdk.feed.liveodds.entities.liveodds.MatchHeaderEntity;
import com.sportradar.sdk.feed.liveodds.entities.liveodds.ScoreCardSummaryEntity;
import com.sportradar.sdk.feed.liveodds.interfaces.LiveOddsFeed;
import com.sportradar.sdk.feed.liveodds.interfaces.LiveOddsFeedListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LiveOddsMatchFeedListenerImpl extends LiveOddsMatchBaseListener <LiveOddsFeed> implements LiveOddsFeedListener{

    private static final Logger logger = LoggerFactory.getLogger(LiveOddsMatchFeedListenerImpl.class);


    @Override
    public void onAliveReceived(LiveOddsFeed sender, AliveEntity entity) {
        if(!BetRadarState.INSTANCE.feedOpened.get()){
            return;
        }
        logger.info("On alive with {} matches", entity.getEventHeaders().size());
        if(entity.getEventHeaders().size() == 0){
            return;
        }
        BetRadarState.INSTANCE.setLastAliveMessageReceivedTime();
        DataContainer.INSTANCE.setIcelandTemplateData(entity);
    }

    @Override
    public void onScoreCardReceived(LiveOddsFeed sender, ScoreCardSummaryEntity entity) {
        if(!BetRadarState.INSTANCE.feedOpened.get()){
            return;
        }
        Long matchTime = ((MatchHeaderEntity) entity.getEventHeader()).getMatchTime();
        logger.info("On score card match id : {} with match time: {} number of cards : {} number of scores : {}",
                entity.getEventId(), matchTime, entity.getCardSummaryByTime().size(), entity.getScoreSummaryByTime().size());
        IcelandSocket.INSTANCE.emitMessageToBetRadarClientRoom(ProtocolConstants.ON_SCORE_CARD_RECEIVED, JSON.INSTANCE.gson.toJson(entity));
    }
}
