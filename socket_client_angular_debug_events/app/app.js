'use strict';

var socketIoAngularClientApp = angular.module('socketIoAngularClientApp', [
    'ui.router',
    'socketIoAngularClientApp.data_module',
    'socketIoAngularClientApp.view_state_module',
    'socketIoAngularClientApp.main'
    ]);

socketIoAngularClientApp.config(['$stateProvider', '$urlRouterProvider', function ($stateProvider, $urlRouterProvider) {

    $urlRouterProvider.otherwise("/main");

    $stateProvider
        .state('main', {
            url: '/main',
            templateUrl: 'views/main.html',
            controller: 'MainCtrl'
        });

}]);