angular.module('tps-forvalteren.welcome',[])
    .controller('WelcomeCtrl', ['$scope', 'locationService',
        function ($scope, locationService) {

            $scope.visTestdataKnapp = false;

            $scope.openServiceRutine = function () {
                locationService.redirectToServiceRutineState();
            };

            $scope.openGT = function () {
                locationService.redirectToGT();
            };

            $scope.openVisTestdata = function () {
                locationService.redirectToTestgruppe();
            };

            $scope.visTestdataKnapp = !$scope.$resolve.environmentsPromise.productionMode;
        }]);
