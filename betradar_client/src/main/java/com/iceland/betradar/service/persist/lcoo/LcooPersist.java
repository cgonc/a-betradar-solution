package com.iceland.betradar.service.persist.lcoo;

import com.iceland.betradar.dao.impl.*;
import com.iceland.betradar.model.IcelandSites;
import com.iceland.betradar.service.persist.sites.SiteCalculations;
import com.sportradar.sdk.feed.lcoo.entities.*;
import org.apache.commons.collections.CollectionUtils;
import org.joda.time.DateTime;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public enum LcooPersist {

	INSTANCE;

	//private final static Logger logger = LoggerFactory.getLogger(LcooPersist.class);

	private void saveXmlBetIfNotExists(Connection connection, DateTime now, Long sportId, long oddsType) throws SQLException {
		XmlBetType xmlBetType = XmlBetTypeDao.INSTANCE.findBySportIdAndSideId(connection, sportId, oddsType);
		if(xmlBetType == null){
			xmlBetType = new XmlBetType();
			xmlBetType.setSportId(sportId);
			xmlBetType.setSideId(oddsType);
			xmlBetType.setCreatedAt(now.toDate());
			xmlBetType.setUpdatedAt(now.toDate());
			XmlBetTypeDao.INSTANCE.insert(connection, xmlBetType);
		}
	}

	public String getGroupNameByOddsType(Integer oddsType) {
		String groupName;
		if(oddsType == 10 || oddsType == 20 || oddsType == 9 || oddsType == 11 || oddsType == 17 || oddsType == 30 || oddsType == 40 || oddsType == 43 || oddsType == 44 || oddsType == 46
				|| oddsType == 47 || oddsType == 225 || oddsType == 378 || oddsType == 381 || oddsType == 382 || oddsType == 406 || oddsType == 407 || oddsType == 13 || oddsType == 27
				|| oddsType == 16 || oddsType == 31 || oddsType == 4 || oddsType == 45 || oddsType == 505 || oddsType == 107 || oddsType == 94){
			groupName = "popular_market";
		} else if(oddsType == 321 || oddsType == 322){
			groupName = "over_under";
		} else if(oddsType == 42 || oddsType == 203 || oddsType == 204 || oddsType == 323 || oddsType == 324 || oddsType == 328 || oddsType == 333 || oddsType == 390 || oddsType == 391
				|| oddsType == 392 || oddsType == 393 || oddsType == 394 || oddsType == 395 || oddsType == 396 || oddsType == 22 || oddsType == 61 || oddsType == 220 || oddsType == 35 || oddsType == 36 || oddsType == 144 || oddsType == 325 || oddsType == 141 || oddsType == 101 || oddsType == 145){
			groupName = "half_time";
		} else if(oddsType == 259 || oddsType == 329 || oddsType == 334 || oddsType == 335 || oddsType == 336 || oddsType == 397 || oddsType == 398 || oddsType == 399 || oddsType == 401
				|| oddsType == 402 || oddsType == 403 || oddsType == 404 || oddsType == 405 || oddsType == 291 || oddsType == 295 || oddsType == 285 || oddsType == 289 || oddsType == 287){
			groupName = "second_half";
		} else if(oddsType == 56 || oddsType == 60 || oddsType == 202 || oddsType == 206 || oddsType == 226 || oddsType == 232 || oddsType == 258 || oddsType == 270 || oddsType == 271
				|| oddsType == 284 || oddsType == 375 || oddsType == 376 || oddsType == 377 || oddsType == 383 || oddsType == 33 || oddsType == 142 || oddsType == 21 || oddsType == 143 || oddsType == 83){
			groupName = "total_market";
		} else if(oddsType == 201 || oddsType == 235 || oddsType == 240 || oddsType == 241 || oddsType == 389){
			groupName = "player_special";
		} else if(oddsType == 48 || oddsType == 254 || oddsType == 255 || oddsType == 267 || oddsType == 315 || oddsType == 316 || oddsType == 317 || oddsType == 330 || oddsType == 353
				|| oddsType == 408 || oddsType == 410){
			groupName = "home_team_market";
		} else if(oddsType == 49 || oddsType == 256 || oddsType == 257 || oddsType == 268 || oddsType == 318 || oddsType == 319 || oddsType == 320 || oddsType == 331 || oddsType == 352
				|| oddsType == 409 || oddsType == 411){
			groupName = "away_team_market";
		} else if(oddsType == 233 || oddsType == 234 || oddsType == 262 || oddsType == 332 || oddsType == 337 || oddsType == 350 || oddsType == 385 || oddsType == 388 || oddsType == 104
				|| oddsType == 26 || oddsType == 261 || oddsType == 29 || oddsType == 464 || oddsType == 2 || oddsType == 520){
			groupName = "correct_score";
		} else if(oddsType == 1 || oddsType == 51 || oddsType == 52 || oddsType == 53 || oddsType == 54 || oddsType == 55 || oddsType == 70 || oddsType == 228 || oddsType == 229 || oddsType == 260
				|| oddsType == 384 || oddsType == 447 || oddsType == 34){
			groupName = "handicap";
		} else if(oddsType == 272 || oddsType == 273 || oddsType == 274 || oddsType == 275 || oddsType == 276 || oddsType == 277 || oddsType == 278 || oddsType == 279 || oddsType == 280
				|| oddsType == 281 || oddsType == 282 || oddsType == 283 || oddsType == 286 || oddsType == 288 || oddsType == 292 || oddsType == 293 || oddsType == 922){
			groupName = "corner";
		} else if(oddsType == 210 || oddsType == 212 || oddsType == 215 || oddsType == 219 || oddsType == 223 || oddsType == 294 || oddsType == 297 || oddsType == 303 || oddsType == 306
				|| oddsType == 309 || oddsType == 314 || oddsType == 339 || oddsType == 340 || oddsType == 341 || oddsType == 342 || oddsType == 354 || oddsType == 358 || oddsType == 362
				|| oddsType == 366 || oddsType == 370 || oddsType == 436 || oddsType == 440 || oddsType == 453 || oddsType == 454 || oddsType == 455 || oddsType == 456 || oddsType == 457
				|| oddsType == 458 || oddsType == 461 || oddsType == 462 || oddsType == 463 || oddsType == 521 || oddsType == 555 || oddsType == 558 || oddsType == 559 || oddsType == 560
				|| oddsType == 563 || oddsType == 564 || oddsType == 565 || oddsType == 516){
			groupName = "1st_x";
		} else if(oddsType == 213 || oddsType == 216 || oddsType == 231 || oddsType == 298 || oddsType == 301 || oddsType == 304 || oddsType == 307
				|| oddsType == 310 || oddsType == 312 || oddsType == 313 || oddsType == 355 || oddsType == 359 || oddsType == 363 || oddsType == 367 || oddsType == 371){
			groupName = "2nd_x";
		} else if(oddsType == 211 || oddsType == 214 || oddsType == 217 || oddsType == 296 || oddsType == 299 || oddsType == 302 || oddsType == 305 || oddsType == 308 || oddsType == 311
				|| oddsType == 343 || oddsType == 356 || oddsType == 360 || oddsType == 364 || oddsType == 368 || oddsType == 372){
			groupName = "3rd_x";
		} else if(oddsType == 344 || oddsType == 351 || oddsType == 357 || oddsType == 361 || oddsType == 365 || oddsType == 369 || oddsType == 373){
			groupName = "4th_x";
		} else if(oddsType == 345 || oddsType == 346){
			groupName = "5th_x";
		} else {
			groupName = "more_market";
		}
		return groupName;
	}

	public Integer getGroupOrderByOddsType(Integer oddsType) {
		Integer groupOrder;
		if(oddsType == 10 || oddsType == 20 || oddsType == 9 || oddsType == 11 || oddsType == 17 || oddsType == 30 || oddsType == 40 || oddsType == 43 || oddsType == 44 || oddsType == 46
				|| oddsType == 47 || oddsType == 225 || oddsType == 378 || oddsType == 381 || oddsType == 382 || oddsType == 406 || oddsType == 407 || oddsType == 13 || oddsType == 27
				|| oddsType == 16 || oddsType == 31 || oddsType == 4 || oddsType == 45 || oddsType == 505 || oddsType == 107 || oddsType == 94){
			groupOrder = 1;
		} else if(oddsType == 321 || oddsType == 322){
			groupOrder = 2;
		} else if(oddsType == 42 || oddsType == 203 || oddsType == 204 || oddsType == 323 || oddsType == 324 || oddsType == 328 || oddsType == 333 || oddsType == 390 || oddsType == 391
				|| oddsType == 392 || oddsType == 393 || oddsType == 394 || oddsType == 395 || oddsType == 396 || oddsType == 22 || oddsType == 61 || oddsType == 220 || oddsType == 35 || oddsType == 36 || oddsType == 144 || oddsType == 325 || oddsType == 141 || oddsType == 101 || oddsType == 145){
			groupOrder = 5;
		} else if(oddsType == 259 || oddsType == 329 || oddsType == 334 || oddsType == 335 || oddsType == 336 || oddsType == 397 || oddsType == 398 || oddsType == 399 || oddsType == 401
				|| oddsType == 402 || oddsType == 403 || oddsType == 404 || oddsType == 405 || oddsType == 291 || oddsType == 295 || oddsType == 285 || oddsType == 289 || oddsType == 287){
			groupOrder = 6;
		} else if(oddsType == 56 || oddsType == 60 || oddsType == 202 || oddsType == 206 || oddsType == 226 || oddsType == 232 || oddsType == 258 || oddsType == 270 || oddsType == 271
				|| oddsType == 284 || oddsType == 375 || oddsType == 376 || oddsType == 377 || oddsType == 383 || oddsType == 33 || oddsType == 142 || oddsType == 21 || oddsType == 143 || oddsType == 83){
			groupOrder = 4;
		} else if(oddsType == 201 || oddsType == 235 || oddsType == 240 || oddsType == 241 || oddsType == 389){
			groupOrder = 16;
		} else if(oddsType == 48 || oddsType == 254 || oddsType == 255 || oddsType == 267 || oddsType == 315 || oddsType == 316 || oddsType == 317 || oddsType == 330 || oddsType == 353
				|| oddsType == 408 || oddsType == 410){
			groupOrder = 14;
		} else if(oddsType == 49 || oddsType == 256 || oddsType == 257 || oddsType == 268 || oddsType == 318 || oddsType == 319 || oddsType == 320 || oddsType == 331 || oddsType == 352
				|| oddsType == 409 || oddsType == 411){
			groupOrder = 15;
		} else if(oddsType == 233 || oddsType == 234 || oddsType == 262 || oddsType == 332 || oddsType == 337 || oddsType == 350 || oddsType == 385 || oddsType == 388 || oddsType == 104
				|| oddsType == 26 || oddsType == 261 || oddsType == 29 || oddsType == 464 || oddsType == 2 || oddsType == 520){
			groupOrder = 13;
		} else if(oddsType == 1 || oddsType == 51 || oddsType == 52 || oddsType == 53 || oddsType == 54 || oddsType == 55 || oddsType == 70 || oddsType == 228 || oddsType == 229 || oddsType == 260
				|| oddsType == 384 || oddsType == 447 || oddsType == 34){
			groupOrder = 3;
		} else if(oddsType == 272 || oddsType == 273 || oddsType == 274 || oddsType == 275 || oddsType == 276 || oddsType == 277 || oddsType == 278 || oddsType == 279 || oddsType == 280
				|| oddsType == 281 || oddsType == 282 || oddsType == 283 || oddsType == 286 || oddsType == 288 || oddsType == 292 || oddsType == 293 || oddsType == 922){
			groupOrder = 12;
		} else if(oddsType == 210 || oddsType == 212 || oddsType == 215 || oddsType == 219 || oddsType == 223 || oddsType == 294 || oddsType == 297 || oddsType == 303 || oddsType == 306
				|| oddsType == 309 || oddsType == 314 || oddsType == 339 || oddsType == 340 || oddsType == 341 || oddsType == 342 || oddsType == 354 || oddsType == 358 || oddsType == 362
				|| oddsType == 366 || oddsType == 370 || oddsType == 436 || oddsType == 440 || oddsType == 453 || oddsType == 454 || oddsType == 455 || oddsType == 456 || oddsType == 457
				|| oddsType == 458 || oddsType == 461 || oddsType == 462 || oddsType == 463 || oddsType == 521 || oddsType == 555 || oddsType == 558 || oddsType == 559 || oddsType == 560
				|| oddsType == 563 || oddsType == 564 || oddsType == 565 || oddsType == 516){
			groupOrder = 7;
		} else if(oddsType == 213 || oddsType == 216 || oddsType == 231 || oddsType == 298 || oddsType == 301 || oddsType == 304 || oddsType == 307
				|| oddsType == 310 || oddsType == 312 || oddsType == 313 || oddsType == 355 || oddsType == 359 || oddsType == 363 || oddsType == 367 || oddsType == 371){
			groupOrder = 8;
		} else if(oddsType == 211 || oddsType == 214 || oddsType == 217 || oddsType == 296 || oddsType == 299 || oddsType == 302 || oddsType == 305 || oddsType == 308 || oddsType == 311
				|| oddsType == 343 || oddsType == 356 || oddsType == 360 || oddsType == 364 || oddsType == 368 || oddsType == 372){
			groupOrder = 9;
		} else if(oddsType == 344 || oddsType == 351 || oddsType == 357 || oddsType == 361 || oddsType == 365 || oddsType == 369 || oddsType == 373){
			groupOrder = 10;
		} else if(oddsType == 345 || oddsType == 346){
			groupOrder = 11;
		} else {
			groupOrder = 99;
		}
		return groupOrder;
	}

	public void mapSportInfoToXmlTeam(SportEntity sportEntity, Connection connection, DateTime dateTime, long teamId, TextEntity text) throws SQLException {
		XmlTeam xmlTeam = XmlTeamDao.INSTANCE.findById(connection, teamId);
		String name = "";
		Long superId = 0L;
		if(text != null && text.getText() != null && text.getText().get(0) != null && text.getText().get(0).getValue() != null && text.getSuperid() != null){
			name = text.getText().get(0).getValue();
			superId = (long) text.getSuperid();
		}
		if(xmlTeam == null){
			xmlTeam = new XmlTeam();
			xmlTeam.setId(teamId);
			xmlTeam.setName(name);
			xmlTeam.setSportId((long) sportEntity.getId());
			xmlTeam.setSuperId(superId);
			xmlTeam.setUpdatedAt(dateTime.toDate());
			xmlTeam.setCreatedAt(dateTime.toDate());
			XmlTeamDao.INSTANCE.insert(connection, xmlTeam);
		} else {
			xmlTeam.setName(name);
			xmlTeam.setSportId((long) sportEntity.getId());
			xmlTeam.setSuperId(superId);
			xmlTeam.setUpdatedAt(dateTime.toDate());
			XmlTeamDao.INSTANCE.updateById(connection, xmlTeam);
		}
	}

	public void mapCategoryToXmlCountry(CategoryEntity categoryEntity, SportEntity sportEntity, Connection connection, DateTime dateTime) throws SQLException {
		XmlCountry xmlCountry = XmlCountryDao.INSTANCE.findById(connection, categoryEntity.getId());
		if(xmlCountry != null){
			xmlCountry.setName(categoryEntity.getTexts().get(1).getValue());
			xmlCountry.setUpdatedAt(dateTime.toDate());
			XmlCountryDao.INSTANCE.updateById(connection, xmlCountry);
		} else {
			XmlCountry newXmlCountry = new XmlCountry();
			newXmlCountry.setId(categoryEntity.getId());
			newXmlCountry.setName(categoryEntity.getTexts().get(1).getValue());
			newXmlCountry.setSportId((long) sportEntity.getId());
			newXmlCountry.setCreatedAt(dateTime.toDate());
			newXmlCountry.setUpdatedAt(dateTime.toDate());
			XmlCountryDao.INSTANCE.insert(connection, newXmlCountry);
		}
	}

	public void mapMatchInfoToXmlEvent(MatchEntity match, Connection connection, DateTime now, Integer homeTeamId, Integer awayTeamId) throws SQLException {
		if(match.getFixture().getDateInfo().getMatchDate().isBeforeNow()){
			return;
		}
		XmlEvent xmlEvent = XmlEventDao.INSTANCE.findById(connection, match.getMatchId());
		Boolean exists = false;
		if(xmlEvent != null){
			exists = true;
			xmlEvent.setUpdatedAt(now.toDate());
		} else {
			xmlEvent = new XmlEvent();
			xmlEvent.setCreatedAt(now.toDate());
			xmlEvent.setUpdatedAt(now.toDate());
			xmlEvent.setId(match.getMatchId());
		}
		xmlEvent.setSportId((long) match.getSport().getId());
		xmlEvent.setLeagueId((long) match.getTournament().getId());
		DateTime matchDate = match.getFixture().getDateInfo().getMatchDate();
		xmlEvent.setStartDate(matchDate.toDate());
		xmlEvent.setLocationId(match.getCategory().getId());
		xmlEvent.setHomeTeamId((long) homeTeamId);
		xmlEvent.setAwayTeamId((long) awayTeamId);
		xmlEvent.setName("");
		xmlEvent.setTime("");
		xmlEvent.setType("tournament");

		if(exists){
			XmlEventDao.INSTANCE.updateById(connection, xmlEvent);
		} else {
			XmlEventDao.INSTANCE.insert(connection, xmlEvent);
		}
	}

	public void mapMatchTournamentToXmlLeague(MatchEntity match, Connection connection, DateTime now) throws SQLException {
		XmlLeague xmlLeague = XmlLeagueDao.INSTANCE.findById(connection, (long) match.getTournament().getId());
		if(xmlLeague == null){
			xmlLeague = new XmlLeague();
			xmlLeague.setId((long) match.getTournament().getId());
			xmlLeague.setName(match.getTournament().getTexts().get(0).getValue());
			xmlLeague.setSportId((long) match.getSport().getId());
			xmlLeague.setLocationId(match.getCategory().getId());
			xmlLeague.setMinEvent(0L);
			xmlLeague.setWonLimit(0L);
			xmlLeague.setCustomerLimit(0D);
			xmlLeague.setProfit(0d);
			xmlLeague.setType("tournament");
			xmlLeague.setCreatedAt(now.toDate());
			xmlLeague.setUpdatedAt(now.toDate());
			XmlLeagueDao.INSTANCE.insert(connection, xmlLeague);
		} else {
			xmlLeague.setName(match.getTournament().getTexts().get(0).getValue());
			xmlLeague.setSportId((long) match.getSport().getId());
			xmlLeague.setLocationId(match.getCategory().getId());
			xmlLeague.setCreatedAt(now.toDate());
			xmlLeague.setUpdatedAt(now.toDate());
			XmlLeagueDao.INSTANCE.updateById(connection, xmlLeague);
		}
	}

	public void mapMatchOddsToXmlOutCome(MatchEntity match, Connection connection, DateTime now) throws SQLException {
		Long eventId = match.getMatchId();
		List<XmlOutCome> existingOutComes = XmlOutComeDao.INSTANCE.findByEventId(connection, match.getMatchId());
		if(!CollectionUtils.isEmpty(existingOutComes)){
			List<BetEntity> betEntities = match.getOdds() == null ? new ArrayList<>() : match.getOdds();

			for(BetEntity betEntity : betEntities){
				int oddsType = betEntity.getOddsType();
				for(OddsEntity oddsEntity : betEntity.getOdds()){
					XmlOutCome xmlOutCome;
					if(oddsEntity.getSpecialBetValue() == null){
						xmlOutCome = XmlOutComeDao.INSTANCE.findByEventIdOddsTypeOutCome(connection, eventId, (long) oddsType, oddsEntity.getOutCome());
					} else {
						xmlOutCome = XmlOutComeDao.INSTANCE.findByEventIdOddsTypeOutComeLine(connection, eventId, (long) oddsType, oddsEntity.getOutCome(), oddsEntity.getSpecialBetValue());
					}

					if(xmlOutCome != null){
						Double value;
						try{
							value = Double.valueOf(oddsEntity.getValue());
						} catch (Exception ex){
							value = 0d;
						}
						xmlOutCome.setValue(value);
						XmlOutComeDao.INSTANCE.updateById(connection, xmlOutCome);
					} else {
						Double value;
						try{
							value = Double.valueOf(oddsEntity.getValue());
						} catch (Exception ex){
							value = 0d;
						}
						Double lordValue = SiteCalculations.INSTANCE.calculateSiteProfitValue(eventId, (long) match.getTournament().getId(), oddsType, (long)match.getSport().getId(), IcelandSites.LORD.getSiteId(), connection);
						Double asyaValue = SiteCalculations.INSTANCE.calculateSiteProfitValue(eventId, (long) match.getTournament().getId(), oddsType, (long)match.getSport().getId(), IcelandSites.ASYA.getSiteId(), connection);
						//logger.info("Lord profit {}",lordValue);
						xmlOutCome = new XmlOutCome();
						xmlOutCome.setEventId(eventId);
						xmlOutCome.setOddsType((long) oddsType);
						xmlOutCome.setGroupName(getGroupNameByOddsType(oddsType));
						xmlOutCome.setGroupOrder(getGroupOrderByOddsType(oddsType).toString());
						xmlOutCome.setOutcome(oddsEntity.getOutCome());
						xmlOutCome.setLine(oddsEntity.getSpecialBetValue());
						xmlOutCome.setValue(value);
						xmlOutCome.setLord(lordValue);
						xmlOutCome.setAsya(asyaValue);
						xmlOutCome.setCreatedAt(now.toDate());
						xmlOutCome.setUpdatedAt(now.toDate());
						XmlOutComeDao.INSTANCE.insert(connection, xmlOutCome);
					}
				}
			}
		} else {

			List<BetEntity> betEntities = match.getOdds() == null ? new ArrayList<>() : match.getOdds();
			List<XmlOutCome> xmlOutComes = new ArrayList<>();

			for(BetEntity betEntity : betEntities){
				int oddsType = betEntity.getOddsType();
				for(OddsEntity oddsEntity : betEntity.getOdds()){
					Double value;
					try{
						value = Double.valueOf(oddsEntity.getValue());
					} catch (Exception ex){
						value = 0d;
					}
					Double lordValue = SiteCalculations.INSTANCE.calculateSiteProfitValue(eventId, (long) match.getTournament().getId(), oddsType, (long)match.getSport().getId(), IcelandSites.LORD.getSiteId(), connection);
					Double asyaValue = SiteCalculations.INSTANCE.calculateSiteProfitValue(eventId, (long) match.getTournament().getId(), oddsType, (long)match.getSport().getId(), IcelandSites.ASYA.getSiteId(), connection);
					//logger.info("Lord profit {}",lordValue);
					XmlOutCome xmlOutCome = new XmlOutCome();
					xmlOutCome.setEventId(eventId);
					xmlOutCome.setOddsType((long) oddsType);
					xmlOutCome.setGroupName(getGroupNameByOddsType(oddsType));
					xmlOutCome.setGroupOrder(getGroupOrderByOddsType(oddsType).toString());
					xmlOutCome.setOutcome(oddsEntity.getOutCome());
					xmlOutCome.setLine(oddsEntity.getSpecialBetValue());
					xmlOutCome.setValue(value);
					xmlOutCome.setLord(lordValue);
					xmlOutCome.setAsya(asyaValue);
					xmlOutCome.setCreatedAt(now.toDate());
					xmlOutCome.setUpdatedAt(now.toDate());
					xmlOutComes.add(xmlOutCome);
				}
			}
			XmlOutComeDao.INSTANCE.insert(connection, xmlOutComes);
		}

	}



	public void mapMatchSportAndOddToXmlBetType(MatchEntity match, Connection connection, DateTime now) throws SQLException {
		Long sportId = (long) match.getSport().getId();
		List<BetEntity> betEntities = match.getOdds() == null ? new ArrayList<>() : match.getOdds();
		for(BetEntity betEntity : betEntities){
			int oddsType = betEntity.getOddsType();
			saveXmlBetIfNotExists(connection, now, sportId, oddsType);
		}
	}

	public void mapOutRightsToXmlOutCome(Connection connection, DateTime now, OutrightEntity outrightEntity, Long leagueId, long eventId) throws SQLException {
		List<XmlOutCome> existingOutComes = XmlOutComeDao.INSTANCE.findByEventId(connection, eventId);
		if(!CollectionUtils.isEmpty(existingOutComes)){
			int oddsType = outrightEntity.getOdds().getOddsType();
			List<OddsEntity> oddsEntities = outrightEntity.getOdds().getOdds();
			for(OddsEntity oddsEntity : oddsEntities){
				XmlOutCome xmlOutCome = XmlOutComeDao.INSTANCE.findByEventIdOddsTypeOutCome(connection, eventId, (long) oddsType, oddsEntity.getId().toString());
				if(xmlOutCome != null){
					Double value;
					try{
						value = Double.valueOf(oddsEntity.getValue());
					} catch (Exception ex){
						value = 0d;
					}
					xmlOutCome.setValue(value);
					XmlOutComeDao.INSTANCE.updateById(connection, xmlOutCome);
				} else {
					Double value;
					try{
						value = Double.valueOf(oddsEntity.getValue());
					} catch (Exception ex){
						value = 0d;
					}
					Double lordValue = SiteCalculations.INSTANCE.calculateSiteProfitValue(eventId, leagueId, oddsType, (long) outrightEntity.getSport().getId(), IcelandSites.LORD.getSiteId(), connection);
					Double asyaValue = SiteCalculations.INSTANCE.calculateSiteProfitValue(eventId, leagueId, oddsType, (long) outrightEntity.getSport().getId(), IcelandSites.ASYA.getSiteId(), connection);
					//logger.info("Lord profit {}",lordValue);
					xmlOutCome = new XmlOutCome();
					xmlOutCome.setEventId(eventId);
					xmlOutCome.setOddsType((long) oddsType);
					xmlOutCome.setOutcome(oddsEntity.getId().toString());
					xmlOutCome.setLine(oddsEntity.getSpecialBetValue());
					xmlOutCome.setValue(value);
					xmlOutCome.setLord(lordValue);
					xmlOutCome.setAsya(asyaValue);
					xmlOutCome.setCreatedAt(now.toDate());
					xmlOutCome.setUpdatedAt(now.toDate());
					XmlOutComeDao.INSTANCE.insert(connection, xmlOutCome);
				}
			}
		} else {
			int oddsType = outrightEntity.getOdds().getOddsType();
			List<OddsEntity> oddsEntities = outrightEntity.getOdds().getOdds();
			List<XmlOutCome> xmlOutComes = new ArrayList<>();
			for(OddsEntity oddsEntity : oddsEntities){
				Double value;
				try{
					value = Double.valueOf(oddsEntity.getValue());
				} catch (Exception ex){
					value = 0d;
				}
				Double lordValue = SiteCalculations.INSTANCE.calculateSiteProfitValue(eventId, leagueId, oddsType, (long) outrightEntity.getSport().getId(), IcelandSites.LORD.getSiteId(), connection);
				Double asyaValue = SiteCalculations.INSTANCE.calculateSiteProfitValue(eventId, leagueId, oddsType, (long) outrightEntity.getSport().getId(), IcelandSites.ASYA.getSiteId(), connection);
				//logger.info("Lord profit {}",lordValue);
				XmlOutCome xmlOutCome = new XmlOutCome();
				xmlOutCome.setEventId(eventId);
				xmlOutCome.setOddsType((long) oddsType);
				xmlOutCome.setOutcome(oddsEntity.getId().toString());
				xmlOutCome.setLine(oddsEntity.getSpecialBetValue());
				xmlOutCome.setValue(value);
				xmlOutCome.setLord(lordValue);
				xmlOutCome.setAsya(asyaValue);
				xmlOutCome.setCreatedAt(now.toDate());
				xmlOutCome.setUpdatedAt(now.toDate());
				xmlOutComes.add(xmlOutCome);
			}
			XmlOutComeDao.INSTANCE.insert(connection, xmlOutComes);
		}
	}

	public void mapOutRightsToXmlEvent(Connection connection, DateTime now, OutrightEntity outrightEntity, Long id, String eventName) throws SQLException {
		XmlEvent xmlEvent = XmlEventDao.INSTANCE.findById(connection, (long) outrightEntity.getId());
		String status = outrightEntity.getFixture().getEventInfo().isActive() ? "active" : "passive";
		if(xmlEvent == null){
			xmlEvent = new XmlEvent();
			xmlEvent.setId((long) outrightEntity.getId());
			xmlEvent.setSportId((long) outrightEntity.getSport().getId());
			xmlEvent.setLeagueId(id);
			xmlEvent.setStartDate(outrightEntity.getFixture().getEventInfo().getEventDate().toDate());
			xmlEvent.setLocationId(outrightEntity.getCategory().getId());
			xmlEvent.setName(eventName);
			xmlEvent.setType("event");
			xmlEvent.setCreatedAt(now.toDate());
			xmlEvent.setUpdatedAt(now.toDate());
			XmlEventDao.INSTANCE.insert(connection, xmlEvent);
		} else {
			xmlEvent.setStartDate(outrightEntity.getFixture().getEventInfo().getEventDate().toDate());
			xmlEvent.setUpdatedAt(now.toDate());
			XmlEventDao.INSTANCE.updateById(connection, xmlEvent);
		}
	}

	public void mapOutRightsToXmlLeague(Connection connection, DateTime now, OutrightEntity outrightEntity, Long id, String eventName) throws SQLException {
		XmlLeague xmlLeague = XmlLeagueDao.INSTANCE.findByName(connection, eventName);

		if(xmlLeague == null){
			xmlLeague = new XmlLeague();
			xmlLeague.setId(id);
			xmlLeague.setName(eventName);
			xmlLeague.setSportId((long) outrightEntity.getSport().getId());
			xmlLeague.setLocationId(outrightEntity.getCategory().getId());
			xmlLeague.setType("event");
			xmlLeague.setCreatedAt(now.toDate());
			xmlLeague.setUpdatedAt(now.toDate());
			XmlLeagueDao.INSTANCE.insert(connection, xmlLeague);
		}
	}

	public void mapOutRightsToXmlTeam(Connection connection, DateTime now, OutrightEntity outrightEntity) throws SQLException {
		List<TextsEntity> texts = outrightEntity.getFixture().getCompetitors().getTexts();
		for(TextsEntity textsEntity : texts){
			for(TextEntity text : textsEntity.getTexts()){
				LcooPersist.INSTANCE.mapSportInfoToXmlTeam(outrightEntity.getSport(), connection, now, text.getId(), text);
			}
		}
	}

	public void mapOutRightsToXmlBetType(Connection connection, OutrightEntity outrightEntity, DateTime now) throws SQLException {
		long sportId = (long) outrightEntity.getSport().getId();
		BetEntity betEntity = outrightEntity.getOdds();
		int outRightOddsType = betEntity.getOddsType();
		saveXmlBetIfNotExists(connection, now, sportId, outRightOddsType);
	}
}
