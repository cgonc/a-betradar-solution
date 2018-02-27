package com.iceland.betradar.unified;


import com.iceland.betradar.unified.fetchers.SportsFetcher;
import com.iceland.betradar.unified.listeners.CustomOddsFeedListener;
import com.sportradar.unifiedodds.sdk.*;
import com.sportradar.unifiedodds.sdk.oddsentities.ProductDown;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;

public class Main {
    private final static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        CustomOddsFeedListener listener = new CustomOddsFeedListener();
        OddsFeedConfiguration config = new OddsFeedConfiguration();
        config.setBookmakerId(17700);
        config.setAccessToken("YeIx0dQVKhRIOEukkI");
        config.setDefaultLocale(new Locale("tr","TR"));

        OddsFeedSessionBuilder builder = OddsFeedSessionBuilder.init(new SDKGlobalEventsListener() {
            @Override
            public void onConnectionDown() {

            }

            @Override
            public void onProductDown(ProductDown productDown) {

            }
        }, config);
        builder.setListener(listener).setMessageInterest(MessageInterest.AllMessages);
        OddsFeedSession session = builder.setListener(listener).setMessageInterest(MessageInterest.AllMessages).build();

        SportsFetcher.INSTANCE.withOddsFeedSession(session).withInterval(1).start();

//        SportsInfo sportsInfo = session.getSportsInfoManager();
//        sportsInfo.getSports().forEach(System.out::println);
//        sportsInfo.getActiveTournaments("soccer").forEach(System.out::println);
//        sportsInfo.getSportEventsFor(new Date()).forEach(System.out::println);
//        sportsInfo.getLiveSportEvents().forEach(System.out::println);

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        logger.info("ICELAND BET-RADAR UNIFIED CLIENT IS RUNNING. HIT ANY KEY TO EXIT");
        try {
            reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        session.close();
        logger.info("CLOSING THE CLIENT");
    }
}
