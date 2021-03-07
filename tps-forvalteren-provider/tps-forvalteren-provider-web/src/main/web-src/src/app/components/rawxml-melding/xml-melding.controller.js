angular.module('tps-forvalteren.rawxml-melding', ['ngMaterial'])
    .controller('RawXmlCtrl', ['$scope', '$mdDialog', '$mdConstant', 'utilsService', 'headerService', 'xmlmeldingService',
        function ($scope, $mdDialog, $mdConstant, utilsService, headerService, xmlmeldingService) {

            headerService.setHeader('Send XML-melding');

            $scope.melding = '';
            $scope.displayQueues = [];
            $scope.displayEnvironments = [];
            $scope.showResponse = false;
            $scope.responseMelding = "";
            $scope.timeout = 5;
            $scope.applications = [];
            var appobjects = [];

            $scope.findApps = function (inp) {

                xmlmeldingService.hentApplikasjoner(inp).then(function (result) {

                    $scope.applications = [];
                    result.data.forEach(function (app) {
                        if (!utilsService.arrayContains($scope.applications, app.name)) {
                            $scope.applications.push(app.name);
                        }
                    });

                }, function(error) {
                    utilsService.showAlertError(error);
                });
            };

            $scope.onChangeApp = function () {

                xmlmeldingService.hentAppRessurser($scope.valgtApp).then(function (result) {

                    prepAppResources(result.data);
                });
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

            $scope.sendTilTps = function () {
                var objectToTps = {
                    miljoe: $scope.egenkoe.substring(3,5),
                    melding: $scope.melding,
                    ko: $scope.egenkoe,
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

        }]);