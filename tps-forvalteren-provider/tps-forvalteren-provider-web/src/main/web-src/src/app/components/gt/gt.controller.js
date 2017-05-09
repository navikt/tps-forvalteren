angular.module('tps-forvalteren.gt')
    .controller('GTCtrl', ['$scope', '$mdDialog', '$state', 'utilsService', 'environmentsPromise',
        function ($scope, $mdDialog, $state,  utilsService, environmentsPromise) {

            $scope.submit = function () {
                $scope.formData.aksjonsKode = "B1";
                var params = utilsService.createParametersFromFormData($scope.formData);
                serviceRutineFactory.getServiceRutineResponse("FS03-FDNUMMER-KERNINFO-O", params).then(function (res) {

                    }, function (error) {
                        console.error(error);
                    }
                );
            };

            function init() {
                if (environmentsPromise) {
                    $scope.environments = utilsService.sortEnvironments(serviceRutineFactory.getEnvironments());
                }
            }

            init();
        }]);

