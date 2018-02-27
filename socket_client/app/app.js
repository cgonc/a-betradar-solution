'use strict';

angular
    .module('socketIoAngularClientApp', ['ngMaterial'])
    .run(function ($log) {
        $log.debug("socketIoAngularClientApp running ");
    })
    .config(function ($mdIconProvider, $mdThemingProvider) {
        $mdIconProvider
            .icon("Soccer", "./assets/svg/sports/soccer.svg", 24)
            .icon("Basketball", "./assets/svg/sports/basketball.svg", 24)
            .icon("Tennis", "./assets/svg/sports/tennis.svg", 24)
            .icon("American Football", "./assets/svg/sports/american-football.svg", 24)
            .icon("Ice Hockey", "./assets/svg/sports/ice-hockey.svg", 24)
    });