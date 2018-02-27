package com.iceland.betradar.model.iceland;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.sportradar.sdk.feed.common.entities.IdNameTuple;
import com.sportradar.sdk.feed.common.entities.LocalizedString;

public class IcelandTournament implements Serializable{

	private static final long serialVersionUID = -5689206330307240574L;

	private Long id;
	private LocalizedString name;
	public Map<Long, IcelandEvent> events = new ConcurrentHashMap<>();

	public IcelandTournament(Long tournamentId, LocalizedString tournamentName) {
		this.id = tournamentId;
		this.name = tournamentName;
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

		IcelandTournament that = (IcelandTournament) o;

		return id.equals(that.id) && name.equals(that.name);

	}

	@Override
	public int hashCode() {
		int result = id.hashCode();
		result = 31 * result + name.hashCode();
		return result;
	}
}
