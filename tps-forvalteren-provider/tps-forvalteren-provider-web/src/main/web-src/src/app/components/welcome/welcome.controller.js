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

            $scope.openVisSkdEndringsmelding = function () {
                locationService.redirectToSkdEndringsmeldingGrupper();
            };

            $scope.visTestdataKnapp = !$scope.$resolve.environmentsPromise.productionMode;
            $scope.visSkdEndringsmeldingKnapp = $scope.$resolve.environmentsPromise.roles.indexOf("ROLE_TPSF_SKDMELDING") >= 0 && !$scope.$resolve.environmentsPromise.productionMode;
        }]);
