angular.module('tps-forvalteren')
    .controller('HeaderCtrl', ['$scope', '$mdDialog', 'authenticationService', 'locationService', 'appInfoService', 'utilsService',
        function ($scope, $mdDialog, authenticationService, locationService, appInfoService, utilsService) {

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

            $scope.openVisSkdEndringsmelding = function () {
                locationService.redirectToSkdEndringsmeldingGrupper();
            };

            $scope.openServiceRutine = function () {
                locationService.redirectToServiceRutineState();
            };

            $scope.goHome = function () {
                locationService.redirectToHomeState();
            };

            $scope.openVisSendDoedsmelding = function () {
                locationService.redirectToSendDoedsmeldinger();
            };

            $scope.isRoot = locationService.isRoot();

            $scope.visGTKnapp = $scope.$resolve.environmentsPromise.roles["hasGT"] ||
                $scope.$resolve.environmentsPromise.roles["hasTST"];
            $scope.visServiceRutineKnapp = $scope.$resolve.environmentsPromise.roles["hasSRV"];
            $scope.visTestdataKnapp = !$scope.$resolve.environmentsPromise.productionMode &&
                $scope.$resolve.environmentsPromise.roles["hasTST"];
            $scope.visSendDoedsmeldingKnapp = !$scope.$resolve.environmentsPromise.productionMode &&
                $scope.$resolve.environmentsPromise.roles["hasTST"];
            $scope.visSkdEndringsmeldingKnapp = $scope.$resolve.environmentsPromise.roles["hasMLD"];

            $scope.$on('updateEvent', function () {
                if ($scope.header && $scope.header.buttons) {
                    $scope.header.buttons.forEach(function (button) {
                        if (button.disabled) {
                            button.status = button.disabled();
                        }
                    });
                }
            });

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
