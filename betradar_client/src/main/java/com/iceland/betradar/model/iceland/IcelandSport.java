package com.iceland.betradar.model.iceland;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.sportradar.sdk.feed.common.entities.IdNameTuple;
import com.sportradar.sdk.feed.common.entities.LocalizedString;

public class IcelandSport implements Serializable{

	private static final long serialVersionUID = -2947630958199867803L;

	private Long id;
	private LocalizedString name;
	public Map<Long, IcelandCategory> icelandCategories = new ConcurrentHashMap<>();

	public IcelandSport(Long sportId, LocalizedString sportName) {
		this.id = sportId;
		this.name = sportName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalizedString getName() {
		return name;
	}

	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;

		IcelandSport that = (IcelandSport) o;

		return id != null ? id.equals(that.id) : that.id == null && (name != null ? name.equals(that.name) : that.name == null);

	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (name != null ? name.hashCode() : 0);
		return result;
	}
}
