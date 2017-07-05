
angular.module('tps-forvalteren')
    .controller('HeaderCtrl', ['$scope', 'authenticationService', 'locationService',
        function ($scope, authenticationService, locationService) {

            $scope.visTestdataKnapp = false;

            $scope.logout = function () {
                authenticationService.invalidateSession(function () {
                    locationService.redirectToLoginState();
                });
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
                locationService.redirectToHomeState();
            };

            $scope.isRoot = locationService.isRoot();

            var environment = $scope.$resolve.environmentsPromise;
            var prodEnvironment = false;
            for (i in environment) {
                if (environment[i] == 'p') {
                    prodEnvironment = true;
                }
            }
            $scope.visTestdataKnapp = !prodEnvironment;
        }]);
