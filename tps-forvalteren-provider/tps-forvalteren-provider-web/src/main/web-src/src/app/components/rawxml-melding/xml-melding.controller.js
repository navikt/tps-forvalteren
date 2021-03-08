angular.module('tps-forvalteren.rawxml-melding', ['ngMaterial'])
    .controller('RawXmlCtrl', ['$scope', '$mdDialog', '$mdConstant', 'utilsService', 'headerService', 'xmlmeldingService',
        function ($scope, $mdDialog, $mdConstant, utilsService, headerService, xmlmeldingService) {

            headerService.setHeader('Send XML-melding');

            $scope.melding = '';
            $scope.showResponse = false;
            $scope.responseMelding = "";
            $scope.timeout = 5;
            var appobjects = [];

            $scope.sendTilTps = function () {
                var objectToTps = {
                    miljoe: $scope.egenkoe.substring(3, $scope.egenkoe.indexOf('_')),
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