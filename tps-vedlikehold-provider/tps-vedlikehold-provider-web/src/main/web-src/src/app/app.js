require('angular');
require('angular-ui-router');
require('angular-animate');
require('angular-material');

require('./services/serviceModule');
require('./services/sessionService');
require('./services/authenticationService');

var app = angular.module('tps-vedlikehold', ['ui.router', 'ngMaterial', 'tps-vedlikehold.service']);

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

app.controller('loginCtrl', ['$rootScope', '$scope','$http', 'sessionService', 'authenticationService',
    function($rootScope, $scope, $http, sessionService, authenticationService){
        $scope.title = 'Logg inn!';

        $scope.authenticationError = false;
        $scope.serverError = false;
        $scope.pendingRequest = false;

        var callback = function(authResponse){
            console.log(authResponse.status);
        };


        $scope.login = function() {
            var credentials = {
                username: "username",
                password: "password"
            };
            authenticationService.authenticate(credentials, callback);
        };
    }]);