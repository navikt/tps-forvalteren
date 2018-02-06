angular.module('tps-forvalteren.doedsmeldinger.endremelding', ['ngMaterial'])
    .controller('EndreMeldingCtrl', ['$scope', '$mdDialog', 'utilsService', 'doedsmeldingService',
        function ($scope, $mdDialog, utilsService, doedsmeldingService, melding, miljoer, handlinger, startOfEra, today, select) {

            $scope.melding = this.melding;
            $scope.miljoer = utilsService.sortEnvironments(this.miljoer.environments);
            $scope.handlinger = this.handlinger;
            $scope.startOfEra = this.startOfEra;
            $scope.today = this.today;
            $scope.SELECT = this.select;

            $scope.oppdatering = angular.copy($scope.melding);

            $scope.avbryt = function () {
                $mdDialog.hide();
            };

            $scope.checkDato = function() {
                if ($scope.oppdatering && $scope.oppdatering.handling === 'D') {
                    $scope.oppdatering.doedsdato = null;
                    $scope.$broadcast('md-calendar-change', $scope.oppdatering.doedsdato);
                }
            };

            $scope.oppdater = function () {
                doedsmeldingService.endre($scope.oppdatering).then(
                    function () {
                        $scope.melding.handling = $scope.oppdatering.handling;
                        $scope.melding.doedsdato = $scope.oppdatering.doedsdato;
                        $scope.melding.miljoe = $scope.oppdatering.miljoe;
                        $mdDialog.hide();
                    },
                    function (error) {
                        $mdDialog.hide();
                        utilsService.showAlertError(error);
                    }
                );
            };
        }]);