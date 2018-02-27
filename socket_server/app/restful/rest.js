"use strict";

module.exports = function (opts) {
    var data_container = opts.data_container;

    var module = {};

    var protocol_constants = opts.protocol_constants;
    var app = opts.app;

    function isEmpty(obj) {
        for(var prop in obj) {
            if(obj.hasOwnProperty(prop))
                return false;
        }

        return JSON.stringify(obj) === JSON.stringify({});
    }

    app.get('/', function (req, res) {
        res.send({"restAlive": true});
    });

    app.get('/protocol_constants', function (req, res) {
        res.send(protocol_constants);
    });

    app.get('/odds/:eventId/:oddId', function (req, res) {
        res.setHeader('Content-Type', 'application/json');
        var eventId = Number(req.params.eventId);
        var oddId = Number(req.params.oddId);
        var oddsData = data_container.getOddsByEventId(eventId);
        if (oddsData) {
            var activeOddsEntities = oddsData.activeOddsEntities;
            if (!activeOddsEntities) {
                res.send({});
            }
            var activeOdd = {};
            activeOddsEntities.some(function (oddEntity) {
                if (oddEntity.id === oddId) {
                    activeOdd = oddEntity;
                }
            });
            if (activeOdd) {
                var result = {};
                result.activeOdd = activeOdd;
                result.betStatus = oddsData.betStatus;
                res.send(result);
                return;
            } else {
                res.send({});
                return;
            }
        }
        res.send({});
    });

    app.get('/odds/:eventId', function (req, res) {
        res.setHeader('Content-Type', 'application/json');
        var eventId = Number(req.params.eventId);
        var oddsData = data_container.getOddsByEventId(eventId);
        if (oddsData) {
            res.send(oddsData);
            return;
        }
        res.send({});
    });

    app.get('/template_data', function (req, res) {
        res.setHeader('Content-Type', 'application/json');
        res.send(data_container.getTemplateData());
    });

    app.get('/oddscheck', function (req, res) {
        res.setHeader('Content-Type', 'application/json');
        var oddsCache = data_container.getOddsCache();
        var odds = [];
        for (var eventId in oddsCache) {
            if(!oddsCache[eventId].activeOddsEntities || isEmpty(oddsCache[eventId].activeOddsEntities)){
                odds.push(oddsCache[eventId]);
            }
        }
        res.send(odds);
    });


    return module;
};