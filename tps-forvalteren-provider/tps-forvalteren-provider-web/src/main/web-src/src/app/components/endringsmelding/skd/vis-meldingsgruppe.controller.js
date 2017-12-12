angular.module('tps-forvalteren.skd-vis-meldingsgruppe', ['ngMessages'])
    .controller('SkdVisMeldigsgruppeCtrl', ['$scope', '$stateParams', '$filter', '$mdDialog', '$copyToClipboard', 'endringsmeldingService',
        'utilsService', 'locationService', 'headerService', 'toggleservice',
        function ($scope, $stateParams, $filter, $mdDialog, $copyToClipboard, endringsmeldingService, utilsService, locationService,
                  headerService, toggleservice) {

            $scope.meldingstypeT1 = "app/components/endringsmelding/skd/meldingstype/meldingstype-t1.html";
            $scope.meldingstypeT2 = "app/components/endringsmelding/skd/meldingstype/meldingstype-t2.html";

            $scope.service = endringsmeldingService;
            $scope.gruppeId = $stateParams.gruppeId;

            $scope.aapneAlleFaner = false;
            $scope.meldingAsText = [];

            function setHeaderButtons () {
                headerService.setButtons([{
                    text: 'Legg til meldinger',
                    icon: 'assets/icons/ic_add_circle_outline_black_24px.svg',
                    disabled: function () {
                        return $scope.visEndret;
                    },
                    click: function () {
                        var confirm = $mdDialog.confirm({
                            controller: 'SkdNyMeldingCtrl',
                            templateUrl: 'app/components/endringsmelding/skd/nymelding/ny-melding.html',
                            parent: angular.element(document.body),
                            bindToController: true
                        });
                        $mdDialog.show(confirm).then(
                            function () { // Prevents duplicate call to rest-endpoint
                            },
                            function () { // Controlled error exit
                            }
                        )
                    }
                }, {
                    text: 'Send til TPS',
                    icon: 'assets/icons/ic_send_black_24px.svg',
                    disabled: function () {
                        return $scope.visEndret || !$scope.meldinger || $scope.meldinger.length == 0
                    },
                    click: function (ev) {
                        var confirm = $mdDialog.confirm({
                            controller: 'SkdSendTilTpsCtrl',
                            templateUrl: 'app/components/endringsmelding/skd/sendtiltps/send-til-tps.html',
                            parent: angular.element(document.body),
                            targetEvent: ev
                        });
                        $mdDialog.show(confirm);
                    }
                }]);
            }

            function setHeaderIcons () {
                headerService.setIcons([{
                    icon: 'assets/icons/ic_mode_edit_black_24px.svg',
                    title: 'Endre meldingsgruppe',
                    click: function (ev) {
                        var confirm = $mdDialog.confirm({
                            controller: 'EndreSkdGruppeCtrl',
                            templateUrl: 'app/components/endringsmelding/skd/endregruppe/endre-gruppe.html',
                            parent: angular.element(document.body),
                            targetEvent: ev,
                            locals: {
                                miljo: $scope.$resolve.environmentsPromise
                            },
                            bindToController: true,
                            controllerAs: 'ctrl'
                        });
                        $mdDialog.show(confirm).then(
                            function () { // Prevents duplicate call to rest-endpoint
                            },
                            function () { // Controlled error exit
                            }
                        )
                    }
                }, {
                    icon: 'assets/icons/ic_delete_black_24px.svg',
                    title: 'Slette meldingsgruppe',
                    click: function () {
                        var meldingsTekst = $scope.meldinger.length > 0 ? ' med ' + $scope.meldinger.length + ' melding' : '';
                        meldingsTekst += $scope.meldinger.length > 1 ? 'er' : '';
                        var confirm = $mdDialog.confirm()
                            .title('Bekreft sletting')
                            .htmlContent('Ønsker du å slette gruppe <strong>' + headerService.getHeader().name + '</strong>' + meldingsTekst + '?')
                            .ariaLabel('Bekreft sletting')
                            .ok('OK')
                            .cancel('Avbryt');
                        $mdDialog.show(confirm).then(
                            function () {
                                endringsmeldingService.deleteGruppe($scope.gruppeId).then(
                                    function () {
                                        locationService.redirectToSkdEndringsmeldingGrupper();
                                    },
                                    function (error) {
                                        utilsService.showAlertError(error);
                                    }
                                )
                            }, function () {
                                // Empty function to prevent unhandled rejection error
                            });
                    }
                }]);
            }

            $scope.alleMeldinger = {checked: false};
            $scope.meldinger = [];
            var originalMeldinger = [];
            $scope.control = [];

            $scope.velgAlle = function () {
                var enabled = 0;
                for (var i = 0; i < $scope.meldinger.length; i++) {
                    if (!$scope.control[i]) {
                        $scope.control[i] = {};
                    }
                    if (!$scope.control[i].disabled) {
                        $scope.control[i].velg = !$scope.alleMeldinger.checked;
                        enabled++;
                    }
                }
                $scope.antallValgt = !$scope.alleMeldinger.checked ? enabled : 0;
                oppdaterFunksjonsknapper();
            };

            var oppdaterFane = undefined;

            $scope.sletteDialog = function (index) {
                var confirm = $mdDialog.confirm()
                    .title('Bekreft sletting')
                    .textContent('Bekreft sletting av valgte meldinger')
                    .ariaLabel('Bekreft sletting')
                    .ok('OK')
                    .cancel('Avbryt');

                $mdDialog.show(confirm).then(function () {
                    sletteMeldinger();
                }, function () {
                    // Prevent unhandled rejection error
                });
            };

            $scope.oppdaterValgt = function () {
                oppdaterFane = true;

                var endret = 0;
                for (var i = 0; i < $scope.meldinger.length; i++) {
                    $scope.control[i] = $scope.control[i] || {};
                    if ($scope.control[i].endret) {
                        endret++;
                    }
                }

                var valgt = 0;
                for (var i = 0; i < $scope.meldinger.length; i++) {
                    if (endret > 0) {
                        $scope.control[i].disabled = !$scope.control[i].endret;
                        if (!$scope.control[i].endret) {
                            $scope.control[i].velg = false;
                        }
                    } else {
                        $scope.control[i].disabled = false;
                    }
                    if ($scope.control[i].velg) {
                        valgt++;
                    }
                }
                $scope.alleMeldinger.checked = (endret === 0 && $scope.meldinger.length === valgt) ||
                    (endret > 0 && endret === valgt);
                $scope.antallEndret = endret;
                $scope.antallValgt = valgt;
                $scope.visEndret = endret > 0;
            };

            function sletteMeldinger () {
                var idList = [];
                for (var i = 0; i < $scope.meldinger.length; i++) {
                    if ($scope.control[i].velg) {
                        idList.push($scope.meldinger[i].id);
                    }
                }
                endringsmeldingService.deleteMeldinger(idList).then(
                    function (result) {
                        fetchMeldingsgruppe();
                    },
                    function (error) {
                        utilsService.showAlertError(error);
                    }
                );
            }

            $scope.lagre = function () {
                var buffer = [];
                $scope.meldinger.forEach(function (melding, index) {
                    if ($scope.control[index] && $scope.control[index].velg) {
                        buffer.push(melding);
                    }
                });
                endringsmeldingService.updateMeldinger(buffer).then(
                    function () {
                        $scope.meldinger.forEach(function (melding, index) {
                            if ($scope.control[index] && $scope.control[index].velg) {
                                $scope.control[index] = {};
                                originalMeldinger[index] = angular.copy(melding);
                            }
                        });
                        $scope.oppdaterValgt();
                        bekrefterLagring();
                        $scope.aapneAlleFaner = toggleservice.checkAggregateOpenCloseButtonNextState(
                            $scope.aapneAlleFaner, $scope.control, $scope.pager, $scope.meldinger.length);
                    },
                    function (error) {
                        utilsService.showAlertError(error);
                    }
                );
            };

            function oppdaterFunksjonsknapper () {
                var endret = false;
                for (var i = 0; i < $scope.control.length; i++) {
                    if ($scope.control[i]) {
                        if ($scope.control[i].endret) {
                            endret = true;
                        }
                    }
                }

                $scope.visEndret = endret;
            }

            $scope.endret = function (index) {
                var originalMelding = JSON.stringify(originalMeldinger[index]).replace(/null/g, '""');
                var endretMelding = JSON.stringify($scope.meldinger[index]).replace(/null/g, '""')
                    .replace(/,*"\$\$hashKey":"[A-Za-z0-9_:]+"/g, '');

                $scope.control[index].endret = originalMelding !== endretMelding;
                $scope.control[index].velg = $scope.control[index].endret;
                $scope.oppdaterValgt();
                if ($scope.control[index].endret) {
                    $scope.meldingAsText[index] = undefined;
                }
            };

            function avbrytLagring () {
                for (var i = 0; i < $scope.meldinger.length; i++) {
                    if ($scope.control[i] && $scope.control[i].velg) {
                        $scope.meldinger[i] = JSON.parse(JSON.stringify(originalMeldinger[i]));
                        $scope.control[i] = {};
                    }
                }
                $scope.slice = $scope.meldinger.slice($scope.pager.startIndex, $scope.pager.endIndex + 1);
                $scope.oppdaterValgt();
            }

            function bekrefterLagring (index) {
                var confirm = $mdDialog.confirm()
                    .title('Bekrefter lagring')
                    .textContent('Lagring er utført')
                    .ariaLabel('Bekrefter lagring')
                    .ok('OK');

                $mdDialog.show(confirm);
            }

            $scope.avbryteDialog = function () {
                var confirm = $mdDialog.confirm()
                    .title('Bekreft avbryt endring')
                    .textContent('Endringer som er gjort vil gå tapt')
                    .ariaLabel('Bekreft avbryt')
                    .ok('OK')
                    .cancel('Avbryt');

                $mdDialog.show(confirm).then(function () {
                    avbrytLagring();
                }, function () {

                });
            };

            function bekreftRelokasjon (next, current) {
                var confirm = $mdDialog.confirm()
                    .title('Du har endringer som ikke er lagret')
                    .textContent('Trykk OK for å forlate siden.')
                    .ariaLabel('Bekreftelse på relokasjon og endringer ikke lagret')
                    .ok('OK')
                    .cancel('Avbryt');

                $mdDialog.show(confirm).then(function () {
                    $scope.visEndret = false;
                    locationService.redirectUrl(next.url, current);
                }, function () {

                });
            }

            $scope.$on('createdMelding', function () {
                fetchMeldingsgruppe();
            });

            $scope.$on('$stateChangeStart', function (event, next, current) {
                if ($scope.visEndret) {
                    event.preventDefault();
                    bekreftRelokasjon(next, current);
                }
            });

            window.onbeforeunload = function (event) {
                if ($scope.visEndret) {
                    return 'Du har data som ikke er lagret. Vil du forlate siden?'; // Trigger nettlesers visning av dialogboks for avslutning
                }
            };

            $scope.$watch('visEndret', function() {
                headerService.eventUpdate();
            });

            function confirmClipboardCopy () {
                var confirm = $mdDialog.confirm()
                    .title('Kopiering vellykket utført!')
                    .textContent('SKD-melding har blitt kopiert til utklippstavle.')
                    .ariaLabel('Bekreftelse på kopiering av SKD-melding til utklippstavle.')
                    .ok('OK');
                $mdDialog.show(confirm);
            }

            $scope.convertMelding = function (index) {
                if (!$scope.meldingAsText[index]) {
                    endringsmeldingService.convertMelding($scope.meldinger[index]).then(
                        function (result) {
                            $scope.meldingAsText[index] = result.data.melding;
                        }, function (error) {
                            utilsService.showAlertError(error);
                            //TODO ta bort linje under
                            $scope.meldingAsText[index] = "Lang tekststreng på 1500 tegn fra fane nummer: " + ++index;
                        });
                }
            };

            $scope.copyToClipboard = function (index) {
                if ($scope.meldingAsText[index]) {
                    $copyToClipboard.copy($scope.meldingAsText[index]);
                    confirmClipboardCopy();
                }
            };

            function fetchMeldingsgruppe () {
                $scope.showSpinner = true;
                $scope.meldinger = undefined;
                endringsmeldingService.getGruppe($scope.gruppeId, true).then(
                    function (result) {
                        headerService.setHeader(result.data.navn);
                        setHeaderButtons();
                        setHeaderIcons();
                        prepTranstype(result.data.meldinger);
                        originalMeldinger = result.data.meldinger;
                        $scope.meldinger = angular.copy(originalMeldinger);
                        $scope.control = [];
                        $scope.meldingAsText = [];
                        $scope.antallEndret = 0;
                        $scope.antallValgt = 0;
                        $scope.alleMeldinger.checked = false;
                        oppdaterFunksjonsknapper();
                        headerService.eventUpdate();
                        $scope.showSpinner = false;
                    },
                    function (error) {
                        utilsService.showAlertError(error);
                        headerService.setHeader("SKD Endringsmeldinger");
                        $scope.showSpinner = false;
                    }
                );
            }

            function prepTranstype (meldinger) {
                meldinger.forEach(function (melding) {
                    if (melding.meldingstype === 't1') {
                        melding.transtype = '1';
                    }
                    if (melding.meldingstype === 't2') {
                        melding.transtype = melding.transtype || '2';
                    }
                });
            }

            var checkIt = false;

            $scope.checkIt = function () { // la være å toggle fane hvis det er checkbox som klikkes
                checkIt = true;
            };

            $scope.toggleFane = function (index) {
                if ($scope.requestForm.$valid) {
                    if (!checkIt) {
                        toggleservice.toggleFane($scope.control, index);
                        $scope.aapneAlleFaner = toggleservice.checkAggregateOpenCloseButtonNextState(
                            $scope.aapneAlleFaner, $scope.control, $scope.pager, $scope.meldinger.length);
                    }
                    checkIt = false;
                }
            };

            $scope.$watch('pager.startIndex', function () {
                if ($scope.meldinger) {
                    $scope.aapneAlleFaner = toggleservice.checkAggregateOpenCloseButtonNextState(
                        $scope.aapneAlleFaner, $scope.control, $scope.pager, $scope.meldinger.length);
                }
            });

            $scope.toggleAlleFaner = function () {
                $scope.aapneAlleFaner = toggleservice.toggleAlleFaner($scope.aapneAlleFaner, $scope.control, $scope.pager);
            };

            fetchMeldingsgruppe();
        }]);
