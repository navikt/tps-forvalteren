angular.module('tps-forvalteren.welcome')
    .controller('WelcomeCtrl', ['$scope', 'locationService',
        function ($scope, locationService) {

            $scope.visTestdataKnapp = false;

            $scope.serviceRutineState = function () {
                locationService.redirectToServiceRutineState();
            };

            $scope.openGT = function () {
                locationService.redirectToGT();
            };

            $scope.openVisTestdata = function () {
                locationService.redirectToTestgruppe();
            };

            var environment = $scope.$resolve.environmentsPromise;
            var prodEnvironment = false;
            for (i in environment) {
                if (environment[i] == 'p') {
                    prodEnvironment = true;
                }
            }
            $scope.visTestdataKnapp = !prodEnvironment;
        }]);
