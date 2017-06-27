
angular.module('tps-forvalteren')
    .controller('HeaderCtrl', ['$scope', 'authenticationService', 'locationService', '$mdSidenav', 'serviceRutineFactory',
        function ($scope, authenticationService, locationService, $mdSidenav, serviceRutineFactory) {

            $scope.visTestdataKnapp = false;

            $scope.logout = function () {
                authenticationService.invalidateSession(function () {
                    locationService.redirectToLoginState();
                });
            };

            $scope.toggleSideNav = function (menuId) {
                $mdSidenav(menuId).toggle();
            };

            $scope.endringState = function () {
                locationService.redirectToEndringState();
            };

            $scope.serviceRutineState = function () {
                locationService.redirectToServiceRutineState();
            };

            $scope.openGT = function () {
                locationService.redirectToGT();
            };

            $scope.openVisTestdata = function () {
                locationService.redirectToTestgruppe();
            };

            $scope.goBack = function () {
                window.history.back();
            };

            $scope.goHome = function () {
                locationService.redirectToLoginReturnState();
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

            $scope.isRoot = locationService.isRoot();
        }]);
