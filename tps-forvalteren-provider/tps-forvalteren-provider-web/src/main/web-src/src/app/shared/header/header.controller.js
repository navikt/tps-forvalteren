
angular.module('tps-forvalteren')
    .controller('HeaderCtrl', ['$scope', '$mdDialog', 'authenticationService', 'locationService', 'appInfoService', 'utilsService',
        function ($scope, $mdDialog, authenticationService, locationService, appInfoService, utilsService) {

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

            $scope.om = function () {
                var confirm = $mdDialog.confirm()
                    .title('Om TPS-Forvalteren')
                    .htmlContent('<table><tr><td>Versjon:</td><td>' + $scope.appInfo.applicationVersion + '</td></tr>' +
                                 '<tr><td>Miljø:</td><td>' + $scope.appInfo.environment.toUpperCase() + '</td></tr>' +
                                 '<tr><td>Vertsmaskin:</td><td>' + $scope.appInfo.hostName + '</td></tr></table>')
                    .ariaLabel('Detaljer om TPS-forvalteren')
                    .ok('OK')
                    .clickOutsideToClose(true);
                $mdDialog.show(confirm);
            };

            appInfoService.getInfo().then(function (result) {
                $scope.appInfo = result.data;
            }, function (error) {
                 utilsService.showAlertError(error);
            });
        }]);
