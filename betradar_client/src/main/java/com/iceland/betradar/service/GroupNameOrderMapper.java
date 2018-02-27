package com.iceland.betradar.service;

import com.iceland.betradar.service.persist.lcoo.LcooPersist;
import com.sportradar.sdk.feed.liveodds.entities.common.OddsEntity;
import com.sportradar.sdk.feed.liveodds.enums.OddsType;

public class GroupNameOrderMapper {

	private OddsEntity oddsEntity;
	private String groupName;
	private Integer groupOrder;

	public GroupNameOrderMapper(OddsEntity activeOddsEntity) {
		this.oddsEntity = activeOddsEntity;
	}

	public String getGroupName() {
		return groupName;
	}

	public Integer getGroupOrder() {
		return groupOrder;
	}

	public GroupNameOrderMapper invoke() {
		if(oddsEntity.getSubType() == null){
			if(oddsEntity.getType() == OddsType.TO){
				groupName = "total_market";
				groupOrder = 4;
			} else if(oddsEntity.getType() == OddsType.HC){
				groupName = "handicap";
				groupOrder = 3;
			} else if(oddsEntity.getType() == OddsType.ITEM3W){
				groupName = "popular_market";
				groupOrder = 1;
			} else if(oddsEntity.getType() == OddsType.FT3W){
				groupName = "popular_market";
				groupOrder = 1;
			} else if(oddsEntity.getType() == OddsType.FT2W){
				groupName = "popular_market";
				groupOrder = 1;
			} else if(oddsEntity.getType() == OddsType.FTNW){
				groupName = "popular_market";
				groupOrder = 1;
			} else {
				groupName = "more_market";
				groupOrder = 99;
			}
		} else {
			groupName = LcooPersist.INSTANCE.getGroupNameByOddsType(oddsEntity.getSubType().intValue());
			groupOrder = LcooPersist.INSTANCE.getGroupOrderByOddsType(oddsEntity.getSubType().intValue());
		}
		return this;
	}
}
