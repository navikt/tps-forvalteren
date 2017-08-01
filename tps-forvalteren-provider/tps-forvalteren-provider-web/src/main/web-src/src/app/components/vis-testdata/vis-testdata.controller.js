angular.module('tps-forvalteren.vis-testdata', ['ngMessages'])
    .controller('VisTestdataCtrl', ['$scope', 'testdataService', 'utilsService', 'locationService', '$mdDialog', '$rootScope',
        'headerService', '$location',
        function ($scope, testdataService, utilsService, locationService, $mdDialog, $rootScope, underHeaderService, $location) {

            $scope.persondetalj = "app/components/vis-testdata/person/person.html";
            $scope.gateadresse = "app/components/vis-testdata/adresse/gateadresse.html";
            $scope.matradresse = "app/components/vis-testdata/adresse/matrikkeladresse.html";
            $scope.postadresse = "app/components/vis-testdata/adresse/postadresse.html";

            $scope.kommuner = [];
            $scope.postnummer = [];

            var gruppeId = $location.url().match(/\d+/g);

            var setHeaderButtons = function (antall_personer) {
                var disable_send_til_tps_button = antall_personer < 1;
                underHeaderService.setButtons([{
                    text: 'Legg til testpersoner',
                    icon: 'assets/icons/ic_add_circle_outline_black_24px.svg',
                    click: function () {
                        locationService.redirectToOpprettTestdata(gruppeId);
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
                    title: 'Endre testgruppe',
                    click: function (ev) {
                        var confirm = $mdDialog.confirm({
                            controller: 'EndreGruppeCtrl',
                            templateUrl: 'app/components/vis-testdata/endregruppe/endregruppe.html',
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
                            .htmlContent('Ønsker du å slette gruppe <strong>' + underHeaderService.getHeader().name + '</strong>' + personTekst + '?<br><br>' +
                                'Denne handlingen vil ikke slette testpersonene fra TPS, dersom de er opprettet der.')
                            .ariaLabel('Bekreft sletting')
                            .ok('OK')
                            .cancel('Avbryt');
                        $mdDialog.show(confirm).then(function () {
                            testdataService.sletteTestgruppe(gruppeId).then(
                                function () {
                                    locationService.redirectToTestgruppe();
                                }
                            )
                        });
                    }
                }]);
            };

            $scope.allePersoner = {checked: false};
            $scope.personer = [];
            var originalPersoner = [];
            $scope.control = [];

            $scope.velgAlle = function () {
                var enabled = 0;
                for (var i = 0; i < $scope.personer.length; i++) {
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

            var hentTestpersoner = function () {
                $scope.personer = undefined;
                testdataService.getTestpersoner(gruppeId).then(
                    function (result) {
                        underHeaderService.setHeader(result.data.navn);
                        setHeaderButtons(result.data.personer.length);
                        setHeaderIcons();
                        originalPersoner = result.data.personer;
                        prepOriginalPersoner();
                        $scope.personer = angular.copy(originalPersoner);
                        $scope.control = [];
                        $scope.antallEndret = 0;
                        $scope.antallValgt = 0;
                        prepPersoner();
                        oppdaterFunksjonsknapper();
                    },
                    function (error) {
                        utilsService.showAlertError(error);
                        underHeaderService.setHeader("Testdata");
                    }
                );
            };

            var hentKommuner = function () {
                testdataService.hentKommuner().then(
                    function (result) {
                        for (var index = 0; index < result.data.length; index++) {
                            var kommune = {};
                            kommune.term = result.data[index].term.toUpperCase();
                            kommune.navn = result.data[index].navn;
                            kommune.visningsnavn = result.data[index].term.toUpperCase() + " - " + result.data[index].navn;
                            $scope.kommuner.push(kommune);
                        }
                        $scope.kommuner.sort(function (a, b) {
                            return a.term.length - b.term.length;
                        });
                    },
                    function (error) {
                        utilsService.showAlertError(error);
                        underHeaderService.setHeader("Testdata");
                    }
                );
            };

            var hentPostnummer = function () {
                testdataService.hentPostnummer().then(
                    function (result) {
                        for (var index = 0; index < result.data.length; index++) {
                            var postnummer = {};
                            postnummer.term = result.data[index].term.toUpperCase();
                            postnummer.navn = result.data[index].navn;
                            postnummer.visningsnavn = result.data[index].term.toUpperCase() + " - " + result.data[index].navn;
                            $scope.postnummer.push(postnummer);
                        }
                        $scope.postnummer.sort(function (a, b) {
                            return a.term.length - b.term.length;
                        });
                    },
                    function (error) {
                        utilsService.showAlertError(error);
                        underHeaderService.setHeader("Testdata");
                    }
                );
            };

            $scope.getPostnummerMatches = function (searchText) {
                var result = [];
                var searchTextUpperCase = searchText.toUpperCase();
                for (var index = 0; index < $scope.postnummer.length; index++) {
                    if ($scope.postnummer[index].visningsnavn.indexOf(searchTextUpperCase) !== -1) {
                        result.push($scope.postnummer[index]);
                    }
                }
                return result;
            };

            $scope.getKommuneMatches = function (searchText) {
                var result = [];
                var searchTextUpperCase = searchText.toUpperCase();
                for (var index = 0; index < $scope.kommuner.length; index++) {
                    if ($scope.kommuner[index].visningsnavn.indexOf(searchTextUpperCase) !== -1) {
                        result.push($scope.kommuner[index]);
                    }
                }
                return result;
            };

            var prepOriginalPersoner = function () {
                for (var i = 0; i < originalPersoner.length; i++) {
                    etablerAdressetype(originalPersoner[i]);
                    fixDatoForDatepicker(originalPersoner[i]);
                    fixKommunenr(originalPersoner[i]);
                    fixPostnummer(originalPersoner[i]);
                }
            };

            var prepPersoner = function () {
                for (var index = 0; index < $scope.personer.length; index++) {
                    fixKommunenr($scope.personer[index]);
                    fixPostnummer($scope.personer[index]);
                }
            };

            // Datofix kjøres etter denne
            var etablerAdressetype = function (person) {
                if (person.boadresse) {
                    if (person.boadresse.adressetype === 'GATE') {
                        person.gateadresse = angular.copy(person.boadresse);
                    } else if (person.boadresse.adressetype === 'MATR') {
                        person.matrikkeladresse = angular.copy(person.boadresse);
                    }
                } else {
                    person.boadresse = {};
                    person.boadresse.adressetype = 'GATE';
                }
            };

            // Denne fikser bug i Material datepicker, ved at feltet finnes i modell vil klikk i feltet være uten sideeffekt
            var fixDatoForDatepicker = function (person) {
                person.regdato = person.regdato ? person.regdato : null;
                person.spesregDato = person.spesregDato ? person.spesregDato : null;

                if (!person.boadresse || !person.boadresse.gateadresse || !person.boadresse.gateadresse.flytteDato) {
                    person.gateadresse = person.gateadresse && !Array.isArray(person.gateadresse) ? person.gateadresse : {};
                    person.gateadresse.flytteDato = null;
                }

                if (!person.boadresse || !person.boadresse.matrikkeladresse || !person.boadresse.matrikkeladresse.flytteDato) {
                    person.matrikkeladresse = person.matrikkeladresse ? person.matrikkeladresse : {};
                    person.matrikkeladresse.flytteDato = null;
                }
            };

            var fixKommunenr = function (person) {
                for (var index = 0; index < $scope.kommuner.length; index++) {
                    if ($scope.kommuner[index].navn === person.gateadresse.kommunenr) {
                        person.gateadresse.kommunenr = $scope.kommuner[index];
                    }
                }
            };

            var fixPostnummer = function (person) {
                for (var index = 0; index < $scope.postnummer.length; index++) {
                    if ($scope.postnummer[index].navn === person.gateadresse.postnr) {
                        person.gateadresse.postnr = $scope.postnummer[index];
                    }
                }
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
            };

            $scope.checkIt = function () {
                checkIt = true;
            };

            $scope.sletteDialog = function (index) {
                var confirm = $mdDialog.confirm()
                    .title('Bekreft sletting')
                    .textContent('Bekreft sletting av valgte personer')
                    .ariaLabel('Bekreft sletting')
                    .ok('OK')
                    .cancel('Avbryt');

                $mdDialog.show(confirm).then(function () {
                    sletteTestpersoner();
                });
            };

            $scope.oppdaterValgt = function () {
                oppdaterFane = true;

                var endret = 0;
                for (var i = 0; i < $scope.personer.length; i++) {
                    if ($scope.control[i] && $scope.control[i].endret) {
                        endret++;
                    }
                }

                var valgt = 0;
                for (var i = 0; i < $scope.personer.length; i++) {
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
                $scope.allePersoner.checked = (endret == 0 && $scope.personer.length == valgt) ||
                    (endret > 0 && endret == valgt);
                $scope.antallEndret = endret;
                $scope.antallValgt = valgt;
                $scope.visEndret = endret > 0;
            };

            var sletteTestpersoner = function () {
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
            };

            $scope.lagre = function () {
                var buffer = [];
                for (var i = 0; i < $scope.personer.length; i++) {
                    if ($scope.control[i] && $scope.control[i].velg) {
                        buffer.push(prepLagrePerson($scope.personer[i]));
                    }
                }
                testdataService.oppdaterTestpersoner(buffer).then(
                    function (result) {
                        for (var i = 0; i < $scope.personer.length; i++) {
                            if ($scope.control[i] && $scope.control[i].velg) {
                                nullstillControl(i);
                                originalPersoner[i] = angular.copy($scope.personer[i]);
                                etablerAdressetype(originalPersoner[i]);
                                fixDatoForDatepicker(originalPersoner[i]);
                            }
                        }
                        $scope.oppdaterValgt();
                        bekrefterLagring();
                    },
                    function (error) {
                        utilsService.showAlertError(error);
                    }
                );
            };

            var prepLagrePerson = function (person) {
                var adressetype = person.boadresse.adressetype;
                if (adressetype === 'GATE') {
                    person.boadresse = angular.copy(person.gateadresse);
                    if (person.boadresse.kommunenr && person.boadresse.kommunenr.navn) {
                        person.boadresse.kommunenr = person.boadresse.kommunenr.navn;
                    }
                    if (person.boadresse.postnr && person.boadresse.postnr.navn) {
                        person.boadresse.postnr = person.boadresse.postnr.navn;
                    }
                    person.matrikkeladresse = undefined;
                } else if (adressetype === 'MATR') {
                    person.boadresse = angular.copy(person.matrikkeladresse);
                    if (person.boadresse.kommunenr && person.boadresse.kommunenr.navn) {
                        person.boadresse.kommunenr = person.boadresse.kommunenr.navn;
                    }
                    if (person.boadresse.postnr && person.boadresse.postnr.navn) {
                        person.boadresse.postnr = person.boadresse.postnr.navn;
                    }
                    person.gateadresse = undefined;
                }
                person.boadresse.adressetype = adressetype;
                return person;
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
                var originalPerson = JSON.stringify(originalPersoner[index]).replace(/null/g, '""') // Angular legger på $$hashKey, fjerner den
                    .replace(/,*"[A-Za-z0-9_]+":""/g, '')
                    .replace(/{}/g, '');
                var endretPerson = JSON.stringify($scope.personer[index]).replace(/null/g, '""')
                    .replace(/,*"\$\$hashKey":"[A-Za-z0-9_:]+"/g, '')
                    .replace(/,*"[A-Za-z0-9_]+":""/g, '')
                    .replace(/{}/g, '');

                $scope.control[index].endret = originalPerson != endretPerson;
                $scope.control[index].velg = $scope.control[index].endret;
                $scope.oppdaterValgt();
            };

            var avbrytLagring = function () {
                for (var i = 0; i < $scope.personer.length; i++) {
                    if ($scope.control[i] && $scope.control[i].velg) {
                        $scope.personer[i] = JSON.parse(JSON.stringify(originalPersoner[i]));
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
                });
            };

            var bekreftRelokasjon = function (next) {
                var confirm = $mdDialog.confirm()
                    .title('Du har endringer som ikke er lagret')
                    .textContent('Trykk OK for å forlate siden.')
                    .ariaLabel('Bekreftelse på relokasjon og endringer ikke lagret')
                    .ok('OK')
                    .cancel('Avbryt');

                $mdDialog.show(confirm).then(function () {
                    $scope.visEndret = false;
                    locationService.redirectUrl(next.url);
                });
            };

            $rootScope.$on('$stateChangeStart', function (event, next, current) {
                if ($scope.visEndret) {
                    event.preventDefault();
                    bekreftRelokasjon(next);
                }
            });

            window.onbeforeunload = function (event) {
                if ($scope.visEndret) {
                    return 'Du har data som ikke er lagret. Vil du forlate siden?'; // Trigger nettlesers visning av dialogboks for avslutning
                }
            };

            hentKommuner();
            hentPostnummer();
            hentTestpersoner();
        }]);
