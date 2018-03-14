angular.module('tps-forvalteren.rawxml-melding', ['ngMaterial'])
    .controller('RawXmlCtrl', ['$scope', '$mdDialog', '$mdConstant', 'utilsService', 'headerService', 'xmlmeldingService',
        function ($scope, $mdDialog, $mdConstant, utilsService, headerService, xmlmeldingService) {

            headerService.setHeader('Send XML-melding');

            $scope.melding = "<?xml version=\\\'1.0\\\' encoding=\\\'ISO-8859-1\\\'?>";
            $scope.tpsMessageQueueList = [];
            $scope.displayQueues = [];
            $scope.displayEnvironments = [];
            $scope.showResponse = false;
            $scope.responseMelding = "";

            $scope.onChangeMiljoe = function () {
                var queueList = [];

                $scope.tpsMessageQueueList.forEach(function (obj) {
                    if(obj.miljo === $scope.valgtMiljoe) {
                        queueList.push(obj.koNavn);
                    }
                });
                $scope.valgtKoe = "";
                $scope.displayQueues = queueList;
            };


            $scope.onChangeQueue = function () {
                $scope.tpsMessageQueueList.forEach(function (obj) {
                    if(obj.koNavn === $scope.valgtKoe) {
                        $scope.valgtMiljoe = obj.miljo;
                    }
                });
            };

            $scope.sendTilTps = function () {
                var objectToTps = {
                    melding: $scope.melding,
                    ko: $scope.valgtKoe
                };

                xmlmeldingService.send(objectToTps).then(function(response){
                    $scope.showResponse = true;
                    if (response.config.data.melding.indexOf("<?xml version") !== -1) {
                        $scope.responseMelding = utilsService.formatXml(response.data.xml);
                    } else {
                        $scope.responseMelding = response.data.xml;
                    }
                }, function (error) {
                    $scope.responseMelding = "Error";
                });
            };

            function removeDuplicatesEnvironments() {
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

            function hentAlleMiljoerOgKoer() {

                xmlmeldingService.hentKoer().then(function (result) {
                        $scope.tpsMessageQueueList = result.data;

                        $scope.tpsMessageQueueList.forEach(function (obj) {
                            $scope.displayQueues.push(obj.koNavn);
                            $scope.displayEnvironments.push(obj.miljo);
                        });

                        $scope.displayEnvironments = removeDuplicatesEnvironments();
                    });

            }

            hentAlleMiljoerOgKoer();


        }]);