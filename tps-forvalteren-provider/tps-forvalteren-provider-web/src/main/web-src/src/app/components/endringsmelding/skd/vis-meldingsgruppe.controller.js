angular.module('tps-forvalteren.skd-vis-meldingsgruppe', ['ngMessages'])
    .controller('SkdVisMeldigsgruppeCtrl', ['$scope', '$rootScope', '$stateParams', '$filter', '$mdDialog', 'endringsmeldingService', 'utilsService', 'locationService',
        'headerService',
        function ($scope, $rootScope, $stateParams, $filter, $mdDialog, endringsmeldingService, utilsService, locationService, underHeaderService) {

            $scope.service = endringsmeldingService;

            $scope.gruppeId = $stateParams.gruppeId;

            $scope.aapneAlleFaner = false;

            var setHeaderButtons = function (antall_personer) {
                var disable_send_til_tps_button = antall_personer < 1;
                underHeaderService.setButtons([{
                    text: 'Legg til meldinger',
                    icon: 'assets/icons/ic_add_circle_outline_black_24px.svg',
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
                    disabled: disable_send_til_tps_button,
                    click: function (ev) {
                        var confirm = $mdDialog.confirm({
                            controller: 'SendTilTpsCtrl',
                            templateUrl: 'app/components/vis-testdata/sendtiltps/send-til-tps.html',
                            parent: angular.element(document.body),
                            targetEvent: ev
                        });
                        $mdDialog.show(confirm);
                    }
                }]);
            };

            var setHeaderIcons = function () {
                underHeaderService.setIcons([{
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
                            .htmlContent('Ønsker du å slette gruppe <strong>' + underHeaderService.getHeader().name + '</strong>' + meldingsTekst + '?')
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
            };

            $scope.allePersoner = {checked: false};
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
                        $scope.control[i].velg = !$scope.allePersoner.checked;
                        enabled++;
                    }
                }
                $scope.antallValgt = !$scope.allePersoner.checked ? enabled : 0;
                oppdaterFunksjonsknapper();
            };



            var oppdaterFane = undefined;
            var checkIt = false;

            $scope.toggleFane = function (index) {
                if (!$scope.control[index]) {
                    $scope.control[index] = {};
                }
                if (!checkIt) {
                    $scope.control[index].aapen = !$scope.control[index].aapen;
                }
                checkIt = false;
                checkAndModifyAggregateOpenCloseButton();
            };

            $scope.checkIt = function () { // la være å toggle fane hvis det er checkbox som klikkes
                checkIt = true;
            };

            function checkAndModifyAggregateOpenCloseButton () {
                var allOpen = true;
                var allClosed = true;
                for (var i = 0; i < $scope.meldinger.length; i++) {
                    if ($scope.control[i] && $scope.control[i].aapen) {
                        allClosed = false;
                    } else {
                        allOpen = false;
                    }
                }
                if ($scope.aapneAlleFaner && allClosed || !$scope.aapneAlleFaner && allOpen)  {
                    $scope.aapneAlleFaner = !$scope.aapneAlleFaner;
                }
            }

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
                    if ($scope.control[i] && $scope.control[i].endret) {
                        endret++;
                    }
                }

                var valgt = 0;
                for (var i = 0; i < $scope.meldinger.length; i++) {
                    if (!$scope.control[i]) {
                        $scope.control[i] = {};
                    }
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
                $scope.allePersoner.checked = (endret === 0 && $scope.meldinger.length === valgt) ||
                    (endret > 0 && endret === valgt);
                $scope.antallEndret = endret;
                $scope.antallValgt = valgt;
                $scope.visEndret = endret > 0;
            };

            var sletteMeldinger = function () {
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
            };

            $scope.lagre = function () {
                var buffer = [];
                for (var i = 0; i < $scope.meldinger.length; i++) {
                    if ($scope.control[i] && $scope.control[i].velg) {
                        buffer.push(prepLagrePerson($scope.meldinger[i]));
                    }
                }
                endringsmeldingService.oppdaterTestpersoner(buffer).then(
                    function (result) {
                        for (var i = 0; i < $scope.meldinger.length; i++) {
                            if ($scope.control[i] && $scope.control[i].velg) {
                                nullstillControl(i);
                                $scope.meldinger[i] = angular.copy(originalMeldinger[i]);
                            }
                        }
                        $scope.oppdaterValgt();
                        bekrefterLagring();
                        checkAndModifyAggregateOpenCloseButton();
                    },
                    function (error) {
                        utilsService.showAlertError(error);
                    }
                );
            };

            var oppdaterFunksjonsknapper = function () {
                var endret = false;
                for (var i = 0; i < $scope.control.length; i++) {
                    if ($scope.control[i]) {
                        if ($scope.control[i].endret) {
                            endret = true;
                        }
                    }
                }

                $scope.visEndret = endret;
            };

            $scope.endret = function (index) {
                var originalMelding = JSON.stringify(originalMeldinger[index]).replace(/null/g, '""') // Angular legger på $$hashKey, fjerner den
                    .replace(/,*"[A-Za-z0-9_]+":""/g, '')
                    .replace(/{}/g, '');
                var endretMelding = JSON.stringify($scope.meldinger[index]).replace(/null/g, '""')
                    .replace(/,*"\$\$hashKey":"[A-Za-z0-9_:]+"/g, '')
                    .replace(/,*"[A-Za-z0-9_]+":""/g, '')
                    .replace(/{}/g, '');

                $scope.control[index].endret = originalMelding !== endretMelding;
                $scope.control[index].velg = $scope.control[index].endret;
                $scope.oppdaterValgt();
            };

            var avbrytLagring = function () {
                for (var i = 0; i < $scope.meldinger.length; i++) {
                    if ($scope.control[i] && $scope.control[i].velg) {
                        $scope.meldinger[i] = JSON.parse(JSON.stringify(originalMeldinger[i]));
                        nullstillControl(i);
                    }
                }
                $scope.oppdaterValgt();
            };

            var nullstillControl = function (index) {
                $scope.control[index].endret = false;
                $scope.control[index].velg = false;
                $scope.control[index].aapen = false;
            };

            var bekrefterLagring = function (index) {
                var confirm = $mdDialog.confirm()
                    .title('Bekrefter lagring')
                    .textContent('Lagring er utført')
                    .ariaLabel('Bekrefter lagring')
                    .ok('OK');

                $mdDialog.show(confirm).then(function () {
                });
            };

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

            var bekreftRelokasjon = function (next, current) {
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
            };

            $rootScope.$on('$stateChangeStart', function (event, next, current) {
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

            $scope.toggleAlleFaner = function () {
                $scope.aapneAlleFaner = !$scope.aapneAlleFaner;
                for (var i = 0; i < $scope.meldinger.length; i++) {
                    if (!$scope.control[i]) {
                        $scope.control[i] = {};
                    }
                    $scope.control[i].aapen = $scope.aapneAlleFaner;
                }
            };

            var fetchMeldingsgruppe = function () {
                $scope.meldinger = undefined;
                endringsmeldingService.getGruppe($scope.gruppeId).then(
                    function (result) {
                        underHeaderService.setHeader(result.data.navn);
                        if (!result.data.meldinger) {
                            result.data.meldinger = [];
                        }
                        setHeaderButtons(result.data.meldinger.length);
                        setHeaderIcons();
                        originalMeldinger = result.data.meldinger;
                        $scope.meldinger = angular.copy(originalMeldinger);
                        $scope.control = [];
                        $scope.antallEndret = 0;
                        $scope.antallValgt = 0;
                        oppdaterFunksjonsknapper();
                    },
                    function (error) {
                        utilsService.showAlertError(error);
                        underHeaderService.setHeader("SKD Endringsmeldinger");
                    }
                );
            };

            fetchMeldingsgruppe();
        }]);
