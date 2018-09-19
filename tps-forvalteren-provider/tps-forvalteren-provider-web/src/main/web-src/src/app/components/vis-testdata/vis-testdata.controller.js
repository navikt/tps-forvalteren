angular.module('tps-forvalteren.vis-testdata', ['ngMessages'])
    .controller('VisTestdataCtrl', ['$scope', '$stateParams', '$filter', '$mdDialog', 'testdataService','gyldigAdresseService', 'utilsService', 'locationService',
        'headerService', 'toggleservice',
        function ($scope, $stateParams, $filter, $mdDialog, testdataService, gyldigAdresseService, utilsService, locationService, headerService, toggleservice) {

            $scope.persondetalj = "app/components/vis-testdata/person/person.html";
            $scope.gateadresse = "app/components/vis-testdata/adresse/gateadresse.html";
            $scope.matradresse = "app/components/vis-testdata/adresse/matrikkeladresse.html";
            $scope.postadresse = "app/components/vis-testdata/adresse/postadresse.html";

            $scope.service = testdataService;

            $scope.gruppeId = $stateParams.gruppeId;

            $scope.aapneAlleFaner = false;

            function setHeaderButtons() {
                headerService.setButtons([{
                    text: 'Legg til testpersoner',
                    icon: 'assets/icons/ic_add_circle_outline_black_24px.svg',
                    disabled: function () {
                        return $scope.visEndret
                    },
                    click: function () {
                        locationService.redirectToOpprettTestdata($scope.gruppeId);
                    }
                }, {
                    text: 'Send til TPS',
                    icon: 'assets/icons/ic_send_black_24px.svg',
                    disabled: function () {
                        return $scope.visEndret || !$scope.personer || $scope.personer.length === 0
                    },
                    click: function (ev) {
                        var confirm = $mdDialog.confirm({
                            controller: 'SendTilTpsCtrl',
                            templateUrl: 'app/components/vis-testdata/sendtiltps/send-til-tps.html',
                            parent: angular.element(document.body),
                            targetEvent: ev
                        });
                        $mdDialog.show(confirm);
                    }
                }, {
                    text: 'Opprett SKD',
                    icon: 'assets/icons/ic_launch_black_24px.svg',
                    disabled: function () {
                        return $scope.visEndret || !$scope.personer || $scope.personer.length === 0
                    },
                    click: function () {
                        testdataService.opprettSkdEndringsmeldingGruppe($scope.gruppeId).then(
                            function (result) {
                                locationService.redirectToOpprettSkdMeldinger(result.data.id);
                            },
                            function (error) {
                                utilsService.showAlertError(error);
                            }
                        );
                    }
                }
                ]);
            }

            function setHeaderIcons() {
                headerService.setIcons([{
                    icon: 'assets/icons/ic_mode_edit_black_24px.svg',
                    title: 'Endre testgruppe',
                    click: function (ev) {
                        var confirm = $mdDialog.confirm({
                            controller: 'EndreGruppeCtrl',
                            templateUrl: 'app/components/vis-testdata/endregruppe/endre-gruppe.html',
                            parent: angular.element(document.body),
                            targetEvent: ev,
                            locals: {
                                miljo: $scope.$resolve.environmentsPromise
                            },
                            bindToController: true,
                            controllerAs: 'ctrl'
                        });
                        $mdDialog.show(confirm).then(
                            function () { // Ser ut til å hindre duplikatkall mot rest-endepunkt
                            }
                        )
                    }
                }, {
                    icon: 'assets/icons/ic_delete_black_24px.svg',
                    title: 'Slette testgruppe',
                    click: function () {
                        var personTekst = $scope.personer.length > 0 ? ' med ' + $scope.personer.length + ' testperson' : '';
                        personTekst += $scope.personer.length > 1 ? 'er' : '';
                        var confirm = $mdDialog.confirm()
                            .title('Bekreft sletting')
                            .htmlContent('Ønsker du å slette gruppe <strong>' + headerService.getHeader().name + '</strong>' + personTekst + '?<br><br>' +
                                'Denne handlingen vil ikke slette testpersonene fra TPS, dersom de er opprettet der.')
                            .ariaLabel('Bekreft sletting')
                            .ok('OK')
                            .cancel('Avbryt');
                        $mdDialog.show(confirm).then(function () {
                            testdataService.sletteTestgruppe($scope.gruppeId).then(
                                function () {
                                    locationService.redirectToTestgruppe();
                                }
                            )
                        }, function () {
                            // Empty function to prevent unhandled rejection error
                        });
                    }
                }]);
            }

            $scope.allePersoner = {checked: false};
            $scope.personer = [];
            var originalPersoner = [];
            $scope.control = [];

            $scope.velgAlle = function () {
                var enabled = 0;
                $scope.personer.forEach(function (person, index) {
                    $scope.control[index] = $scope.control[index] || {};
                    if (!$scope.control[index].disabled) {
                        $scope.control[index].velg = !$scope.allePersoner.checked;
                        enabled++;
                    }
                });
                $scope.antallValgt = !$scope.allePersoner.checked ? enabled : 0;
                oppdaterFunksjonsknapper();
            };

            function hentTestpersoner() {
                $scope.showSpinner = true;
                $scope.personer = undefined;
                testdataService.getGruppe($scope.gruppeId, true).then(
                    function (result) {
                        headerService.setHeader(result.data.navn);
                        setHeaderButtons();
                        setHeaderIcons();
                        originalPersoner = result.data.personer;
                        prepOriginalPersoner();
                        $scope.personer = angular.copy(originalPersoner);
                        $scope.control = [];
                        $scope.antallEndret = 0;
                        $scope.antallValgt = 0;
                        oppdaterFunksjonsknapper();
                        headerService.eventUpdate();
                        $scope.showSpinner = false;
                    },
                    function (error) {
                        utilsService.showAlertError(error);
                        headerService.setHeader("Testdata");
                        $scope.showSpinner = false;
                    }
                );
            }

            function prepOriginalPersoner() {
                for (var i = 0; i < originalPersoner.length; i++) {
                    etablerAdressetype(originalPersoner[i]);
                    fixDatoForDatepicker(originalPersoner[i]);
                    fixCase(originalPersoner[i]);
                }
            }

            function fixCase(person) {
                person.fornavn = $filter('titlecase')(person.fornavn);
                if (person.mellomnavn) {
                    person.mellomnavn = $filter('titlecase')(person.mellomnavn);
                }
                person.etternavn = $filter('titlecase')(person.etternavn);
                if (person.postadresse && person.postadresse[0]) {
                    if (person.postadresse[0].postLinje1) {
                        person.postadresse[0].postLinje1 = $filter('titlecase')(person.postadresse[0].postLinje1);
                    }
                    if (person.postadresse[0].postLinje2) {
                        person.postadresse[0].postLinje2 = $filter('titlecase')(person.postadresse[0].postLinje2);
                    }
                    if (person.postadresse[0].postLinje3) {
                        person.postadresse[0].postLinje3 = $filter('titlecase')(person.postadresse[0].postLinje3);
                    }
                }
            }

            // Datofix kjøres etter denne
            function etablerAdressetype(person) {
                if (person.boadresse) {
                    if (person.boadresse.adressetype === 'GATE') {
                        person.gateadresse = angular.copy(person.boadresse);
                        person.gateadresse.gateadresse = $filter('titlecase')(person.gateadresse.gateadresse);
                        person.gateadresse.husnummer = $filter('uppercase')(person.gateadresse.husnummer);
                    } else if (person.boadresse.adressetype === 'MATR') {
                        person.matrikkeladresse = angular.copy(person.boadresse);
                        person.matrikkeladresse.mellomnavn = $filter('titlecase')(person.matrikkeladresse.mellomnavn);
                    }
                } else {
                    person.boadresse = {};
                    person.boadresse.adressetype = 'GATE';
                }
            }

            // Denne fikser bug i Material datepicker, ved at feltet finnes i modell vil klikk i feltet være uten sideeffekt
            function fixDatoForDatepicker(person) {
                person.regdato = person.regdato || null;
                person.statsborgerskapRegdato = person.statsborgerskapRegdato || null;
                person.innvandretFraLandFlyttedato = person.innvandretFraLandFlyttedato || null;
                person.spesregDato = person.spesregDato || null;
                person.doedsdato = person.doedsdato || null;
                person.gateadresse = person.gateadresse || {};
                person.gateadresse.flyttedato = person.gateadresse.flyttedato || null;
                person.matrikkeladresse = person.matrikkeladresse || {};
                person.matrikkeladresse.flyttedato = person.matrikkeladresse.flyttedato || null;
            }

            $scope.sletteDialog = function (index) {
                var confirm = $mdDialog.confirm()
                    .title('Bekreft sletting')
                    .textContent('Bekreft sletting av valgte personer')
                    .ariaLabel('Bekreft sletting')
                    .ok('OK')
                    .cancel('Avbryt');

                $mdDialog.show(confirm).then(function () {
                    sletteTestpersoner();
                }, function () {
                    // Prevent unhandled rejection error
                });
            };

            $scope.oppdaterValgt = function () {

                var endret = 0;
                for (var i = 0; i < $scope.personer.length; i++) {
                    if ($scope.control[i] && $scope.control[i].endret) {
                        endret++;
                    }
                }

                var valgt = 0;
                for (var j = 0; j < $scope.personer.length; j++) {
                    if (!$scope.control[j]) {
                        $scope.control[j] = {};
                    }
                    if (endret > 0) {
                        $scope.control[j].disabled = !$scope.control[j].endret;
                        if (!$scope.control[j].endret) {
                            $scope.control[j].velg = false;
                        }
                    } else {
                        $scope.control[j].disabled = false;
                    }
                    if ($scope.control[j].velg) {
                        valgt++;
                    }
                }
                $scope.allePersoner.checked = (endret === 0 && $scope.personer.length === valgt) ||
                    (endret > 0 && endret === valgt);
                $scope.antallEndret = endret;
                $scope.antallValgt = valgt;
                $scope.visEndret = endret > 0;
            };

            function sletteTestpersoner() {
                var identer = [];
                for (var i = 0; i < $scope.personer.length; i++) {
                    if ($scope.control[i].velg) {
                        identer.push($scope.personer[i].personId);
                    }
                }
                testdataService.sletteTestpersoner(identer).then(
                    function (result) {
                        hentTestpersoner();
                    },
                    function (error) {
                        utilsService.showAlertError(error);
                    }
                );
            }

            $scope.lagre = function () {
                var buffer = [];
                for (var i = 0; i < $scope.personer.length; i++) {
                    if ($scope.control[i] && $scope.control[i].velg) {
                        buffer.push(prepLagrePerson($scope.personer[i]));
                    }
                }
                console.log(buffer)
                testdataService.oppdaterTestpersoner(buffer).then(
                    function (result) {
                        for (var i = 0; i < $scope.personer.length; i++) {
                            if ($scope.control[i] && $scope.control[i].velg) {
                                $scope.control[i] = {};
                                originalPersoner[i] = angular.copy($scope.personer[i]);
                                etablerAdressetype(originalPersoner[i]);
                                fixDatoForDatepicker(originalPersoner[i]);
                                fixCase(originalPersoner[i]);
                            }
                        }
                        $scope.oppdaterValgt();
                        bekrefterLagring();
                        $scope.aapneAlleFaner = toggleservice.checkAggregateOpenCloseButtonNextState(
                            $scope.aapneAlleFaner, $scope.control, $scope.pager, $scope.personer.length);
                    },
                    function (error) {
                        utilsService.showAlertError(error);
                    }
                );
            };

            function prepLagrePerson(person) {
                var adressetype = person.boadresse.adressetype;
                if (adressetype === 'GATE') {
                    person.boadresse = angular.copy(person.gateadresse);
                    person.matrikkeladresse = undefined;
                } else if (adressetype === 'MATR') {
                    person.boadresse = angular.copy(person.matrikkeladresse);
                    person.gateadresse = undefined;
                }
                person.boadresse.adressetype = adressetype;
                fixTimezone(person.boadresse.flyttedato);
                fixTimezone(person.regdato);
                fixTimezone(person.statsborgerskapRegdato);
                fixTimezone(person.spesregDato);
                fixTimezone(person.doedsdato);
                fixTimezone(person.innvandretFraLandFlyttedato);
                return person;
            }

            function fixTimezone(date) {
                if (date && date.toString().length > 19) {
                    date.setMinutes(date.getTimezoneOffset() * -1);
                }
            }

            function oppdaterFunksjonsknapper() {
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
                var originalPerson = JSON.stringify(originalPersoner[index]).replace(/null/g, '""') // Angular legger på $$hashKey, fjerner den
                    .replace(/,*"[A-Za-z0-9_]+":""/g, '')
                    .replace(/{}/g, '');
                var endretPerson = JSON.stringify($scope.personer[index]).replace(/null/g, '""')
                    .replace(/,*"\$\$hashKey":"[A-Za-z0-9_:]+"/g, '')
                    .replace(/,*"[A-Za-z0-9_]+":""/g, '')
                    .replace(/{}/g, '');

                $scope.control[index].endret = originalPerson !== endretPerson;
                $scope.control[index].velg = $scope.control[index].endret;
                $scope.oppdaterValgt();
            };

            function avbrytLagring() {
                for (var i = 0; i < $scope.personer.length; i++) {
                    if ($scope.control[i] && $scope.control[i].velg) {
                        $scope.personer[i] = JSON.parse(JSON.stringify(originalPersoner[i]));
                        $scope.control[i] = {};
                    }
                }
                $scope.slice = $scope.personer.slice($scope.pager.startIndex, $scope.pager.endIndex + 1);
                $scope.oppdaterValgt();
            }

            function bekrefterLagring(index) {
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

            function bekreftRelokasjon(next, current) {
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

            $scope.$watch('visEndret', function () {
                headerService.eventUpdate();
            });

            var checkIt = false;

            $scope.checkIt = function () { // la være å toggle fane hvis det er checkbox som klikkes
                checkIt = true;
            };

            $scope.toggleFane = function (index) {
                if ($scope.requestForm.$valid) {
                    if (!checkIt) {
                        toggleservice.toggleFane($scope.control, index);
                        $scope.aapneAlleFaner = toggleservice.checkAggregateOpenCloseButtonNextState(
                            $scope.aapneAlleFaner, $scope.control, $scope.pager, $scope.personer.length);
                    }
                    checkIt = false;
                }
            };

            $scope.showAddrInfo = function () {
                var info = $mdDialog.alert()
                    .title('Adresseinfo')
                    .htmlContent('Når gatenavn evt stedsnavn angis må alle tilhørende felter ha verdi for å utgjøre en gyldig adresse.')
                    .ariaLabel('Informasjon om adresse')
                    .ok('OK')
                    .clickOutsideToClose(true);
                $mdDialog.show(info);
            };

            $scope.$watch('pager.startIndex', function () {
                if ($scope.personer) {
                    $scope.aapneAlleFaner = toggleservice.checkAggregateOpenCloseButtonNextState(
                        $scope.aapneAlleFaner, $scope.control, $scope.pager, $scope.personer.length);
                }
            });

            $scope.toggleAlleFaner = function () {
                $scope.aapneAlleFaner = toggleservice.toggleAlleFaner($scope.aapneAlleFaner, $scope.control, $scope.pager);
            };

            $scope.$on('tps-sent', function(event, args){
                hentTestpersoner();
            });

            $scope.hentgyldigeAdresser = function (person) {

                gyldigAdresseService.finnGyldigAdresse(person.gateadresse).then(
                    function (result) {
                        if(result.data.response.status.kode!="00") {
                            utilsService.showAlertDialog(result.data.response.status.utfyllendeMelding, "Finn gyldig adresse");
                        }
                        var responsAdresser = result.data.response.data1.adrData;
                        if(utilsService.isArray(responsAdresser)) {
                            $scope.gyldigeAdresser = responsAdresser;
                        }else {
                            $scope.gyldigeAdresser = [responsAdresser];
                        }

                        for(var i = 0; i<$scope.gyldigeAdresser.length; i++) {
                                $scope.gyldigeAdresser[i].visningsnavn = createVisningsnavn($scope.gyldigeAdresser[i]);
                            }

                    },
                    function (error) {
                            utilsService.showAlertError(error);
                    }

                );

                function createVisningsnavn(adresse) {
                    return adresse.adrnavn +", husnr "+
                        adresse.husnrfra +"-"+ adresse.husnrtil+", "+
                        adresse.gkode + ","
                        + adresse.pnr + " "+adresse.psted + ", "
                        + adresse.knr + " " + adresse.knavn ;
                }

            };

            $scope.updateAdresseGyldig = function (selectedGyldigAdresse, person) {
                person.gateadresse.gateadresse = selectedGyldigAdresse.adrnavn;
                var husnrFra = parseInt(selectedGyldigAdresse.husnrfra);
                var husnrTil = parseInt(selectedGyldigAdresse.husnrtil);
                if(typeof person.gateadresse.husnummer==='undefined' || husnrFra>person.gateadresse.husnummer || husnrTil<person.gateadresse.husnummer){
                person.gateadresse.husnummer=""+Math.floor( Math.random()*(husnrTil-husnrFra +1)+ husnrFra );
                }
                person.gateadresse.gatekode = ""+selectedGyldigAdresse.gkode;
                if(typeof selectedGyldigAdresse.pnr !=='undefined'){
                    person.gateadresse.postnr = selectedGyldigAdresse.pnr;
                }
                person.gateadresse.kommunenr= selectedGyldigAdresse.knr;
            };

            hentTestpersoner();
        }]);
