angular.module('tps-forvalteren.gt')
    .controller('GTCtrl', ['$scope', '$mdDialog', '$state', 'utilsService', 'environmentsPromise','serviceRutineFactory',
        function ($scope, $mdDialog, $state,  utilsService, environmentsPromise, serviceRutineFactory) {

            $scope.submit = function () {
                $scope.formData.aksjonsKode = "B0";
                var params = utilsService.createParametersFromFormData($scope.formData);
                serviceRutineFactory.getServiceRutineResponse("FS03-FDNUMMER-KERNINFO-O", params).then(function (res) {
                        $scope.gt = res.data.response.data[0];
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

