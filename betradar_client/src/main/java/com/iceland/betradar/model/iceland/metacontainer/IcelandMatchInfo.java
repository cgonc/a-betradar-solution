package com.iceland.betradar.model.iceland.metacontainer;

import java.io.Serializable;

import org.joda.time.DateTime;

import com.sportradar.sdk.feed.common.entities.IdNameTuple;

public class IcelandMatchInfo implements Serializable {

	private static final long serialVersionUID = 4651288790703447193L;
	private IdNameTuple awayTeam;
	private DateTime dateOfMatch;
	private IdNameTuple homeTeam;

	public IdNameTuple getAwayTeam() {
		return awayTeam;
	}

	public void setAwayTeam(IdNameTuple awayTeam) {
		this.awayTeam = awayTeam;
	}

	public DateTime getDateOfMatch() {
		return dateOfMatch;
	}

	public void setDateOfMatch(DateTime dateOfMatch) {
		this.dateOfMatch = dateOfMatch;
	}

	public IdNameTuple getHomeTeam() {
		return homeTeam;
	}

	public void setHomeTeam(IdNameTuple homeTeam) {
		this.homeTeam = homeTeam;
	}
}
