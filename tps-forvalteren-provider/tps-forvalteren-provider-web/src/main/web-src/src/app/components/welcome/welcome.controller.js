angular.module('tps-forvalteren.welcome',[])
    .controller('WelcomeCtrl', ['$scope', 'locationService', 'authenticationService',
        function ($scope, locationService, authenticationService) {

            $scope.openServiceRutine = function () {
                locationService.redirectToServiceRutineState();
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

            $scope.openVisSendDoedsmeldinger = function () {
                locationService.redirectToSendDoedsmeldinger();
            };

            $scope.visGTKnapp = !$scope.$resolve.environmentsPromise.productionMode || $scope.$resolve.environmentsPromise.roles["hasGT"];
            $scope.visTestdataKnapp = !$scope.$resolve.environmentsPromise.productionMode;
            $scope.visSendDoedsmeldingKnapp = !$scope.$resolve.environmentsPromise.productionMode;
            $scope.visSkdEndringsmeldingKnapp = !$scope.$resolve.environmentsPromise.productionMode || $scope.$resolve.environmentsPromise.roles["hasMLD"];
            $scope.visServiceRutineKnapp = !$scope.$resolve.environmentsPromise.productionMode || $scope.$resolve.environmentsPromise.roles["hasSRV"];
    }]);