angular.module('tps-forvalteren.gt')
    .controller('GTCtrl', ['$scope', '$mdDialog', '$state', 'utilsService', 'environmentsPromise','serviceRutineFactory',
        function ($scope, $mdDialog, $state,  utilsService, environmentsPromise, serviceRutineFactory) {

            $scope.submit = function () {
                $scope.formData.aksjonsKode = "B0";
                var params = utilsService.createParametersFromFormData($scope.formData);
                serviceRutineFactory.getServiceRutineResponse("FS03-FDNUMMER-KERNINFO-O", params).then(function (res) {
                        var response = res.data.response;
                        $scope.gt = response.data[0];
                        $scope.xml = response.xml;
                        prepStatus(response);
                    }, function (error) {
                        console.error(error);
                    }
                );
            };

            var prepStatus = function (response) {
                $scope.okStatus = response.status.kode == '00';

                var svarStatus = "Status: " + response.status.kode;
                if (!$scope.okStatus) {
                    svarStatus += "; Melding: " + response.status.melding + ": " + response.status.utfyllendeMelding;
                }
                $scope.statusMld = svarStatus;
                $scope.antallTreff = $scope.okStatus ? response.data.length : 0;
            };


            if (environmentsPromise) {
                $scope.environments = utilsService.sortEnvironments(serviceRutineFactory.getEnvironments());
            }
        }]);

