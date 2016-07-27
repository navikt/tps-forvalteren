require('angular');
require('angular-ui-router');
require('angular-animate');
require('angular-material');
require('angular-messages');
require('angular-material-icons');
require('angular-highlightjs');
require('angular-moment');
require('pikaday');
require('pikaday-angular');

require('./components/login/login.module');
require('./components/service-rutine/service-rutine.module');

require('./components/login/login.controller');
require('./components/service-rutine/service-rutine.controller');

require('./services/service.module');
require('./services/location.service');
require('./services/session.service');
require('./services/utils.service');
require('./services/authentication.service');
require('./services/server-service-rutine.service');
require('./services/server-environment.service');

require('./factory/service-rutine.factory');

var app = angular.module('tps-vedlikehold', ['ui.router', 'ngMaterial', 'ngMdIcons', 'angularMoment', 'tps-vedlikehold.login',
    'tps-vedlikehold.service', 'tps-vedlikehold.service-rutine', 'pikaday']);


require('./shared/header/header.controller');
require('./shared/side-navigator/side-navigator.controller');

require('./directives/input-field.directive');
require('./directives/output-field.directive');

require('./settings/response-form.config');
require('./settings/service-rutine.config');

app.config(['pikadayConfigProvider', 'moment', function(pikaday, moment) {

    moment.locale("nb");
    var locales = {
        nb: {
            previousMonth : "Forrige maned",
            nextMonth : "Neste maned",
            months : ["Januar", "Februar", "Mars", "April", "Mai", "Juni", "Juli", "August", "September", "Oktober", "November", "Desember"],
            weekdays: ["Søndag", "Mandag", "Tirsdag", "Onsdag", "Torsdag", "Fredag", "Lørdag"],
            weekdaysShort: ["Sø", "Ma", "Ti", "On", "To", "Fr", "Lø"]
        }
    };

    pikaday.setConfig({
        format: "YYYY-MM-DD",
        firstDay: 1,
        defaultDate: new Date(),
        yearRange: [1900, 2100],
        i18n: locales.nb,
        locales: locales
    });
}]);

app.config(['$stateProvider', '$httpProvider', '$urlRouterProvider', '$mdThemingProvider',
    function($stateProvider, $httpProvider, $urlRouteProvider, $mdThemingProvider) {
        
    $urlRouteProvider.otherwise("/");

    $stateProvider

    .state('login', {
        url: "/login",
        views: {
            'content@' : {
                templateUrl: "app/components/login/login.html",
                controller: 'LoginCtrl'
            }
        }
    })
    .state('servicerutine', {
        url: "/",
        params: {
            serviceRutineName: null
        },
        resolve: {
            user: ['authenticationService', function (authenticationService) {
                return authenticationService.loadUser();
            }],
            serviceRutinesPromise: ['user', 'serviceRutineFactory', function (user, serviceRutineFactory) {
                return serviceRutineFactory.loadFromServerServiceRutines();
            }],
            environmentsPromise: ['user', 'serviceRutineFactory', function (user, serviceRutineFactory) {
                return serviceRutineFactory.loadFromServerEnvironments();
            }]
        },
        views: {
            'content@': {
                templateUrl: "app/components/service-rutine/service-rutine.html",
                controller: 'ServiceRutineCtrl'

            },
            'header@': {
                templateUrl: "app/shared/header/header.html",
                controller: 'HeaderCtrl'
            },
            'side-navigator@': {
                templateUrl: "app/shared/side-navigator/side-navigator.html",
                controller: 'SideNavigatorCtrl'
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