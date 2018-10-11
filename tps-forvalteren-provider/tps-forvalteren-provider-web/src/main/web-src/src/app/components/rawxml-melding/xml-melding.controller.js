angular.module('tps-forvalteren.rawxml-melding', ['ngMaterial'])
    .controller('RawXmlCtrl', ['$scope', '$mdDialog', '$mdConstant', 'utilsService', 'headerService', 'xmlmeldingService',
        function ($scope, $mdDialog, $mdConstant, utilsService, headerService, xmlmeldingService) {

            headerService.setHeader('Send XML-melding');

            const OWN_QUEUE = '--- Egendefinert kø ---';

            $scope.melding = '';
            $scope.displayQueues = [];
            $scope.displayEnvironments = [];
            $scope.showResponse = false;
            $scope.responseMelding = "";
            $scope.timeout = 5;
            $scope.applications = [];
            var appobjects = [];

            $scope.onChangeApp = function () {

                xmlmeldingService.hentAppRessurser($scope.valgtApp).then(function (result) {

                    prepAppResources(result.data);
                });
            };

            $scope.clicker = function (inp) {

                xmlmeldingService.hentAppRessurser(inp).then(function (result) {

                    $scope.applications = [];
                    result.data.forEach(function (app) {
                        if (!utilsService.arrayContains($scope.applications, app.appnavn)) {
                            $scope.applications.push(app.appnavn);
                        }
                    });
                    prepAppResources(result.data);
                })
            };

            function prepAppResources(result) {
                appobjects = result;
                var environments = [];
                var queues = [];

                appobjects.forEach(function (app) {
                    if (!utilsService.arrayContains(environments, app.environment)) {
                        environments.push(app.environment);
                    }
                    app.queues.forEach(function (que) {
                        if (!utilsService.arrayContains(queues, que.queueName)) {
                            queues.push(que.queueName);
                        }
                    });
                });

                $scope.displayQueues = queues;
                $scope.displayEnvironments = sortEnvironmentsForDisplay(environments);
            }

            $scope.onChangeMiljoe = function () {
                var queueList = [];

                appobjects.forEach(function (obj) {
                    if ($scope.valgtApp === obj.appnavn && obj.environment === $scope.valgtMiljoe) {
                        obj.queues.forEach(function (que) {
                            if (!utilsService.arrayContains(queueList, que.queueName)) {
                                queueList.push(que.queueName);
                            }
                        });
                    }
                });
                queueList.push(OWN_QUEUE);
                $scope.valgtKoe = queueList[0];
                $scope.displayQueues = queueList;
                $scope.showOwnQueue = false;
            };

            $scope.onChangeQueue = function () {
                appobjects.forEach(function (obj) {
                    if (obj.koNavn === $scope.valgtKoe) {
                        $scope.valgtMiljoe = obj.environment;
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
                xmlmeldingService.send(objectToTps).then(function (response) {
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

            function sortEnvironmentsForDisplay(environments) {
                var filteredEnvironments = {};
                var sortedEnvironments = [];

                environments.sort(function (a, b) {
                    return a.substring(1) - b.substring(1);
                });

                angular.forEach(environments, function (env) {
                    var substrMiljoe = env.charAt(0);

                    if (filteredEnvironments[substrMiljoe]) {
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

            function hentApplikasjonerFraFasit() {
                xmlmeldingService.hentApplikasjoner().then(function (result) {

                    angular.forEach(result.data, function (app) {
                        $scope.applications.push(app.name);
                    });
                });
            }

            hentApplikasjonerFraFasit();
        }]);