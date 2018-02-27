package com.iceland.betradar.model.iceland;

import java.io.Serializable;

import com.iceland.betradar.model.iceland.metacontainer.IcelandMatchInfo;
import com.sportradar.sdk.feed.liveodds.entities.common.OddsEntity;
import com.sportradar.sdk.feed.liveodds.entities.liveodds.MatchHeaderEntity;

public class IcelandEvent implements Serializable{

	private static final long serialVersionUID = -6430398695786594616L;

	private Long eventId;

	/**
	 * This field stores the match headers
	 */
	private MatchHeaderEntity eventHeader;

	/**
	 * This field stores the match info
	 */
	private IcelandMatchInfo matchInfo;

	/**
	 * This field holds the prime odd. Which has a type of 6 or 7 or the first odd.
	 */
	private OddsEntity primeOdd;

	/**
	 * This field holds the total odd count.
	 */
	private Integer activeOddsCount;

	public boolean isEmptyIcelandEvent() {
		if(activeOddsCount == 0 || primeOdd == null) {
			return true;
		}
		return  false;
	}


	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}

	public MatchHeaderEntity getEventHeader() {
		return eventHeader;
	}

	public void setEventHeader(MatchHeaderEntity eventHeader) {
		this.eventHeader = eventHeader;
	}

	public IcelandMatchInfo getMatchInfo() {
		return matchInfo;
	}

	public void setMatchInfo(IcelandMatchInfo matchInfo) {
		this.matchInfo = matchInfo;
	}

	public OddsEntity getPrimeOdd() {
		return primeOdd;
	}

	public void setPrimeOdd(OddsEntity primeOdd) {
		this.primeOdd = primeOdd;
	}

	public Integer getActiveOddsCount() {
		return activeOddsCount;
	}

	public void setActiveOddsCount(Integer activeOddsCount) {
		this.activeOddsCount = activeOddsCount;
	}

	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;

		IcelandEvent that = (IcelandEvent) o;

		return eventId.equals(that.eventId);

	}

	@Override
	public int hashCode() {
		return eventId.hashCode();
	}
}
