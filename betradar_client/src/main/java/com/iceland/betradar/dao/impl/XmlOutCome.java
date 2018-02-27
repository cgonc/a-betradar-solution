package com.iceland.betradar.dao.impl;

import java.util.Date;

public class XmlOutCome {
	private Long id;
	private Long eventId;
	private Long oddsType;
	private String outcome;
	private String line;
	private String groupName;
	private String groupOrder;
	private Double value;
	private Double lord;
	private Double asya;
	private Date createdAt;
	private Date updatedAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getEventId() {
		return eventId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}

	public Long getOddsType() {
		return oddsType;
	}

	public void setOddsType(Long oddsType) {
		this.oddsType = oddsType;
	}

	public String getOutcome() {
		return outcome;
	}

	public void setOutcome(String outcome) {
		this.outcome = outcome;
	}

	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getGroupOrder() {
		return groupOrder;
	}

	public void setGroupOrder(String groupOrder) {
		this.groupOrder = groupOrder;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public Double getLord() {
		return lord;
	}

	public void setLord(Double lord) {
		this.lord = lord;
	}

	public Double getAsya() {
		return asya;
	}

	public void setAsya(Double asya) {
		this.asya = asya;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
}
