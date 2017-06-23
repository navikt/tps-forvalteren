angular.module('tps-forvalteren.welcome')
    .controller('WelcomeCtrl', ['$scope', 'locationService', 'serviceRutineFactory',
        function ($scope, locationService, serviceRutineFactory) {

            $scope.visTestdataKnapp = false;

            $scope.serviceRutineState = function () {
                locationService.redirectToServiceRutineState();
            };

            $scope.openGT = function () {
                locationService.redirectToGT();
            };

            $scope.openVisTestdata = function () {
                locationService.redirectToVisTestdata();
            };

            serviceRutineFactory.loadFromServerEnvironments().then( function(environment) {
                var prodEnvironment = false;
                for (i in environment) {
                    if (environment[i] == 'p') {
                        prodEnvironment = true;
                    }
                }
                $scope.visTestdataKnapp = !prodEnvironment;
            });

        }]);
