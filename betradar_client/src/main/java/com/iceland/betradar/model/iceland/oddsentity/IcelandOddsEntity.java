package com.iceland.betradar.model.iceland.oddsentity;

import java.util.Map;

import com.sportradar.sdk.feed.common.entities.LocalizedString;
import com.sportradar.sdk.feed.liveodds.entities.common.OddsFieldEntity;
import com.sportradar.sdk.feed.liveodds.enums.OddsType;

public class IcelandOddsEntity {

	private boolean active;
	private Boolean changed;
	private Long combination;
	private String forTheRest;
	private String freeText;
	private long id;
	private Boolean mostBalanced;
	private LocalizedString name;
	private Map<String, OddsFieldEntity> oddFields;
	private String specialOddsValue;
	private Long subType;
	private OddsType type;
	private long typeId;
	private String groupName;

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Boolean getChanged() {
		return changed;
	}

	public void setChanged(Boolean changed) {
		this.changed = changed;
	}

	public Long getCombination() {
		return combination;
	}

	public void setCombination(Long combination) {
		this.combination = combination;
	}

	public String getForTheRest() {
		return forTheRest;
	}

	public void setForTheRest(String forTheRest) {
		this.forTheRest = forTheRest;
	}

	public String getFreeText() {
		return freeText;
	}

	public void setFreeText(String freeText) {
		this.freeText = freeText;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Boolean getMostBalanced() {
		return mostBalanced;
	}

	public void setMostBalanced(Boolean mostBalanced) {
		this.mostBalanced = mostBalanced;
	}

	public LocalizedString getName() {
		return name;
	}

	public void setName(LocalizedString name) {
		this.name = name;
	}

	public Map<String, OddsFieldEntity> getOddFields() {
		return oddFields;
	}

	public void setOddFields(Map<String, OddsFieldEntity> oddFields) {
		this.oddFields = oddFields;
	}

	public String getSpecialOddsValue() {
		return specialOddsValue;
	}

	public void setSpecialOddsValue(String specialOddsValue) {
		this.specialOddsValue = specialOddsValue;
	}

	public Long getSubType() {
		return subType;
	}

	public void setSubType(Long subType) {
		this.subType = subType;
	}

	public OddsType getType() {
		return type;
	}

	public void setType(OddsType type) {
		this.type = type;
	}

	public long getTypeId() {
		return typeId;
	}

	public void setTypeId(long typeId) {
		this.typeId = typeId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
}
