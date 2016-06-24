require('angular');
require('angular-ui-router');
require('angular-animate');
require('angular-material');
require('angular-messages');

require('./components/login/login');
require('./services/serviceModule');
require('./services/locationService');
require('./services/AuthenticationService');
require('./services/sessionService');

var app = angular.module('tps-vedlikehold', ['ui.router', 'ngMaterial', 'tps-vedlikehold.login',  'tps-vedlikehold.service']);

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

app.run(['$rootScope', 'authenticationService', 'sessionService', 'locationService', function($rootScope, authenticationService, sessionService, locationService){

    $rootScope.$on('$stateChangeSuccess', function(){
        if (!sessionService.getIsAuthenticated()) {
            authenticationService.validateToken();
        }
    });

    $rootScope.$on('$stateChangeStart', function(event, toState, toParam, fromState, fromParam){
        if (toState.name === "login" && !fromState.abstract) {
            //locationService.updateLoginReturnUrl();
        }
    });

}]);