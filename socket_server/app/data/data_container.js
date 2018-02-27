"use strict";

module.exports = function () {
    var module = {};
    var templateData = {};
    var oddsCacheData = {};

    module.clearDataContainer = function () {
        templateData = {};
        oddsCacheData = {};
    };

    module.refreshOddsCache = function (eventId, oddsCacheEntityObject) {
        oddsCacheData[eventId] = oddsCacheEntityObject;
    };

    module.removeOddsCache = function (eventId) {
        delete oddsCacheData[eventId];
    };

    module.getOddsByEventId = function (eventId) {
        return oddsCacheData[eventId];
    };

    module.setTemplateData = function (incomingTemplateData) {
        templateData = incomingTemplateData;
    };

    module.getTemplateData = function () {
        return templateData;
    };

    module.getOddsCache = function () {
        return oddsCacheData;
    };


    return module;
};