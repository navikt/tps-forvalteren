angular.module('tps-forvalteren.skd-vis-meldingsgruppe', ['ngMessages'])
    .controller('SkdVisMeldigsgruppeCtrl', ['$scope', '$rootScope', '$stateParams', '$filter', '$mdDialog', '$copyToClipboard', 'endringsmeldingService',
        'utilsService', 'locationService', 'headerService',
        function ($scope, $rootScope, $stateParams, $filter, $mdDialog, $copyToClipboard, endringsmeldingService, utilsService, locationService, headerService) {

            $scope.meldingstypeT1 = "app/components/endringsmelding/skd/meldingstype/meldingstype-t1.html";
            $scope.meldingstypeT2 = "app/components/endringsmelding/skd/meldingstype/meldingstype-t2.html";

            $scope.service = endringsmeldingService;
            $scope.gruppeId = $stateParams.gruppeId;

            $scope.aapneAlleFaner = false;

            function setHeaderButtons (antall_personer) {
                var disable_send_til_tps_button = antall_personer < 1;
                headerService.setButtons([{
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
                for (var i = 0; i < $scope.meldinger.length; i++) {
                    if ($scope.control[i] && $scope.control[i].velg) {
                        buffer.push($scope.meldinger[i]);
                    }
                }
                endringsmeldingService.updateMeldinger(buffer).then(
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

            function avbrytLagring () {
                for (var i = 0; i < $scope.meldinger.length; i++) {
                    if ($scope.control[i] && $scope.control[i].velg) {
                        $scope.meldinger[i] = JSON.parse(JSON.stringify(originalMeldinger[i]));
                        nullstillControl(i);
                    }
                }
                $scope.oppdaterValgt();
            }

            function nullstillControl (index) {
                $scope.control[index].endret = false;
                $scope.control[index].velg = false;
                $scope.control[index].aapen = false;
            }

            function bekrefterLagring (index) {
                var confirm = $mdDialog.confirm()
                    .title('Bekrefter lagring')
                    .textContent('Lagring er utført')
                    .ariaLabel('Bekrefter lagring')
                    .ok('OK');

                $mdDialog.show(confirm).then(function () {
                });
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
                $scope.meldinger.forEach(function(melding, index) {
                    $scope.control[index] = $scope.control[index] = {};
                    $scope.control[index].aapen = $scope.aapneAlleFaner;
                });
            };

            function confirmClipboardCopy (next, current) {
                var confirm = $mdDialog.confirm()
                    .title('Kopiering vellykket utført!')
                    .textContent('SKD-melding har blitt kopiert til utklippstavle.')
                    .ariaLabel('Bekreftelse på kopiering av SKD-melding til utklippstavle.')
                    .ok('OK');
                $mdDialog.show(confirm);
            }

            $scope.copyToClipboard = function (index) {
                endringsmeldingService.convertMelding($scope.meldinger[index]).then(
                    function (data) {
                        $copyToClipboard.copy(data);
                        confirmClipboardCopy();
                    }, function (error) {
                        utilsService.showAlertError(error);
                    });
            };

            var fetchMeldingsgruppe = function () {
                $scope.showSpinner = true;
                $scope.meldinger = undefined;
                endringsmeldingService.getGruppe($scope.gruppeId).then(
                    function (result) {
                        headerService.setHeader(result.data.navn);
                        if (!result.data.meldinger) {
                            result.data.meldinger = [{
                                beskrivelse: 'T1-melding med alle felter ifyllt',
                                meldingstype: 't1',
                                fodselsdato: '130358',
                                personnummer: '12345',
                                maskindato: '20100908',
                                maskintid: '123456',
                                transtype: '1',
                                aarsakskode: '12',
                                regDato:     '20150908',
                                statuskode:  'K',
                                datoDoed:     '20171031',
                                slektsnavn:   'Asbjørnsen og Moe Dahl',
                                fornavn:       'Gjertrud Gurine',
                                mellomnavn:    'Marie',
                                slekstnavnUgift:  'Torleifsdottir',
                                forkortetNavn:  'ABAC',
                                regDatoNavn:    '20100212',
                                foedekommLand:  'XVH3',
                                foedested:      'Gloppen',
                                statsborgerskap: '123',
                                regdatoStatsb:   '19981209',
                                familienummer:   '20020617231',
                                regdatoFamnr:    '20001222',
                                personkode:      '1',
                                spesRegType:     '1',
                                datoSpesRegType: '20120405',
                                sivilstand:      '1',
                                regdatoSivilstand:  '19870216',
                                ektefellePartnerFdato:  '130408',
                                ektefellePartnerPnr:  '12333',
                                ektefellePartnerNavn:  'Hans Christian',
                                ektefellePartnerStatsb:  '456',
                                regdatoAdr:       '20134512',
                                flyttedatoAdr:  '20160313',
                                kommunenummer:   '1234',
                                gateGaard:       '12345',
                                husBruk: '1234',
                                bokstavFestenr: '1234',
                                undernr:         '123',
                                adressenavn:    '25 tegn adresse her',
                                adressetype:     'A',
                                tilleggsadresse:   '25 tegn adresse her',
                                postnummer:   '1394',
                                valgkrets:      '0220',
                                adresse1:       '30 tegn adresse her',
                                adresse2:       '30 tegn adresse her',
                                adresse3:       '30 tegn adresse her',
                                postadrLand:    '123',
                                innvandretFraLand:  '123',
                                fraLandRegdato: '20120323',
                                fraLandFlyttedato: '20130212',
                                fraKommune:     '0223',
                                fraKommRegdato:  '19970213',
                                fraKommFlyttedato: '20120512',
                                utvandretTilLand: '123',
                                tilLandRegdato: '20120512',
                                tilLandFlyttedato: '20120512',
                                samemanntall:  'G',
                                datoSamemanntall: '20120512',
                                umyndiggjort:  'K',
                                datoUmyndiggjort: '20120512',
                                foreldreansvar:  'B',
                                datoForeldreansvar: '20120512',
                                arbeidstillatelse: 'H',
                                datoArbeidstillatelse: '20120512',
                                fremkonnummer:  '12345678',
                                morsFodselsdato: '120512',
                                morsPersonnummer: '12345',
                                morsNavn:   '50 tegn navn her',
                                morsStatsbSkap:  '123',
                                farsFodselsdato:  '120358',
                                farsPersonnummer:  '12345',
                                farsFarsNavn:   '50 tegn navn her',
                                farsStatsbSkap:  '123',
                                tidligereFnrDnr:  '12345678901',
                                datoTidlFnrDnr: '20120512',
                                nyttFnr: '12345678901',
                                datoNyttFnr: '20120512',
                                levendeDoed: 'D',
                                kjonn: 'F',
                                tildelingskode:  'P',
                                foedselstype:  'AB',
                                morsSiviltilstand: 'R',
                                ekteskPartnskNr:  'G',
                                ektfEkteskPartnskNr: 'U',
                                vigselstype:  'K',
                                forsByrde: 'RS',
                                dombevilling:  'T',
                                antallBarn: 'RE',
                                tidlSivilstand: 'V',
                                ektfTidlSivilstand: 'J',
                                hjemmel: 'P',
                                fylke: 'RE',
                                vigselskomm: '1234',
                                tidlSepDomBev: 'W',
                                begjertAv: 'Q',
                                registrGrunnlag: 'C',
                                doedssted: 'ABCD',
                                typeDoedssted: 'A',
                                vigselsdato: '20120512',
                                medlKirken: 'D',
                                sekvensnr: '123456',
                                bolignr: 'ABCED',
                                dufId: '123456789012',
                                brukerident: 'ABCDEFGH',
                                skolerets:  '1234',
                                tkNr:  '1234',
                                dnrHjemlandsadresse1: '40 tegn her',
                                dnrHjemlandsadresse2: '40 tegn her',
                                dnrHjemlandsadresse3: '40 tegn her',
                                dnrHjemlandLandkode: '123',
                                dnrHjemlandRegDato: '20120512',
                                dnrIdKontroll: 'S',
                                postadrRegDato: '20120512',
                                utvandringstype: 'P',
                                grunnkrets:  '1234',
                                statsborgerskap2: '123',
                                regdatoStatsb2: '20120512',
                                statsborgerskap3: '123',
                                regdatoStatsb3: '20120512',
                                statsborgerskap4: '123',
                                regdatoStatsb4: '20120512',
                                statsborgerskap5: '123',
                                regdatoStatsb5: '20120512',
                                statsborgerskap6: '123',
                                regdatoStatsb6: '20120512',
                                statsborgerskap7: '123',
                                regdatoStatsb7: '20120512',
                                statsborgerskap8: '123',
                                regdatoStatsb8: '20120512',
                                statsborgerskap9: '123',
                                regdatoStatsb9: '20120512',
                                statsborgerskap10: '123',
                                regdatoStatsb10: '20120512',
                                bibehold: 'W',
                                regdatoBibehold: '20120512',
                                saksid:         '1234567',
                                embete:         'ABCD',
                                sakstype:       'GEH',
                                vedtaksdato:     '20120417',
                                internVergeid:    '1234567',
                                vergeFnrDnr:      '12345678901',
                                vergetype:        'GHE',
                                mandattype:       'MLE',
                                mandatTekst:      '100 tegn her',
                                reserverFramtidigBruk:  '140 tegn'
                            },
                                {
                                    beskrivelse: 'Tom T1-melding',
                                    meldingstype: 't1'
                                },
                                {
                                    beskrivelse: 'T2-melding med alle felter ifyllt',
                                    meldingstype: 't2',
                                    fodeselsnr: '12345678901',
                                    maskindato: '20120405',
                                    maskintid:  '122345',
                                    transtype:  '2',
                                    aarsakskode: '22',
                                    barnFodsdato1: '123456',
                                    barnPersnr1:  '12345',
                                    barnNavn1: '50 tegn her',
                                    barnKjoenn1: 'M',
                                    barnFodsdato2: '123456',
                                    barnPersnr2:  '12345',
                                    barnNavn2: '50 tegn her',
                                    barnKjoenn2: 'M',
                                    barnFodsdato3: '123456',
                                    barnPersnr3:  '12345',
                                    barnNavn3: '50 tegn her',
                                    barnKjoenn3: 'M',
                                    barnFodsdato4: '123456',
                                    barnPersnr4:  '12345',
                                    barnNavn4: '50 tegn her',
                                    barnKjoenn4: 'M',
                                    barnFodsdato5: '123456',
                                    barnPersnr5:  '12345',
                                    barnNavn5: '50 tegn her',
                                    barnKjoenn5: 'M',
                                    barnFodsdato6: '123456',
                                    barnPersnr6:  '12345',
                                    barnNavn6: '50 tegn her',
                                    barnKjoenn6: 'M',
                                    barnFodsdato7: '123456',
                                    barnPersnr7:  '12345',
                                    barnNavn7: '50 tegn her',
                                    barnKjoenn7: 'M',
                                    barnFodsdato8: '123456',
                                    barnPersnr8:  '12345',
                                    barnNavn8: '50 tegn her',
                                    barnKjoenn8: 'M',
                                    barnFodsdato9: '123456',
                                    barnPersnr9:  '12345',
                                    barnNavn9: '50 tegn her',
                                    barnKjoenn9: 'M',
                                    barnFodsdato10: '123456',
                                    barnPersnr10:  '12345',
                                    barnNavn10: '50 tegn her',
                                    barnKjoenn10: 'M',
                                    barnFodsdato11: '123456',
                                    barnPersnr11:  '12345',
                                    barnNavn11: '50 tegn her',
                                    barnKjoenn11: 'M',
                                    barnFodsdato12: '123456',
                                    barnPersnr12:  '12345',
                                    barnNavn12: '50 tegn her',
                                    barnKjoenn12: 'M',
                                    barnFodsdato13: '123456',
                                    barnPersnr13:  '12345',
                                    barnNavn13: '50 tegn her',
                                    barnKjoenn13: 'M',
                                    sekvensnr: '123456'
                                },
                                {
                                    beskrivelse: 'Tom T2-melding',
                                    meldingstype: 't2'
                                }
                            ];
                        }
                        setHeaderButtons(result.data.meldinger.length);
                        setHeaderIcons();
                        originalMeldinger = result.data.meldinger;
                        $scope.meldinger = angular.copy(originalMeldinger);
                        $scope.control = [];
                        $scope.antallEndret = 0;
                        $scope.antallValgt = 0;
                        oppdaterFunksjonsknapper();
                        $scope.showSpinner = false;
                    },
                    function (error) {
                        utilsService.showAlertError(error);
                        headerService.setHeader("SKD Endringsmeldinger");
                        $scope.showSpinner = false;
                    }
                );
            };

            fetchMeldingsgruppe();
        }]);
