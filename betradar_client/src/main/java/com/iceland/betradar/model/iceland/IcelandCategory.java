package com.iceland.betradar.model.iceland;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.sportradar.sdk.feed.common.entities.IdNameTuple;
import com.sportradar.sdk.feed.common.entities.LocalizedString;

public class IcelandCategory implements Serializable{

	private static final long serialVersionUID = -4656130299948436940L;

	private Long id;
	private LocalizedString name;
	public Map<Long, IcelandTournament> icelandTournaments = new ConcurrentHashMap<>();

	public IcelandCategory(Long categoryId, LocalizedString categoryName) {
		this.id = categoryId;
		this.name = categoryName;
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

		IcelandCategory that = (IcelandCategory) o;

		return id.equals(that.id) && name.equals(that.name);

	}

	@Override
	public int hashCode() {
		int result = id.hashCode();
		result = 31 * result + name.hashCode();
		return result;
	}
}
