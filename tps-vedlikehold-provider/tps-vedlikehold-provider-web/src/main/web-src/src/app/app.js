/**
 * Created by k148233 on 23.06.2016.
 */
require('angular');
require('angular-ui-router');
require('angular-animate');
require('angular-material');


//require('./components/login/login');

var app = angular.module('tps-vedlikehold', ['ui.router', 'ngMaterial']);

app.config(['$stateProvider', '$urlRouterProvider', function($stateProvider, $urlRouteProvider) {

    $urlRouteProvider.otherwise("/");

    $stateProvider

    .state('login', {
        url: "/login",
        templateUrl: "app/components/login/login.html"
    })
    .state('dashboard', {
        url: "/dashboard",
        templateUrl: "app/components/dashboard/dashboard.html"
    });

}]);

app.controller('LoginCtrl', function($scope) {
    
});