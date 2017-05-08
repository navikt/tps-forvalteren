angular.module('tps-forvalteren')
    .controller('GTCtrl', ['$scope', '$mdDialog', '$state', 'serviceRutineFactory', 'serviceRutinesPromise', 'utilsService', 'environmentsPromise', 'locationService',
        function ($scope, $mdDialog, $state, serviceRutineFactory, serviceRutinesPromise, utilsService, environmentsPromise, locationService) {

            $scope.isServicerutineState = function () {
                return locationService.isServicerutineState();
            };

            $scope.formData = {
                "environment": "",
                "fnr": "",
                "aksjonsKode": "A0"
            };

            $scope.submit = function () {
                serviceRutineFactory.getServiceRutineResponse("FS03-FDNUMMER-KERNINFO-O", $scope.formData).then(function (res) {

                    //console.log(res.)

                    }, function (error) {
                        console.log(error)
                    }
                );
            }

            function init() {
                if (environmentsPromise) {
                    $scope.environments = utilsService.sortEnvironments(serviceRutineFactory.getEnvironments());
                }
            }

            init();
        }]);

