angular.module('tps-forvalteren.service-rutine.velg-service-rutine', ['ngMaterial'])
    .controller('VelgServiceRutineCtrl', ['$scope', '$mdDialog', 'locationService',
        function ($scope, $mdDialog, locationService) {


            $scope.avbryt = function () {
                $mdDialog.hide();
            };
            $scope.send = function (rutine) {
                $mdDialog.hide();
                locationService.redirectToServiceRutineState(rutine);
            };

            $scope.openVisXmlMelding = function () {
                $mdDialog.hide();
                locationService.redirectToRawXmlMelding();
            };

            $scope.enableXmlMld = !$scope.$resolve.environmentsPromise.productionMode;
        }]);