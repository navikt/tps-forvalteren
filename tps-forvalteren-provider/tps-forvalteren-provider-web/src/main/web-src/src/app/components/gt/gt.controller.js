angular.module('tps-forvalteren.gt')
    .controller('GTCtrl', ['$scope', '$mdDialog', '$state', 'utilsService', 'environmentsPromise','serviceRutineFactory',
        function ($scope, $mdDialog, $state,  utilsService, environmentsPromise, serviceRutineFactory) {

            $scope.submit = function () {
                $scope.formData.aksjonsKode = "B0";
                nullstill();
                var params = utilsService.createParametersFromFormData($scope.formData);
                serviceRutineFactory.getServiceRutineResponse("FS03-FDNUMMER-KERNINFO-O", params).then(function (res) {
                        $scope.gt = res.data.response.data[0];
                        $scope.xmlForm = utilsService.formatXml(res.data.xml);
                        prepStatus(res.data.response);
                    }, function (error) {
                        showAlertTPSError(error);
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

            var nullstill = function() {
                $scope.gt = undefined;
                $scope.xmlForm = undefined;
                $scope.okStatus = undefined;
                $scope.statusMld = undefined;
                $scope.antallTreff = undefined;
            };

            function showAlertTPSError(error) {
                var errorMessages = {
                    401: {
                        title: 'Ikke autorisert',
                        text: 'Din bruker har ikke tillatelse til denne spørringen.',
                        ariaLabel: 'Din bruker har ikke tillatelse til denne spørringen.'
                    },
                    500: {
                        title: 'Serverfeil',
                        text: 'Fikk ikke hentet informasjon om TPS fra server.',
                        ariaLabel: 'Feil ved henting av data fra TPS'
                    }
                };

                var errorObj = error.status == 401 ? errorMessages[401] : errorMessages[500];
                $mdDialog.show(
                    $mdDialog.alert()
                        .title(errorObj.title)
                        .textContent(errorObj.text)
                        .ariaLabel(errorObj.ariaLabel)
                        .ok('OK')
                );
            }

            if (environmentsPromise) {
                $scope.environments = utilsService.sortEnvironments(serviceRutineFactory.getEnvironments());
            }
        }]);

