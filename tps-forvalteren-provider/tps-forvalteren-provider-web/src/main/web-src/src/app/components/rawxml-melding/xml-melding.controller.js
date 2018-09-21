angular.module('tps-forvalteren.rawxml-melding', ['ngMaterial'])
    .controller('RawXmlCtrl', ['$scope', '$mdDialog', '$mdConstant', 'utilsService', 'headerService', 'xmlmeldingService',
        function ($scope, $mdDialog, $mdConstant, utilsService, headerService, xmlmeldingService) {

            headerService.setHeader('Send XML-melding');

            const OWN_QUEUE = '--- Egendefinert kø ---';

            $scope.melding = '';
            $scope.tpsMessageQueueList = [];
            $scope.displayQueues = [];
            $scope.displayEnvironments = [];
            $scope.showResponse = false;
            $scope.responseMelding = "";
            $scope.timeout = 5;

            $scope.onChangeMiljoe = function () {
                var queueList = [];

                $scope.tpsMessageQueueList.forEach(function (obj) {
                    if(obj.miljo === $scope.valgtMiljoe) {
                        queueList.push(obj.koNavn);
                    }
                });
                queueList.push(OWN_QUEUE);
                $scope.valgtKoe = queueList[0];
                $scope.displayQueues = queueList;
                $scope.showOwnQueue = false;
            };

            $scope.onChangeQueue = function () {
                $scope.tpsMessageQueueList.forEach(function (obj) {
                    if(obj.koNavn === $scope.valgtKoe) {
                        $scope.valgtMiljoe = obj.miljo;
                    }
                });
                $scope.showOwnQueue = $scope.valgtKoe === OWN_QUEUE;
            };

            $scope.sendTilTps = function () {
                var queue = $scope.valgtKoe;
                if ($scope.valgtKoe === OWN_QUEUE) {
                    queue = $scope.egenkoe;
                }
                var objectToTps = {
                    miljoe: $scope.valgtMiljoe,
                    melding: $scope.melding,
                    ko: queue,
                    timeout: $scope.timeout
                };

                $scope.responseMelding = "";
                $scope.showResponse = false;
                xmlmeldingService.send(objectToTps).then(function(response){
                    $scope.showResponse = true;
                    if (response.config.data.melding.indexOf("<?xml version") !== -1) {
                        $scope.responseMelding = utilsService.formatXml(response.data.xml);
                    } else {
                        $scope.responseMelding = response.data.xml;
                    }
                }, function (error) {
                    utilsService.showAlertError(error);
                });
            };

            function removeDuplicateEnvironments() {
                var environments = [];

                $scope.tpsMessageQueueList.forEach(function (obj) {
                    if(environments.length === 0 ) {
                        environments.push(obj.miljo);
                    } else if(environments.length > 0 && environments.indexOf(obj.miljo) < 0) {
                        environments.push(obj.miljo);
                    }
                });

                return environments;
            }

            function sortEnvironmentsForDisplay(environments) {
                var filteredEnvironments = {};
                var sortedEnvironments = [];

                environments.sort(function (a, b) {
                    return a.substring(1) - b.substring(1);
                });

                angular.forEach(environments, function (env) {
                    var substrMiljoe = env.charAt(0);

                    if(filteredEnvironments[substrMiljoe]) {
                        filteredEnvironments[substrMiljoe].push(env);
                    } else {
                        filteredEnvironments[substrMiljoe] = [];
                        filteredEnvironments[substrMiljoe].push(env);
                    }
                });

                if (filteredEnvironments['u']) {
                    angular.forEach(filteredEnvironments['u'], function (env) {
                        sortedEnvironments.push(env);
                    });
                }

                if (filteredEnvironments['t']) {
                    angular.forEach(filteredEnvironments['t'], function (env) {
                        sortedEnvironments.push(env);
                    });
                }

                if (filteredEnvironments['q']) {
                    angular.forEach(filteredEnvironments['q'], function (env) {
                        sortedEnvironments.push(env);
                    });
                }

                return sortedEnvironments;
            }

            function hentAlleMiljoerOgKoer() {

                xmlmeldingService.hentKoer().then(function (result) {
                    var environments = [];
                    $scope.tpsMessageQueueList = result.data;

                    $scope.tpsMessageQueueList.forEach(function (obj) {
                        $scope.displayQueues.push(obj.koNavn);
                        environments.push(obj.miljo);
                    });

                    environments = removeDuplicateEnvironments();

                    $scope.displayEnvironments = sortEnvironmentsForDisplay(environments);
                });
            }

            hentAlleMiljoerOgKoer();

        }]);