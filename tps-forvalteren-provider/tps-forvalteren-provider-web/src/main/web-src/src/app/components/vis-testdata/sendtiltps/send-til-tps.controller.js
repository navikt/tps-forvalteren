angular.module('tps-forvalteren.vis-testdata.sendtiltps', ['ngMaterial'])
    .controller('SendTilTpsCtrl', ['$scope', '$mdDialog', '$stateParams', 'serviceRutineFactory', 'testdataService', 'utilsService',
        function ($scope, $mdDialog, $stateParams, serviceRutineFactory, testdataService, utilsService) {

            var gruppeId = $stateParams.gruppeId;

            $scope.showSpinner = false;
            $scope.valgteMiljoer = [];
            $scope.alleMiljoe = false;
            $scope.miljoeValgt = false;

            $scope.avbryt = function () {
                $mdDialog.hide();
            };

            $scope.send = function () {
                $scope.showSpinner = true;
                testdataService.sendTilTps(gruppeId, getSelectedMiljoer()).then(
                    function () {
                        $scope.showSpinner = false;
                        $mdDialog.hide();
                        var alert = $mdDialog.alert()
                            .title('Bekreftelse')
                            .htmlContent('Testpersoner for gruppe har blitt sendt til TPS.<br>' +
                                    'På grunn av prosessering i batch kan det ta flere minutter før endringen er synlig.')
                            .ariaLabel('Bekreftelse på at testpersoner har blitt sendt til TPS')
                            .ok('OK');
                        $mdDialog.show(alert);
                    },
                    function (error) {
                        $mdDialog.hide();
                        utilsService.showAlertError(error);
                    }
                );
            };

            function getSelectedMiljoer () {
                var miljoer = [];
                $scope.miljoer.forEach(function (miljoe, index){
                    if ($scope.valgteMiljoer[index]) {
                        miljoer.push(miljoe);
                    }
                });
                return miljoer;
            }

            $scope.velgAlleMiljoe = function () {
                $scope.miljoer.forEach(function (miljoe, index) {
                    $scope.valgteMiljoer[index] = !$scope.alleMiljoe;
                });
                isMiljoeSelected();
            };

            $scope.oppdaterVelgAlle = function () {
                $scope.alleMiljoe = $scope.valgteMiljoer.length === $scope.miljoer.length;
                $scope.valgteMiljoer.forEach(function (miljoe) {
                    if (!miljoe) {
                        $scope.alleMiljoe = false;
                    }
                });
                isMiljoeSelected();
            };

            function isMiljoeSelected () {
                $scope.miljoeValgt = false;
                $scope.valgteMiljoer.forEach(function (miljoe) {
                    if (miljoe) {
                        $scope.miljoeValgt = true;
                    }
                });
            }

            function init() {
                $scope.miljoer = serviceRutineFactory.getEnvironments().environments;
                $scope.sortedMiljoer = {};

                angular.forEach($scope.miljoer, function (miljoe) {
                    var substrMiljoe = miljoe.charAt(0);

                    if($scope.sortedMiljoer[substrMiljoe]) {
                        $scope.sortedMiljoer[substrMiljoe].push(miljoe);
                    } else {
                        $scope.sortedMiljoer[substrMiljoe] = [];
                        $scope.sortedMiljoer[substrMiljoe].push(miljoe);
                    }
                });
            }

            init();


        }]);