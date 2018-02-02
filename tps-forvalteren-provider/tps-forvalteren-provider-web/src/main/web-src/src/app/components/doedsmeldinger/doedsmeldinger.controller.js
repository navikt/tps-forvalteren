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

            $scope.sjekkgyldig = function () {
                if ($scope.melding.miljoe && $scope.melding.identer.length > 0) {
                    var identer = [];
                    $scope.melding.identer.forEach(function (ident) {
                        identer.push(ident.ident);
                    });
                    doedsmeldingService.sjekkgyldig({miljoe: $scope.melding.miljoe, identer: identer}).then(
                        function (result) {
                            var elementer = document.getElementsByName('chips-template');
                            result.data.forEach(function (personStatus, index) {
                                $scope.melding.identer.forEach(function (meldingIdent) {
                                    if (personStatus.ident === meldingIdent.ident) {
                                        elementer[index].parentElement.parentElement.style.backgroundColor = personStatus === 'FIN' ? 'yellowgreen' : 'indianred';
                                    }
                                });
                            });
                        }, function (error) {
                            utilsService.showAlertError(error);
                            // var elementer = document.getElementsByName('chips-template');
                            // $scope.melding.identer.forEach(function (meldingIdent, index) {
                            //     elementer[index].parentElement.parentElement.style.backgroundColor = index % 2 === 0 ? 'yellowgreen' : 'indianred';
                            // });
                        }
                    );
                }
            };

            $scope.getType = function (ident) {
                return parseInt(ident.substr(0, 1)) > 3 ? 'dnr' :
                    parseInt(ident.substr(2, 1)) > 2 ? 'bnr' : 'fnr';
            };

            $scope.addMultipleChips = function (chip) {
                var seperatedString = angular.copy(chip);
                seperatedString = seperatedString.toString();
                var chipsArray = seperatedString.split(/[\W\s]+/);
                chipsArray.forEach( function (chipToAdd) {
                    if (chipToAdd.match(/^\d{11}$/)) {
                        var found = false;
                        $scope.melding.identer.forEach(function (element) {
                            if (chipToAdd === element) {
                                found = true;
                            }
                        });
                        if (!found) {
                            $scope.melding.identer.push(chipToAdd);
                        }
                    }
                });
                $scope.sjekkgyldig();
                return null;
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

            function clearRequestForm() {
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