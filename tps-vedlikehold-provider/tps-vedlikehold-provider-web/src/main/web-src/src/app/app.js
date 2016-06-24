require('angular');
require('angular-ui-router');
require('angular-animate');
require('angular-material');
require('angular-messages');

require('./components/login/login');
require('./services/serviceModule');
require('./services/locationService');

var app = angular.module('tps-vedlikehold', ['ui.router', 'ngMaterial', 'tps-vedlikehold.login']);

app.config(['$stateProvider', '$urlRouterProvider', function($stateProvider, $urlRouteProvider) {

    $urlRouteProvider.otherwise("/");

    $stateProvider

    .state('login', {
        url: "/",
        templateUrl: "app/components/login/login.html"

    })
    .state('dashboard', {
        url: "/dashboard",
        templateUrl: "app/components/dashboard/dashboard.html"
    });
}]);
