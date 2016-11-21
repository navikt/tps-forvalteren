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

require('./components/index');
require('./services/service.module');
require('./directives/directives.module');
require('./factory/factory.module');

var app = angular.module('tps-vedlikehold', ['ui.router', 'ngMaterial', 'ngMdIcons', 'angularMoment', 'tps-vedlikehold.login',
    'tps-vedlikehold.service', 'tps-vedlikehold.factory', 'tps-vedlikehold.service-rutine', 'tps-vedlikehold.directives','pikaday']);

require('./shared/index');

app.config(['pikadayConfigProvider', 'moment', function (pikaday, moment) {

    moment.locale("nb");
    var locales = {
        nb: {
            previousMonth: "Forrige maned",
            nextMonth: "Neste maned",
            months: ["Januar", "Februar", "Mars", "April", "Mai", "Juni", "Juli", "August", "September", "Oktober", "November", "Desember"],
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
    function ($stateProvider, $httpProvider, $urlRouteProvider, $mdThemingProvider) {

        $urlRouteProvider.otherwise("/");

        $stateProvider
            .state('login', {
                url: "/login",
                views: {
                    'content@': {
                        templateUrl: "app/components/login/login.html",
                        controller: 'LoginCtrl'
                    }
                }
            })
            .state('servicerutine', {
                url: "/{serviceRutineName}/",
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
                        templateUrl: "app/shared/side-navigator/side-navigator-sr.html",
                        controller: 'SideNavigatorCtrl'
                    }
                }
            })

            .state('root', {
                url: '/',
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
                        templateUrl: "app/components/service-rutine/welcome.html",
                        controller: 'ServiceRutineCtrl'
                    },
                    'header@': {
                        templateUrl: "app/shared/header/header.html",
                        controller: 'HeaderCtrl'
                    },
                    'side-navigator@': {
                        templateUrl: "app/shared/side-navigator/side-navigator-sr.html",
                        controller: 'SideNavigatorCtrl'
                    }
                }
            });

        $httpProvider.defaults.headers.common["X-Requested-With"] = "XMLHttpRequest";

        var extended_grey = $mdThemingProvider.extendPalette('grey', {'50': '#ffffff'});
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

/* Custom Filters */
app.filter('startFrom', function () {
    return function (input, start) {
        if (input) {
            start = +start;     //Parsing to int.
            return input.slice(start);
        }
        return [];
    };
});

app.filter('notEmpty', function (){
  return function (inputObject) {
      var outputObject = {};
      for(var key in inputObject){
          if(!angular.equals(inputObject[key], {})){
              outputObject[key] = inputObject[key];
          }
      }
      return outputObject;
  };
});
