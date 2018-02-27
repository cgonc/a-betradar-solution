package com.iceland.betradar;

public class ProtocolConstants {
    private ProtocolConstants() {}

    //Socket channels
    public static final String BET_RADAR_CLOCK = "alarm:betRadarClock";
    public static final String BET_RADAR_ALIVE_HEALTH_STATUS = "alarm:betRadarHealthStatus";
    public static final String BET_RADAR_CLIENT_ON_INITIALIZED = "alarm:betRadarClientOnInitialized";
    public static final String BET_RADAR_CLIENT_DISCONNECTED = "alarm:betRadarClientDisconnects";
    public static final String ALIVE_REMOVED = "event:alive:removed";
    public static final String ALIVE_CHANGED = "event:alive:changed";
    public static final String ALIVE_NEW_ALIVE_EXISTING_TOURNAMENT_RECEIVED = "event:new:alive:existing:tournament";
    public static final String ALIVE_NEW_ALIVE_EXISTING_CATEGORY = "event:new:alive:existing:category";
    public static final String ALIVE_NEW_ALIVE_EXISTING_SPORT = "event:new:alive:existing:sport";
    public static final String ALIVE_NEW_SPORT = "event:new:alive:new:sport";
    public static final String ON_SCORE_CARD_RECEIVED = "event:onScoreCardReceived";
    public static final String REFRESH_TEMPLATE_DATA_ON_SOCKET_SERVER = "event:refreshTemplateData";
    public static final String REFRESH_ODDS_CACHE_ON_SOCKET_SERVER = "event:RefreshOddsCache";
    public static final String REMOVE_ICELAND_ODDS_CACHE_DATA = "event:RemoveOddsCache";
    public static final String ON_ODDS_CHANGE = "event:onOddsChange";
    public static final String PRIME_ODDS_CHANGE = "event:onPrimeOddsChange";
    public static final String PRIME_ODDS_DELETED = "event:onPrimeOddsDeleted";
    public static final String ON_BET_CANCEL = "event:onBetCancel";
    public static final String ON_BET_CANCEL_UNDONE = "event:onBetCancelUndone";
    public static final String ON_BET_CLEAR = "event:onBetClear";
    public static final String ON_BET_CLEAR_ROLL_BACK = "event:onBetClearRollBack";
    public static final String ON_BET_START = "event:onBetStart";
    public static final String ON_BET_STOP = "event:onBetStop";
    public static final String ON_EVENT_MESSAGE_RECEIVED = "event:onEventMessageReceived";
    public static final String ON_EVENT_STATUS_RECEIVED = "event:onEventStatusReceived";
    public static final String ON_IRRELEVANT_ODDS_CHANGE = "event:onIrrelevantOddsChange";
}
