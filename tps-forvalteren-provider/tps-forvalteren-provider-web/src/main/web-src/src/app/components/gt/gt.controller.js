angular.module('tps-forvalteren.gt')
    .controller('GTCtrl', ['$scope', '$mdDialog', '$state', '$q', 'utilsService', 'environmentsPromise', 'serviceRutineFactory',
        function ($scope, $mdDialog, $state, $q, utilsService, environmentsPromise, serviceRutineFactory) {

            $scope.isArray = angular.isArray;

            $scope.submit = function () {
                hentServiceRutinePromise();
            };

            function hentServiceRutine(serviceRutineNavn) {
                $scope.formData.aksjonsKode = "B0";
                nullstill();
                var params = utilsService.createParametersFromFormData($scope.formData);
                serviceRutineFactory.getServiceRutineResponse(serviceRutineNavn, params).then(function (res) {
                        $scope.gt = res.data.response.data[0];
                        $scope.xmlForm = utilsService.formatXml(res.data.xml);
                        prepFooter(res.data.response);
                    }, function (error) {
                        showAlertTPSError(error);
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
                    $scope.xmlFormGT = utilsService.formatXml(res[0].data.xml);
                    $scope.gtStatus = gtResult.status;
                    prepFooter(gtResult);

                    var hisResult = res[1].data.response;
                    $scope.adresseHistorikk = hisResult.data[0];
                    $scope.xmlFormAdrHist = utilsService.formatXml(res[1].data.xml);
                    $scope.adrHistStatus = hisResult.status;

                    var adresseLinjeResult = res[2].data.response;
                    $scope.adresseLinjer = adresseLinjeResult.data[0];
                    $scope.xmlFormAdrLinje = utilsService.formatXml(res[2].data.xml);
                    $scope.adrLinjeStatus = adresseLinjeResult.status;

                    var utvandringResult = res[3].data.response;
                    $scope.utvandring = utvandringResult.data[0];
                    $scope.xmlFormUtvandring = utilsService.formatXml(res[3].data.xml);
                    $scope.utvandringStatus = utvandringResult.status;

                });
            }


            var prepFooter = function (response) {
                $scope.status = response.status;
                $scope.treff = response.data ? response.data.length : 0;
            };

            var nullstill = function() {
                $scope.gt = undefined;
                $scope.xmlForm = undefined;
                $scope.status = undefined;
                $scope.treff = undefined;
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

