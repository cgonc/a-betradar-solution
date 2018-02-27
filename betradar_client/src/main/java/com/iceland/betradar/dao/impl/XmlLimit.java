package com.iceland.betradar.dao.impl;

import java.util.Date;

public class XmlLimit {
    private Long id;
    private Long siteId;
    private String type;
    private Long typeId;
    private Long minEvent;
    private Double wonLimit;
    private Double customerLimit;
    private Double maxLimit;
    private Double profit;
    private String feed;
    private Date createdAt;
    private Date updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSiteId() {
        return siteId;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public Long getMinEvent() {
        return minEvent;
    }

    public void setMinEvent(Long minEvent) {
        this.minEvent = minEvent;
    }

    public Double getWonLimit() {
        return wonLimit;
    }

    public void setWonLimit(Double wonLimit) {
        this.wonLimit = wonLimit;
    }

    public Double getCustomerLimit() {
        return customerLimit;
    }

    public void setCustomerLimit(Double customerLimit) {
        this.customerLimit = customerLimit;
    }

    public Double getMaxLimit() {
        return maxLimit;
    }

    public void setMaxLimit(Double maxLimit) {
        this.maxLimit = maxLimit;
    }

    public Double getProfit() {
        return profit;
    }

    public void setProfit(Double profit) {
        this.profit = profit;
    }

    public String getFeed() {
        return feed;
    }

    public void setFeed(String feed) {
        this.feed = feed;
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
