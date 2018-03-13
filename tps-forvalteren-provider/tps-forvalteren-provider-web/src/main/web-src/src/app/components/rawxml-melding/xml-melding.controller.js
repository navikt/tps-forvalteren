angular.module('tps-forvalteren.rawxml-melding', ['ngMaterial'])
    .controller('RawXmlCtrl', ['$scope', '$mdDialog', '$mdConstant', 'utilsService', 'headerService', 'XmlmeldingService',
        function ($scope, $mdDialog, $mdConstant, utilsService, headerService, XmlmeldingService) {

            headerService.setHeader('Send XML-melding');

            $scope.melding = "<?xml version=\\\'1.0\\\' encoding=\\\'ISO-8859-1\\\'?>";
            $scope.tpsMessageQueueList = [];
            $scope.displayQueues = [];
            $scope.displayEnvironments = [];
            $scope.showStatus = true;
            $scope.returStatus = "00";
            $scope.svarStatus = "Meldingen er sendt";

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

            $scope.xmlFormatter = function (text) {
                var xmlFormat = utilsService.formatXml(text);
                $scope.melding = xmlFormat;
                console.log($scope.melding);
            };

            $scope.onChangeQueue = function () {
                $scope.tpsMessageQueueList.forEach(function (obj) {
                    if(obj.koNavn === $scope.valgtKoe) {
                        $scope.valgtMiljoe = obj.miljo;
                    }
                });
            };

            $scope.sendTilTps = function () {

                console.log("Message is sent");
                var objectToTps = {
                    koNavn: $scope.valgtKoe,
                    melding: $scope.melding
                };

                XmlmeldingService.send(objectToTps).then(function(response){
                    console.log(response);
                });

                console.log(objectToTps);
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

                XmlmeldingService.hentKoer().then(
                    function (result) {
                        // debugger;

                        $scope.tpsMessageQueueList = result.data;
                        console.log($scope.tpsMessageQueueList);

                        removeDuplicatesEnvironments()
                        $scope.tpsMessageQueueList.forEach(function (obj) {
                            console.log(obj);
                            $scope.displayQueues.push(obj.koNavn);
                            $scope.displayEnvironments.push(obj.miljo);
                        });

                        $scope.displayEnvironments = removeDuplicatesEnvironments();
                        console.log($scope.displayEnvironments);
                    }
                );

                //Skal gjøre et kall på et endepunkt, men er forelpig ikke klart så bruker dummydata


                    // [
                    // {
                    //     "miljo": "u5",
                    //     "koNavn": "QA.D8_411.TPS_FORESPORSEL_XML_O"
                    // },
                    // {
                    //     "miljo": "u6",
                    //     "koNavn": "QA.D8_411.TPS_FORESPORSEL_XML_O_TEST"
                    // },
                    // {
                    //     "miljo": "u6",
                    //     "koNavn": "QA.D8_411.TPS_FORESPORSEL_XML_O_TEST2"
                    // },
                    // {
                    //     "miljo": "u6",
                    //     "koNavn": "QA.D8_411.SFE_ENDRINGSMELDING_TEST"
                    // }


            }

            hentAlleMiljoerOgKoer();


        }]);