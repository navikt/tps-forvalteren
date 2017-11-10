angular.module('tps-forvalteren.skd-vis-meldingsgruppe.sendtiltps', ['ngMaterial'])
    .controller('SkdSendTilTpsCtrl', ['$scope', '$mdDialog', '$stateParams', 'serviceRutineFactory', 'endringsmeldingService', 'utilsService',
        function ($scope, $mdDialog, $stateParams, serviceRutineFactory, endringsmeldingService, utilsService) {

            var gruppeId = $stateParams.gruppeId;
            $scope.showSpinner = false;

            $scope.avbryt = function () {
                $mdDialog.hide();
            };

            $scope.send = function () {
                $scope.showSpinner = true;
                endringsmeldingService.sendTilTps(gruppeId, $scope.valgtMiljoe).then(
                    function () {
                        $scope.showSpinner = false;
                        $mdDialog.hide();
                        var alert = $mdDialog.alert()
                            .title('Bekreftelse')
                            .htmlContent('SKD-melding(er) for gruppe har blitt sendt til TPS.<br>' +
                                    'På grunn av prosessering i batch kan det ta flere minutter før endringen er synlig.')
                            .ariaLabel('Bekreftelse på at SKD-meldinger har blitt sendt til TPS')
                            .ok('OK');
                        $mdDialog.show(alert);
                    },
                    function (error) {
                        $scope.showSpinner = false;
                        $mdDialog.hide();
                        utilsService.showAlertError(error);
                    }
                );
            };

            $scope.miljoer = serviceRutineFactory.getEnvironments().environments;
            $scope.miljoer.sort(function (a, b) {
                return a.substring(1) - b.substring(1);
            });
            $scope.miljoe = $scope.miljoer[0].substr(0,1);

        }]);