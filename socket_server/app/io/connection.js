"use strict";

module.exports = function (opts) {
    var io = opts.io;
    var data_container = opts.data_container;

    var module = {};

    var betRadarClientRoom = io.of("/betRadarClientRoom");
    var icelandUsers = io.of("/icelandUsers");

    var secretKey = "alxlhj123%&4+###klmfkjhgnbv4rf94ds59hj!";
    var isBetRadarConnected = false;

    betRadarClientRoom.on('connection', function (socket) {

        isBetRadarConnected = true;

        socket.on("alarm:betRadarHealthStatus" + secretKey, function (msg) {
            var jsonMsg = JSON.parse(msg);
            icelandUsers.emit("alarm:betRadarHealthStatus", jsonMsg);
        });

        socket.on("disconnect", function () {
            console.log("BET RADAR CLIENT DISCONNECTS!!! EMITTING ALARM DATA... :((");
            isBetRadarConnected = false;
            icelandUsers.emit("alarm:betRadarClientDisconnects", "critical");
        });

        socket.on("alarm:betRadarClientOnInitialized" + secretKey, function () {
            console.log("Bet radar client initialized... CLEARING STATE ... Emitting alarm data :))");
            isBetRadarConnected = true;
            icelandUsers.emit("alarm:betRadarClientConnected", "critical");
            data_container.clearDataContainer();
        });

        socket.on("alarm:betRadarClientDisconnects" + secretKey, function () {
            console.log("BET RADAR CLIENT FEED DISCONNECTS!!! EMITTING ALARM DATA...");
            isBetRadarConnected = false;
            icelandUsers.emit("alarm:betRadarClientDisconnects", "critical");
        });

        socket.on("event:alive:removed" + secretKey, function (msg) {
            var jsonMsg = JSON.parse(msg);
            console.log(jsonMsg.length + " alive events removed...");
            icelandUsers.emit("event:alive:removed", jsonMsg);
        });

        socket.on("event:alive:changed" + secretKey, function (msg) {
            var jsonMsg = JSON.parse(msg);
            console.log(jsonMsg.length + " alive events changed...");
            icelandUsers.emit("event:alive:changed", jsonMsg);
        });

        socket.on("event:new:alive:existing:tournament" + secretKey, function (msg) {
            var jsonMsg = JSON.parse(msg);
            console.log(jsonMsg.length + " alive events received in existing tournament...");
            icelandUsers.emit("event:new:alive:existing:tournament", jsonMsg);
        });

        socket.on("event:new:alive:existing:category" + secretKey, function (msg) {
            var jsonMsg = JSON.parse(msg);
            console.log(jsonMsg.length + " alive events received in existing category...");
            icelandUsers.emit("event:new:alive:existing:category", jsonMsg);
        });

        socket.on("event:new:alive:existing:sport" + secretKey, function (msg) {
            var jsonMsg = JSON.parse(msg);
            console.log(jsonMsg.length + " alive events received in existing sport...");
            icelandUsers.emit("event:new:alive:existing:sport", jsonMsg);
        });

        socket.on("event:new:alive:new:sport" + secretKey, function (msg) {
            var jsonMsg = JSON.parse(msg);
            console.log(jsonMsg.length + " alive events received as new sport...");
            icelandUsers.emit("event:new:alive:new:sport", jsonMsg);
        });

        socket.on("event:onScoreCardReceived" + secretKey, function (msg) {
            console.log("score or card received...");
            var jsonMsg = JSON.parse(msg);
            icelandUsers.emit("event:onScoreCardReceived", jsonMsg);
        });

        socket.on("event:refreshTemplateData" + secretKey, function (msg) {
            //console.log("template data received : " + msg.length + " " + new Date());
            var jsonMsg = JSON.parse(msg);
            data_container.setTemplateData(jsonMsg);
        });

        socket.on("event:onOddsChange" + secretKey, function (msg) {
            var jsonMsg = JSON.parse(msg);
            icelandUsers.emit("event:onOddsChange", jsonMsg);
        });

        socket.on("event:onPrimeOddsChange" + secretKey, function (msg) {
            var jsonMsg = JSON.parse(msg);
            icelandUsers.emit("event:onPrimeOddsChange", jsonMsg);
        });

        socket.on("event:onPrimeOddsDeleted" + secretKey, function (msg) {
            var jsonMsg = JSON.parse(msg);
            icelandUsers.emit("event:onPrimeOddsDeleted", jsonMsg);
        });

        socket.on("event:RefreshOddsCache" + secretKey, function (msg) {
            var oddsCacheEntity = JSON.parse(msg);
            var oddsCacheEntityObject = {};
            var eventId = oddsCacheEntity.eventId;
            oddsCacheEntityObject.eventId = eventId;
            oddsCacheEntityObject.eventHeader = oddsCacheEntity.eventHeader;
            oddsCacheEntityObject.activeOddsEntities = oddsCacheEntity.activeOddsEntities;
            oddsCacheEntityObject.homeTeam = oddsCacheEntity.homeTeam;
            oddsCacheEntityObject.awayTeam = oddsCacheEntity.awayTeam;
            oddsCacheEntityObject.betStatus = oddsCacheEntity.betStatus;
            data_container.refreshOddsCache(eventId,oddsCacheEntityObject);
        });

        socket.on("event:RemoveOddsCache" + secretKey, function (eventId) {
            data_container.removeOddsCache(eventId);
        });
        
        socket.on("event:onBetCancel" + secretKey, function (msg) {
            var jsonMsg = JSON.parse(msg);
            icelandUsers.emit("event:onBetCancel", jsonMsg);
        });        

        socket.on("event:onBetCancelUndone" + secretKey, function (msg) {
            var jsonMsg = JSON.parse(msg);
            icelandUsers.emit("event:onBetCancelUndone", jsonMsg);
        });

        socket.on("event:onBetClear" + secretKey, function (msg) {
            var jsonMsg = JSON.parse(msg);
            icelandUsers.emit("event:onBetClear", jsonMsg);
        });

        socket.on("event:onBetClearRollBack" + secretKey, function (msg) {
            var jsonMsg = JSON.parse(msg);
            icelandUsers.emit("event:onBetClearRollBack", jsonMsg);
        });

        socket.on("event:onBetStart" + secretKey, function (msg) {
            var jsonMsg = JSON.parse(msg);
            icelandUsers.emit("event:onBetStart", jsonMsg);
        });

        socket.on("event:onBetStop" + secretKey, function (msg) {
            var jsonMsg = JSON.parse(msg);
            icelandUsers.emit("event:onBetStop", jsonMsg);
        });

        socket.on("event:onEventMessageReceived" + secretKey, function (msg) {
            var jsonMsg = JSON.parse(msg);
            icelandUsers.emit("event:onEventMessageReceived", jsonMsg);
        });

        socket.on("event:onEventStatusReceived" + secretKey, function (msg) {
            var jsonMsg = JSON.parse(msg);
            icelandUsers.emit("event:onEventStatusReceived", jsonMsg);
        });

        socket.on("event:onIrrelevantOddsChange" + secretKey, function (msg) {
            var jsonMsg = JSON.parse(msg);
            icelandUsers.emit("event:onIrrelevantOddsChange", jsonMsg);
        });

    });

    icelandUsers.on('connection', function (socket) {
        console.log('iceland user connected...');
        
        if (isBetRadarConnected) {
            console.log("Bet radar client is ON. Emitting up to date template data...");
            socket.emit("event:onTemplateDataReceived", data_container.getTemplateData());
        } else {
            console.log("BET RADAR CLIENT IS OFF. EMITTING ALARM DATA!!!");
            socket.emit("alarm:betRadarClientDisconnects", "critical");
        }

        socket.on("request:getOddsByEventId", function (eventId) {
            var requestedOdds = data_container.getOddsByEventId(eventId);
            var response = {};
            response.activeEventOdds = requestedOdds;
            socket.emit("response:getOddsByEventId", response);
        });
    });

    return module;
};