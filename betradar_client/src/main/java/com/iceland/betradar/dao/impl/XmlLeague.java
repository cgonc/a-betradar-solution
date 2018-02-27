package com.iceland.betradar.dao.impl;

import java.util.Date;

public class XmlLeague {
	private Long id;
	private String name;
	private Long locationId;
	private Long sportId;
	private Long minEvent;
	private Long wonLimit;
	private Double customerLimit;
	private Double profit;
	private String type;
	private Date createdAt;
	private Date updatedAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getLocationId() {
		return locationId;
	}

	public void setLocationId(Long locationId) {
		this.locationId = locationId;
	}

	public Long getSportId() {
		return sportId;
	}

	public void setSportId(Long sportId) {
		this.sportId = sportId;
	}

	public Long getMinEvent() {
		return minEvent;
	}

	public void setMinEvent(Long minEvent) {
		this.minEvent = minEvent;
	}

	public Long getWonLimit() {
		return wonLimit;
	}

	public void setWonLimit(Long wonLimit) {
		this.wonLimit = wonLimit;
	}

	public Double getCustomerLimit() {
		return customerLimit;
	}

	public void setCustomerLimit(Double customerLimit) {
		this.customerLimit = customerLimit;
	}

	public Double getProfit() {
		return profit;
	}

	public void setProfit(Double profit) {
		this.profit = profit;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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
