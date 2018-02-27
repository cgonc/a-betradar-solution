'use strict';

angular.module('socketIoAngularClientApp.view_state_module', [])

    .factory('ViewStateService', ['IcelandSocket', function (IcelandSocket) {
        var viewStateServiceInstance = {};

        IcelandSocket.on("event:onTemplateDataReceived", function (data) {
            console.log(data);
        });

        return viewStateServiceInstance;
    }]);