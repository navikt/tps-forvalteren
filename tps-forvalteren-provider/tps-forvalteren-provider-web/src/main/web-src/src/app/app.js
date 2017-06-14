require('angular');
require('angular-ui-router');
require('angular-aria');
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

var app = angular.module('tps-forvalteren', ['ui.router', 'ngMaterial', 'ngMessages', 'ngMdIcons', 'angularMoment', 'tps-forvalteren.login',
    'tps-forvalteren.service', 'tps-forvalteren.factory', 'tps-forvalteren.service-rutine', 'tps-forvalteren.directives', 'tps-forvalteren.gt',
    'tps-forvalteren.opprett-testdata', 'tps-forvalteren.vis-testdata', 'pikaday']);

require('./shared/index');

app.config(['pikadayConfigProvider', 'moment', '$mdDateLocaleProvider', function (pikaday, moment, $mdDateLocaleProvider) {

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

    $mdDateLocaleProvider.shortMonths = ['Jan','Feb', 'Mar', 'Apr', 'Mai', 'Jun', 'Jul', 'Aug', 'Sep', 'Okt', 'Nov', 'Des'];
    $mdDateLocaleProvider.shortDays = ['Sø', 'Ma', 'Ti', 'On', 'To', 'Fr', 'Lø'];
    $mdDateLocaleProvider.msgCalendar = 'Kalender';
    $mdDateLocaleProvider.lastRenderableDate = new Date();
    $mdDateLocaleProvider.firstDayOfWeek = 1;
    $mdDateLocaleProvider.formatDate = function(date) {
        var m = moment(date);
        return m.isValid() ? m.format('DD-MM-YYYY') : new Date(NaN);
    };
    $mdDateLocaleProvider.parseDate = function(dateString) {
        var m = moment(dateString, 'DD-MM-YYYY', true);
        return m.isValid() ? m.toDate() : '';
    };
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

            .state('gt', {
                url: "/gt",
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
                        templateUrl: "app/components/gt/gt.html",
                        controller: 'GTCtrl'
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

            .state('vis-testdata', {
                url: "/vis-testdata",
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
                        templateUrl: "app/components/vis-testdata/vis-testdata.html",
                        controller: 'VisTestdataCtrl'
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

            .state('opprett-testdata', {
                url: "/opprett-testdata",
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
                        templateUrl: "app/components/opprett-testdata/opprett-testdata.html",
                        controller: 'OpprettTestdataCtrl'
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

            .state('servicerutine', {
                url: "/{serviceRutineName}",
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
                }
            );

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

app.filter('startFromKey', function () {
    return function (inputObject, startKey) {
        var outputObject = {};
        var reachedKey = false;
        for(var key in inputObject){
           if(key == startKey){
               reachedKey = true;
           }
           if(reachedKey){
               outputObject[key] = inputObject[key];
           }
        }
        return outputObject;
    };
});

app.filter('startFromIndex', function () {
    return function (inputObject, startIndex) {
        return inputObject.slice(startIndex);
    };
});

app.filter('removeDuplicateKeys', function () {
    return function (inputObject, objectComp) {
        var outputObject = {};
        for(var i in inputObject){
            jsonObject = inputObject[i];
            if(jsonObject.fieldData.indexOf("[") ){
                var res = jsonObject.fieldData.split("[");
                jsonObject.fieldData = res[0];
            }
            if(!objectComp.hasOwnProperty(jsonObject.fieldData) && !objectComp.hasOwnProperty()){
               outputObject[jsonObject.fieldData] = inputObject[jsonObject.fieldData];
            }
        }
        return outputObject;
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
