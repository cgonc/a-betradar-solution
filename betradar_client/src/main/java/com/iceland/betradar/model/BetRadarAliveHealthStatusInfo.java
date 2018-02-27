package com.iceland.betradar.model;

import java.io.Serializable;
import java.util.Date;

/**
 * A model class which can provide last alive message received information.
 */
public class BetRadarAliveHealthStatusInfo implements Serializable{

    private static final long serialVersionUID = -5642168493362352128L;

    private Date lastAliveMessageReceivedTime;
    private Integer lastAliveMessageReceivedInMilliSeconds;
    private Boolean betsShouldContinue;

    public Date getLastAliveMessageReceivedTime() {
        return lastAliveMessageReceivedTime;
    }

    public void setLastAliveMessageReceivedTime(Date lastAliveMessageReceivedTime) {
        this.lastAliveMessageReceivedTime = lastAliveMessageReceivedTime;
    }

    public Integer getLastAliveMessageReceivedInMilliSeconds() {
        return lastAliveMessageReceivedInMilliSeconds;
    }

    public void setLastAliveMessageReceivedInMilliSeconds(Integer lastAliveMessageReceivedInMilliSeconds) {
        this.lastAliveMessageReceivedInMilliSeconds = lastAliveMessageReceivedInMilliSeconds;
    }

    public Boolean getBetsShouldContinue() {
        return betsShouldContinue;
    }

    public void setBetsShouldContinue(Boolean betsShouldContinue) {
        this.betsShouldContinue = betsShouldContinue;
    }
}
