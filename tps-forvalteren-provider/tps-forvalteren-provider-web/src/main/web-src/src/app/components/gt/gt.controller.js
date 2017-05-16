angular.module('tps-forvalteren.gt')
    .controller('GTCtrl', ['$scope', '$mdDialog', '$state', '$q', 'utilsService', 'environmentsPromise', 'serviceRutineFactory',
        function ($scope, $mdDialog, $state, $q, utilsService, environmentsPromise, serviceRutineFactory) {

            $scope.isArray = angular.isArray;

            $scope.submit = function () {
                hentServiceRutinePromise();
            };

            function hentServiceRutine(serviceRutineNavn) {
                var params = utilsService.createParametersFromFormData($scope.formData);
                serviceRutineFactory.getServiceRutineResponse(serviceRutineNavn, params).then(function (res) {
                        $scope.gt = res.data.response.data[0];
                        $scope.xmlForm = utilsService.formatXml(res.data.xml);
                        prepStatus(res.data.response);
                    }, function (error) {
                        console.error(error);
                    }
                );
            }

            function hentServiceRutinePromise() {
                $scope.formData.aksjonsKode = "B0";
                var params = utilsService.createParametersFromFormData($scope.formData);
                var kjernePromise = serviceRutineFactory.getServiceRutineResponse("FS03-FDNUMMER-KERNINFO-O", params);
                var adressePromise = serviceRutineFactory.getServiceRutineResponse("FS03-FDNUMMER-ADRHISTO-O", params);

                $scope.formData.aksjonsKode = "A0";
                $scope.formData.adresseType = "ALLE";
                params = utilsService.createParametersFromFormData($scope.formData);
                var adresseLinjePromise = serviceRutineFactory.getServiceRutineResponse("FS03-FDNUMMER-ADLIHIST-O", params);

                $scope.formData.infoType = "ALLE";
                $scope.formData.buffNr = "1";
                params = utilsService.createParametersFromFormData($scope.formData);
                var utvandringPromise = serviceRutineFactory.getServiceRutineResponse("FS03-FDNUMMER-SOAIHIST-O", params);

                $q.all([kjernePromise, adressePromise, adresseLinjePromise, utvandringPromise]).then(function (res) {

                    var gtResult = res[0].data.response;
                    $scope.gt = gtResult.data[0];

                    var hisResult = res[1].data.response;
                    $scope.adresseHistorikk = hisResult.data[0];

                    var adresseLinjeResult = res[2].data.response;
                    $scope.adresseLinjer = adresseLinjeResult.data[0];
                    console.log($scope.adresseLinjer);

                    var utvandringResult = res[3].data.response;
                    $scope.utvandring = utvandringResult.data[0];
                    console.log($scope.utvandring);

                });
            }


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

