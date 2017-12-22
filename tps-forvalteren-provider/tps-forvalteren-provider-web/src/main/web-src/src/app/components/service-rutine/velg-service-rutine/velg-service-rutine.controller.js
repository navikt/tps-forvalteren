angular.module('tps-forvalteren.service-rutine.velg-service-rutine', ['ngMaterial'])
    .controller('VelgServiceRutineCtrl', ['$scope', '$mdDialog', 'serviceRutineFactory', 'testdataService', '$location', 'utilsService', 'locationService',
        function ($scope, $mdDialog, serviceRutineFactory, testdataService, $location, utilsService, locationService) {


            $scope.avbryt = function () {
                $mdDialog.hide();
            };
            $scope.send = function (rutine) {

                $mdDialog.hide();
                locationService.redirectToServiceRutineState(rutine);

            };

            $scope.range = function(n) {
                 console.log("testing");
                return new Array(n);
            };


        }]);