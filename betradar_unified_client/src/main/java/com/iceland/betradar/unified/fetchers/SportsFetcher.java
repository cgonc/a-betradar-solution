package com.iceland.betradar.unified.fetchers;

import com.iceland.betradar.unified.services.JSON;
import com.sportradar.unifiedodds.sdk.OddsFeedSession;
import com.sportradar.unifiedodds.sdk.SportsInfo;
import com.sportradar.unifiedodds.sdk.sportentities.Sport;
import com.sportradar.unifiedodds.sdk.sportentities.SportEvent;
import com.sportradar.unifiedodds.sdk.sportentities.Tournament;

import java.util.Timer;
import java.util.TimerTask;

public enum SportsFetcher {
    INSTANCE;
    //private static final Logger logger = LoggerFactory.getLogger(SportsFetcher.class);
    private OddsFeedSession session;
    private Integer interval;
    private Timer sportFetcherTimer = new Timer();
    private TimerTask sportsFetcherTask = new TimerTask() {
        @Override
        public void run() {
            SportsInfo sportsInfo = session.getSportsInfoManager();
            try {
//                for (Sport sport : sportsInfo.getSports()) {
//                    System.out.println(sport.getId());
//                    System.out.println(sport.getName());
//                    System.out.println(sport.getSportCategories());
//                    System.out.println(JSON.INSTANCE.gson.toJson(sport));
//                }
                for (SportEvent sportEvent : sportsInfo.getLiveSportEvents()) {
                    System.out.println(JSON.INSTANCE.gson.toJson(sportEvent));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    public SportsFetcher withOddsFeedSession(OddsFeedSession session) {
        this.session = session;
        return this;
    }

    public SportsFetcher withInterval(Integer interval) {
        this.interval = interval * 1000;
        return this;
    }

    public void start() {
        sportFetcherTimer.schedule(this.sportsFetcherTask, this.interval);
    }


}
