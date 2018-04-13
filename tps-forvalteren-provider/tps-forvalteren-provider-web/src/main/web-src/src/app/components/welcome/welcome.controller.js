angular.module('tps-forvalteren.welcome',[])
    .controller('WelcomeCtrl', ['$scope', 'locationService',
        function ($scope, locationService) {

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

            $scope.visTestdataKnapp = !$scope.$resolve.environmentsPromise.productionMode;
            $scope.visSkdEndringsmeldingKnapp = !$scope.$resolve.environmentsPromise.productionMode;
            $scope.visServiceRutineKnapp = $scope.$resolve.environmentsPromise.roles.indexOf("ROLE_TPSF_SERVICERUTINER") >= 0;
            $scope.visSendDoedsmeldingKnapp = !$scope.$resolve.environmentsPromise.productionMode;
    }]);
