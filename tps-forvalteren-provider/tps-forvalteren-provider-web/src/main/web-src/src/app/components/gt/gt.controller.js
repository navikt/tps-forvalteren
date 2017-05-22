angular.module('tps-forvalteren.gt')
    .controller('GTCtrl', ['$scope', '$mdDialog', '$state', '$q', 'utilsService', 'environmentsPromise', 'serviceRutineFactory',
        function ($scope, $mdDialog, $state, $q, utilsService, environmentsPromise, serviceRutineFactory) {

            $scope.isArray = angular.isArray;

            $scope.submit = function () {
                executeServiceRutiner();
            };

            function executeServiceRutiner() {
                $scope.loading = true;
                nullstillOutputFields();
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

                    $scope.loading = false;

                    var gtResult = res[0].data.response;
                    $scope.gt = gtResult.data1;
                    $scope.xmlFormGT = utilsService.formatXml(res[0].data.xml);
                    $scope.gtStatus = gtResult.status;
                    prepFooter(gtResult);

                    var hisResult = res[1].data.response;
                    $scope.adresseHistorikk = hisResult.data1;
                    $scope.xmlFormAdrHist = utilsService.formatXml(res[1].data.xml);
                    $scope.adrHistStatus = hisResult.status;

                    var adresseLinjeResult = res[2].data.response;
                    $scope.adresseLinjer = adresseLinjeResult.data1;
                    $scope.xmlFormAdrLinje = utilsService.formatXml(res[2].data.xml);
                    $scope.adrLinjeStatus = adresseLinjeResult.status;

                    var utvandringResult = res[3].data.response;
                    $scope.utvandring = utvandringResult.data;
                    $scope.xmlFormUtvandring = utilsService.formatXml(res[3].data.xml);
                    $scope.utvandringStatus = utvandringResult.status;

                },function (error) {
                    showAlertTPSError(error);
                });
            }


            var prepFooter = function (response) {
                $scope.status = $scope.gtStatus;
                $scope.treff = response.data ? response.data.length : 0;
            };

            var nullstillOutputFields = function() {
                $scope.gt = undefined;
                $scope.xmlForm = undefined;
                $scope.status = undefined;
                $scope.treff = undefined;
                $scope.adresseHistorikk = undefined;
                $scope.xmlFormAdrHist = undefined;
                $scope.adrHistStatus = undefined;
                $scope.adresseLinjer = undefined;
                $scope.xmlFormAdrLinje = undefined;
                $scope.adrLinjeStatus = undefined;
                $scope.utvandring = undefined;
                $scope.xmlFormUtvandring = undefined;
                $scope.utvandringStatus = undefined;
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

