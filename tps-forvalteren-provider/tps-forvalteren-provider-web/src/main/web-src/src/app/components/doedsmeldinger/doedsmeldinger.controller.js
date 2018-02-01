angular.module('tps-forvalteren.doedsmeldinger', ['ngMaterial'])
    .controller('SendDoedsmeldingerCtrl', ['$scope', '$mdDialog', '$rootScope', '$stateParams', '$mdConstant', 'locationService', 'utilsService', 'headerService', 'doedsmeldingService',
        function ($scope, $mdDialog, $rootScope, $stateParams, $mdConstant, locationService, utilsService, headerService, doedsmeldingService) {

            headerService.setHeader('Dødsmelding');

            $scope.handlinger = [{handling: 'Sette dødsdato', action: 'C'},
                {handling: 'Endre dødsdato', action: 'U'},
                {handling: 'Slette dødsdato', action: 'D'}];

            $scope.startOfEra = new Date(1850, 0, 1); // Month is 0-indexed
            $scope.today = new Date();

            $scope.separators = [$mdConstant.KEY_CODE.ENTER, $mdConstant.KEY_CODE.COMMA, $mdConstant.KEY_CODE.SEMICOLON, $mdConstant.KEY_CODE.SPACE, $mdConstant.KEY_CODE.TAB];

            function getMeldinger() {
                $scope.progress = true;
                doedsmeldingService.hent().then(
                    function (result) {
                        $scope.meldinger = result.data;
                        clearRequestForm();
                        $scope.progress = false;
                    },
                    function (error) {
                        utilsService.showAlertError(error);
                        $scope.progress = false;
                    }
                );
            }

            $scope.getType = function (chip) {
                return parseInt(chip.substr(0,1)) > 3 ? 'dnr' :
                    parseInt(chip.substr(2,1)) > 2 ? 'bnr' : 'fnr' ;
            };

            $scope.validateChip = function(chip) {
                return !!chip.match(/^\d{11}$/) ? undefined : null;
            };

            $scope.checkDato = function () {
                if ($scope.melding && $scope.melding.handling === 'D') {
                    $scope.melding.doedsdato = undefined;
                    $scope.$broadcast('md-calendar-change', $scope.melding.doedsdato);
                }
            };

            $scope.add = function () {
                doedsmeldingService.opprett($scope.melding).then(function () {
                        getMeldinger();
                        clearRequestForm();
                    }, function (error) {
                        utilsService.showAlertError(error);
                    }
                );
            };

            function clearRequestForm () {
                $scope.melding = {};
                $scope.melding.identer = [];
                $scope.melding.doedsdato = null;
                $scope.requestForm.$setPristine();
                $scope.requestForm.$setUntouched();
            }

            $scope.delete = function (index) {
                doedsmeldingService.slett($scope.meldinger[index].id).then(function () {
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