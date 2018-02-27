package com.iceland.betradar.service;

import com.iceland.betradar.model.BetRadarAliveHealthStatusInfo;
import com.iceland.betradar.model.ProtocolConstants;

import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.joda.time.Period;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A singleton clock instance which needs to be in sync with BetRadar system.
 * The class is responsible for supplying last alive message received data.
 * If the last alive message passes the threshold which is 20 seconds, a bet stop message will also be emitted.
 */
public enum BetRadarState {
	INSTANCE;
	private static final Logger logger = LoggerFactory.getLogger(BetRadarState.class);
	public AtomicBoolean feedOpened = new AtomicBoolean(false);
	public AtomicBoolean triggerOnInitFlag = new AtomicBoolean(false);
	private LocalDateTime lastAliveMessageReceivedTime;
	private AtomicInteger aliveMessageReceivedCounter;
	private Timer aliveMessageReceivedTimer;
	private TimerTask checkAliveMessageReceivedTime = new TimerTask() {

		@Override
		public void run() {
			logger.info(" LAST ALIVE MESSAGE RECEIVED : " + lastAliveMessageReceivedTime.toString("dd.MM.yyyy HH:mm:ss"));
			logger.info(" NOW : " + LocalDateTime.now().toString("dd:MM.yyyy HH:mm:ss"));
			Long diff = (LocalDateTime.now().toDate().getTime() - lastAliveMessageReceivedTime.toDate().getTime()) / 1000;
			logger.info(" DIFFERENCE : " + diff + " SECONDS PASSED....");
			BetRadarAliveHealthStatusInfo aliveMessageInfo = new BetRadarAliveHealthStatusInfo();
			aliveMessageInfo.setLastAliveMessageReceivedTime(lastAliveMessageReceivedTime.toDate());
			aliveMessageInfo.setLastAliveMessageReceivedInMilliSeconds(diff.intValue());
			if(diff > 20){
				aliveMessageInfo.setBetsShouldContinue(false);
			} else {
				aliveMessageInfo.setBetsShouldContinue(true);
			}
			IcelandSocket.INSTANCE.emitMessageToBetRadarClientRoom(ProtocolConstants.BET_RADAR_ALIVE_HEALTH_STATUS, JSON.INSTANCE.gson.toJson(aliveMessageInfo));
		}
	};

	BetRadarState() {
		lastAliveMessageReceivedTime = LocalDateTime.now();
		aliveMessageReceivedCounter = new AtomicInteger();
		aliveMessageReceivedTimer = new Timer();
		aliveMessageReceivedTimer.schedule(checkAliveMessageReceivedTime, 0, 15000);
	}

	/**
	 * A method for setting the last alive message received time.
	 */
	public void setLastAliveMessageReceivedTime() {
		this.lastAliveMessageReceivedTime = new LocalDateTime();
		aliveMessageReceivedCounter.incrementAndGet();
	}

}
