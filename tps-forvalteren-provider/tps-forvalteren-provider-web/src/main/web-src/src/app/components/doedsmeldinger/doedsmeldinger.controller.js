angular.module('tps-forvalteren.doedsmeldinger', ['ngMaterial'])
    .controller('SendDoedsmeldingerCtrl', ['$scope', '$mdDialog', '$mdConstant', 'utilsService', 'headerService', 'doedsmeldingService',
        function ($scope, $mdDialog, $mdConstant, utilsService, headerService, doedsmeldingService) {

            headerService.setHeader('Dødsmelding');

            $scope.handlinger = [{handling: 'Sette dødsdato', action: 'C'},
                {handling: 'Endre dødsdato', action: 'U'},
                {handling: 'Annulere dødsdato', action: 'D'}];

            $scope.separators = [$mdConstant.KEY_CODE.ENTER, $mdConstant.KEY_CODE.COMMA, $mdConstant.KEY_CODE.SEMICOLON, $mdConstant.KEY_CODE.SPACE, $mdConstant.KEY_CODE.TAB];
            $scope.SELECT = 'Velg';
            var identStatus = [];

            function getMeldinger() {
                $scope.showProgress = true;
                doedsmeldingService.hent().then(
                    function (result) {
                        $scope.meldinger = result.data;
                        clearRequestForm();
                        $scope.showProgress = false;
                    },
                    function (error) {
                        utilsService.showAlertError(error);
                        clearRequestForm();
                        $scope.showProgress = false;
                    }
                );
            }

            $scope.sjekkgyldig = function () {
                if ($scope.melding.miljoe && $scope.SELECT !== $scope.melding.miljoe && $scope.melding.identer.length > 0) {
                    var identer = [];
                    $scope.melding.identer.forEach(function (ident) {
                        identer.push(ident);
                    });
                    doedsmeldingService.sjekkgyldig({miljoe: $scope.melding.miljoe, identer: identer}).then(
                        function (result) {
                            var elementer = document.getElementsByName('chips-template');
                            for (var i = 0; i < result.data.length; i++) {
                                $scope.melding.identer.forEach(function (meldingIdent, index) {
                                    if (result.data[i].ident === meldingIdent) {
                                        elementer[index].parentElement.parentElement.style.backgroundColor = result.data[i].status === 'LIM' ? 'yellowgreen' :
                                            result.data[i].status === 'FIN' ? 'yellow' : 'indianred';
                                        elementer[index].parentElement.parentElement.title = result.data[i].status === 'FIN' ? 'Ident er allerede lagt til i liste under' : '';
                                        identStatus[result.data[i].ident] = result.data[i].status === 'LIM';
                                    }
                                });
                            }
                        }, function (error) {
                            utilsService.showAlertError(error);
                        }
                    );
                }
            };

            $scope.addMultipleChips = function (chip) {
                var seperatedString = angular.copy(chip);
                seperatedString = seperatedString.toString();
                var chipsArray = seperatedString.split(/[\W\s]+/);
                chipsArray.forEach(function (chipToAdd) {
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

            $scope.showResponse = function () {
                var match = false;
                if ($scope.meldinger && $scope.meldinger.length > 0) {

                    if (!$scope.melding.miljoe || $scope.SELECT === $scope.melding.miljoe) {
                        match = true;
                    } else {
                        $scope.meldinger.forEach(function (melding) {
                            if (melding.miljoe === $scope.melding.miljoe) {
                                match = true;
                            }
                        });
                    }
                }
                return match;
            };

            $scope.checkDato = function () {
                if ($scope.melding && $scope.melding.handling === 'D') {
                    $scope.melding.doedsdato = null;
                    $scope.$broadcast('md-calendar-change', $scope.melding.doedsdato);
                }
            };

            $scope.add = function () {
                var identer = [];
                $scope.melding.identer.forEach(function (ident) {
                    if (identStatus[ident]) {
                        identer.push(ident);
                    }
                });
                $scope.melding.identer = identer;
                $scope.melding.doedsdato = fixDate($scope.melding.doedsdato);
                doedsmeldingService.opprett($scope.melding).then(function () {
                        getMeldinger();
                        clearRequestForm();
                    }, function (error) {
                        utilsService.showAlertError(error);
                    }
                );
            };

            function fixDate(date) {
                return date ? new Date(date.getTime() + (12 * 3600 * 1000)) : null;
            }

            function clearRequestForm() {
                $scope.melding = $scope.melding ? $scope.melding : {};
                $scope.melding.identer = [];
                $scope.melding.doedsdato = null;
                $scope.melding.handling = undefined;
                $scope.melding.miljoe = undefined;
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
                        select: $scope.SELECT,
                        getMeldinger: getMeldinger
                    }
                });
                $mdDialog.show(confirm);
            };

            $scope.toemSkjema = function () {
                var confirm = $mdDialog.confirm()
                    .title('Bekrefting sletting')
                    .textContent("Vennligst bekreft sletting av alle dødsmeldinger i lokal database:")
                    .ariaLabel('Bekreft tømming av skjema.')
                    .ok('Tøm skjema')
                    .cancel('Avbryt');
                $mdDialog.show(confirm).then(function () {
                    doedsmeldingService.toemSkjema($scope.melding.miljoe).then(function () {
                        var meldinger = [];
                        $scope.meldinger.forEach(function (melding) {
                            if ($scope.melding && $scope.melding.miljoe && $scope.SELECT !== $scope.melding.miljoe && melding.miljoe !== $scope.melding.miljoe) {
                                meldinger.push(melding);
                            }
                        });
                        $scope.meldinger = meldinger;
                    }, function (error) {
                        utilsService.showAlertError(error);
                    });
                });
            };

            $scope.sendTilTps = function () {
                $scope.showProgress = true;
                var meldinger = [];
                $scope.meldinger.forEach(function (melding) {
                    if (!$scope.melding.miljoe || $scope.SELECT === $scope.melding.miljoe || melding.miljoe === $scope.melding.miljoe) {
                        meldinger.push(melding);
                    }
                });
                doedsmeldingService.sendSkjema(meldinger).then(function () {
                    var alert = $mdDialog.alert()
                        .title('Meldinger sendt')
                        .textContent('Sending av dødsmelding(er) til TPS er utført!')
                        .ariaLabel('Dødsmeldinger er sendt til TPS.')
                        .ok('OK');
                    $mdDialog.show(alert);
                    $scope.showProgress = false;
                    getMeldinger();
                }, function (error) {
                    utilsService.showAlertError(error);
                    $scope.showProgress = false;
                    getMeldinger();
                })
            };

            function sortEnvironmentsForDisplay(environments) {
                var filteredEnvironments = {};
                var sortedEnvironments = [];

                environments = utilsService.sortEnvironments(environments);

                angular.forEach(environments, function (env) {
                    var substrMiljoe = env.charAt(0);

                    if(filteredEnvironments[substrMiljoe]) {
                        filteredEnvironments[substrMiljoe].push(env);
                    } else {
                        filteredEnvironments[substrMiljoe] = [];
                        filteredEnvironments[substrMiljoe].push(env);
                    }
                });

                if (filteredEnvironments['u']) {
                    angular.forEach(filteredEnvironments['u'], function (env) {
                        sortedEnvironments.push(env);
                    });
                }

                if (filteredEnvironments['t']) {
                    angular.forEach(filteredEnvironments['t'], function (env) {
                        sortedEnvironments.push(env);
                    });
                }

                if (filteredEnvironments['q']) {
                    angular.forEach(filteredEnvironments['q'], function (env) {
                        sortedEnvironments.push(env);
                    });
                }

                return sortedEnvironments;
            }

            function getEnvironments() {
                var environments = $scope.$resolve.environmentsPromise;
                $scope.environments = sortEnvironmentsForDisplay(environments.environments);
            }

            getEnvironments();
            getMeldinger();
        }]);