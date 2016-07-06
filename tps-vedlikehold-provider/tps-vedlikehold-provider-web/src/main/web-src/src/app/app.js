require('angular');
require('angular-ui-router');
require('angular-animate');
require('angular-material');
require('angular-messages');
require('angular-material-icons');

require('./components/login/login');
require('./components/dashboard/dashboard');

require('./services/serviceModule');
require('./services/locationService');
require('./services/sessionService');
require('./services/utilsService');
require('./services/authenticationService');

var app = angular.module('tps-vedlikehold', ['ui.router', 'ngMaterial', 'ngMdIcons', 'tps-vedlikehold.login', 'tps-vedlikehold.service', 'tps-vedlikehold.dashboard']);

require('./factory/requestFactory');
require('./shared/header/header');
require('./shared/side-navigator/side-navigator');
require('./directives/textField');
require('./directives/dateField');
require('./directives/selectField');

app.config(['$stateProvider', '$httpProvider', '$urlRouterProvider', '$mdThemingProvider', function($stateProvider,  $httpProvider, $urlRouteProvider, $mdThemingProvider) {

    $urlRouteProvider.otherwise("/");

    $stateProvider

    .state('login', {
        url: "/",
        views: {
            'content@' : {
                templateUrl: "app/components/login/login.html"
            }
        }
    })
    .state('dashboard', {
        url: "/dashboard",
        views: {
            'content@' : {
                templateUrl: "app/components/dashboard/dashboard.html"
            },
            'header@' : {
                templateUrl: "app/shared/header/header.html"
            },
            'side-navigator@' : {
                templateUrl: "app/shared/side-navigator/side-navigator.html"
            }
        }

    });

    $httpProvider.defaults.headers.common["X-Requested-With"] = "XMLHttpRequest";

    var extended_grey = $mdThemingProvider.extendPalette('grey', { '50': '#ffffff'});
    $mdThemingProvider.definePalette('tps-vk-grey', extended_grey);

    $mdThemingProvider.theme('default')
        .primaryPalette('indigo', {
            'default': '500'
        })
        .accentPalette('blue', {
            'default': 'A200'
        })
        .backgroundPalette('tps-vk-grey', {
            'default': '50'
        });
}]);

app.run(['$rootScope', 'authenticationService', 'sessionService', 'locationService', function($rootScope, authenticationService, sessionService, locationService){
    // This extra call is necessary to ensure that the X-CSRF token is updated in instances where the assets have been pre-loaded before login
    // The login triggers the creation of a new token  but the updated token is not updated until the next subsequent server response is received with updated token
    $rootScope.$on('$stateChangeSuccess', function(event, toState){
        if (toState.name === 'dashboard') {
            authenticationService.authenticate(false, null);
        }
    });

    $rootScope.$on('$stateChangeStart', function(event, toState){
        if (toState.name === 'login') {
            return;
        }

        var authenticated = sessionService.getIsAuthenticated();

        // Commented out for testing reasons
        // if (!authenticated) {
        //     locationService.redirectToLoginUrl();
        // }
    });

}]);