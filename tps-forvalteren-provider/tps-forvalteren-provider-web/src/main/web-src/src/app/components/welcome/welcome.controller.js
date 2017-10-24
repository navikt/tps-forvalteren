angular.module('tps-forvalteren.welcome',[])
    .controller('WelcomeCtrl', ['$scope', 'locationService',
        function ($scope, locationService) {

            $scope.visTestdataKnapp = false;
            $scope.visSkdEndringsmeldingKnapp = false;

            $scope.serviceRutineState = function () {
                locationService.redirectToServiceRutineState();
            };

            $scope.openGT = function () {
                locationService.redirectToGT();
            };

            $scope.openVisTestdata = function () {
                locationService.redirectToTestgruppe();
            };

            $scope.openVisEndringsmelding = function () {
                locationService.redirectToSkdEndringsmeldingGrupper();
            };

            $scope.visTestdataKnapp = !$scope.$resolve.environmentsPromise.productionMode;
            $scope.visSkdEndringsmeldingKnapp = !$scope.$resolve.environmentsPromise.productionMode;
        }]);
