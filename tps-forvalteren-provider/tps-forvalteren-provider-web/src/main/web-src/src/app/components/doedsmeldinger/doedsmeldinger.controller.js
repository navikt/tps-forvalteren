angular.module('tps-forvalteren.doedsmeldinger', ['ngMaterial'])
    .controller('SendDoedsmeldingerCtrl', ['$scope', '$mdDialog', '$rootScope', '$stateParams', 'locationService', 'utilsService', 'headerService', 'doedsmeldingService',
        function ($scope, $mdDialog, $rootScope, $stateParams, locationService, utilsService, headerService, doedsmeldingService) {

            headerService.setHeader('Dødsmelding');

            $scope.handlinger = [{handling: 'Sette dødsdato', action: 'C'},
                {handling: 'Endre dødsdato', action: 'U'},
                {handling: 'Slette dødsdato', action: 'D'}];

            $scope.startOfEra = new Date(1850, 0, 1); // Month is 0-indexed
            $scope.today = new Date();
            $scope.melding = {};

            function getMeldinger() {
                doedsmeldingService.hent().then(
                    function (result) {
                        $scope.meldinger = result.data;
                    },
                    function (error) {
                        utilsService.showAlertError(error);
                    }
                );
            }

            $scope.checkDato = function () {
                if ($scope.melding && $scope.melding.handling === 'D') {
                    $scope.melding.doedsdato = undefined;
                    $scope.$broadcast('md-calendar-change', $scope.melding.doedsdato);
                }
            };

            $scope.add = function () {
                $scope.melding.identer = $scope.identer.split(/[\W\s]+/g);
                doedsmeldingService.opprett($scope.melding).then(function () {
                        getMeldinger();
                    }, function (error) {
                        utilsService.showAlertError(error);
                    }
                );
            };

            $scope.delete = function (index) {
                doedsmeldingService.slett($scope.meldinger[index].ident).then(function () {
                        $scope.meldinger.splice(index, 1);
                    }, function (error) {
                        utilsService.showAlertError(error);
                    }
                );
            };

            $scope.edit = function (index) {
                endreMelding(index);
            };

            var endreMelding = function (index, ev) {
                var confirm = $mdDialog.confirm({
                    controller: 'EndreMeldingCtrl',
                    templateUrl: 'app/components/doedsmeldinger/endremelding/endre-melding.html',
                    parent: angular.element(document.body),
                    targetEvent: ev,
                    locals: {
                        melding: $scope.meldinger[index],
                        miljoer: $scope.$resolve.environmentsPromise,
                        handlinger: $scope.handlinger,
                        startOfEra: $scope.startOfEra,
                        today: $scope.today
                    }
                });
                $mdDialog.show(confirm);
            };

            var init = function () {
                var environments = $scope.$resolve.environmentsPromise;
                $scope.environments = utilsService.sortEnvironments(environments.environments);
            };

            $scope.toemSkjema = function () {
                var confirm = $mdDialog.confirm()
                    .title('Bekrefting sletting')
                    .textContent("Vennligst bekreft sletting av alle dødsmeldinger i lokal database:")
                    .ariaLabel('Bekreft tømming av skjema.')
                    .ok('Tøm skjema')
                    .cancel('Avbryt');
                $mdDialog.show(confirm).then(function () {
                    doedsmeldingService.toemSkjema().then(function () {
                        $scope.meldinger = [];
                    }, function (error) {
                        utilsService.showAlertError(error);
                    });
                });
            };

            $scope.sendTilTps = function () {
                doedsmeldingService.sendSkjema($scope.meldinger).then(function () {
                    var alert = $mdDialog.alert()
                        .title('Meldinger sendt')
                        .textContent('Sending av dødsmeldinger til TPS er utført!')
                        .ariaLabel('Dødsmeldinger er sendt til TPS.')
                        .ok('OK');
                    $mdDialog.show(alert);
                }, function (error) {
                    utilsService.showAlertError(error);
                })
            };

            init();
            getMeldinger();

        }]);