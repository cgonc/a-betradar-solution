package com.iceland.betradar.model.iceland;

import java.io.Serializable;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class IcelandTemplateData implements Serializable{

	private static final long serialVersionUID = 5431367667452980481L;

	public Map<Long, IcelandSport> sports = new ConcurrentHashMap<>();

	/**
	 * Search an iceland event by a event id in iceland template data object.
	 * @param eventId Event id to be searched for.
	 * @return Target IcelandEvent
	 */
	public Optional<IcelandEvent> getIcelandEventById(Long eventId) {
		Optional<IcelandEvent> result = Optional.empty();
		for(IcelandSport sport : this.sports.values()) {
			for(IcelandCategory category : sport.icelandCategories.values()) {
				for(IcelandTournament tournament : category.icelandTournaments.values()) {
					if(tournament.events.containsKey(eventId)) {
						result = Optional.of(tournament.events.get(eventId));
						return result;
					}
				}
			}
		}
		return result;
	}

	/**
	 * Search an iceland tournament by a event id in iceland template data object.
	 * @param eventId Event id to be searched for.
	 * @return Target IcelandTournament
	 */
	public Optional<IcelandTournament> getIcelandTournamentEventById(Long eventId) {
		Optional<IcelandTournament> result = Optional.empty();
		for(IcelandSport sport : this.sports.values()) {
			for(IcelandCategory category : sport.icelandCategories.values()) {
				for(IcelandTournament tournament : category.icelandTournaments.values()) {
					if(tournament.events.containsKey(eventId)) {
						result = Optional.of(tournament);
						return result;
					}
				}
			}
		}
		return result;
	}

	public void removeEmptyNodes() {
		for(IcelandSport sport : this.sports.values()) {
			for(IcelandCategory category : sport.icelandCategories.values()) {
				for(IcelandTournament tournament : category.icelandTournaments.values()) {
					if(tournament.events.size() == 0) {
						category.icelandTournaments.remove(tournament.getId());
					}
				}
				if(category.icelandTournaments.size() == 0) {
					sport.icelandCategories.remove(category.getId());
				}
			}
			if(sport.icelandCategories.size() == 0) {
				this.sports.remove(sport.getId());
			}
		}
	}
}
