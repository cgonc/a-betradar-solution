package com.iceland.betradar.unified.listeners;

import com.sportradar.unifiedodds.sdk.OddsFeedListener;
import com.sportradar.unifiedodds.sdk.OddsFeedSession;
import com.sportradar.unifiedodds.sdk.oddsentities.*;
import com.sportradar.unifiedodds.sdk.sportentities.SportEvent;

public class CustomOddsFeedListener implements OddsFeedListener {

    @Override
    public void onOddsChange(OddsFeedSession oddsFeedSession, OddsChange<SportEvent> oddsChange) {

    }

    @Override
    public void onBetStop(OddsFeedSession oddsFeedSession, BetStop<SportEvent> betStop) {

    }

    @Override
    public void onBetSettlement(OddsFeedSession oddsFeedSession, BetSettlement<SportEvent> betSettlement) {

    }

    @Override
    public void onRollbackBetSettlement(OddsFeedSession oddsFeedSession, RollbackBetSettlement<SportEvent> rollbackBetSettlement) {

    }

    @Override
    public void onBetCancel(OddsFeedSession oddsFeedSession, BetCancel<SportEvent> betCancel) {

    }

    @Override
    public void onRollbackBetCancel(OddsFeedSession oddsFeedSession, RollbackBetCancel<SportEvent> rollbackBetCancel) {

    }

    @Override
    public void onFixtureChange(OddsFeedSession oddsFeedSession, FixtureChange<SportEvent> fixtureChange) {

    }

    @Override
    public void onUnalteredEvents(OddsFeedSession oddsFeedSession, UnalteredEventsMessage<SportEvent> unalteredEventsMessage) {

    }
}
