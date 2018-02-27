--Check uniqueness for entries that has null lines.
select count(1) say, eventID, oddsType, outcome from xml_outcome where line is null group by eventID,oddsType,outcome;

--Check uniqueness for entries that has non null line.
select count(1) say, eventID, oddsType, outcome, line from xml_outcome where line is not null group by eventID,oddsType,outcome,line;