angular.module('tps-forvalteren.gt', ['ngMessages', 'hljs'])
    .controller('GTCtrl', ['$scope', '$q', 'utilsService', 'environmentsPromise', 'gtService', 'headerService',
        function ($scope, $q, utilsService, environmentsPromise, gtService, headerService) {

            headerService.setHeader('Geografisk Tilknytning');

            $scope.isArray = angular.isArray;

            $scope.submit = function () {
                executeServiceRutiner();
            };

            function executeServiceRutiner() {
                $scope.loading = true;
                nullstillOutputFields();
                var params = utilsService.createParametersFromFormData($scope.formData);

                $q.all([
                    gtService.getKjerneinfo(params),
                    gtService.getAdressehistorikk(params),
                    gtService.getAdresselinjehistorikk(params),
                    gtService.getSoaihistorikk(params)]
                ).then(function (res) {

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
                    $scope.utvandring = utvandringResult.data1;
                    $scope.xmlFormUtvandring = utilsService.formatXml(res[3].data.xml);
                    $scope.utvandringStatus = utvandringResult.status;

                }, function (error) {
                    utilsService.showAlertError(error);
                    $scope.loading = false;
                });
            }


            var prepFooter = function (response) {
                $scope.status = $scope.gtStatus;
                $scope.treff = response.data ? response.data.length : 0;
            };

            var nullstillOutputFields = function () {
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

            function sortEnvironmentsForDisplay(environments) {
                var filteredEnvironments = {};
                var sortedEnvironments = [];

                var addEnvironment = function (environment) {
                    if (filteredEnvironments[environment]) {
                        angular.forEach(filteredEnvironments[environment], function (env) {
                            sortedEnvironments.push(env);
                        });
                    }
                };

                environments = utilsService.sortEnvironments(environments);

                angular.forEach(environments, function (env) {
                    var substrMiljoe = env.charAt(0);
                    filteredEnvironments[substrMiljoe] = filteredEnvironments[substrMiljoe] ? filteredEnvironments[substrMiljoe] : [];
                    filteredEnvironments[substrMiljoe].push(env);
                });

                addEnvironment('u');
                addEnvironment('t');
                addEnvironment('q');
                addEnvironment('p');

                return sortedEnvironments;
            }

            var environments = $scope.$resolve.environmentsPromise;
            if (environments.status !== undefined) {
                utilsService.showAlertError(environments);
            } else {
                $scope.environments = sortEnvironmentsForDisplay(environments.environments);
            }

        }]);