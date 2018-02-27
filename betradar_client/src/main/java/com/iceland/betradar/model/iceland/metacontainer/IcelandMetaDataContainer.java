package com.iceland.betradar.model.iceland.metacontainer;

import java.io.Serializable;

import com.sportradar.sdk.feed.liveodds.entities.liveodds.MatchHeaderEntity;

public class IcelandMetaDataContainer implements Serializable {

	private static final long serialVersionUID = -4186535207376906379L;

	private MatchHeaderEntity matchHeader;
	private IcelandMatchInfo matchInfo;

	public MatchHeaderEntity getMatchHeader() {
		return matchHeader;
	}

	public void setMatchHeader(MatchHeaderEntity matchHeader) {
		this.matchHeader = matchHeader;
	}

	public IcelandMatchInfo getMatchInfo() {
		return matchInfo;
	}

	public void setMatchInfo(IcelandMatchInfo matchInfo) {
		this.matchInfo = matchInfo;
	}
}
