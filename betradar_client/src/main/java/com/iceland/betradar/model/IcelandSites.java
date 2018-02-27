package com.iceland.betradar.model;

public enum IcelandSites {
    LORD(9999L),ASYA(5900L);

    private Long siteId;

    IcelandSites(long siteId) {
        this.siteId = siteId;
    }

    public Long getSiteId() {
        return siteId;
    }
}
