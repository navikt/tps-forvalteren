angular.module('tps-forvalteren.opprett-testdata', ['ngMessages'])
    .controller('OpprettTestdataCtrl', ['$scope', '$stateParams', 'testdataService', 'utilsService', '$mdDialog', 'locationService', '$filter', 'headerService',
        function ($scope, $stateParams, testdataService, utilsService, $mdDialog, locationService, $filter, headerService) {

            var gruppeId = $stateParams.gruppeId;

            headerService.setHeader('Legg til testpersoner');

            $scope.kriterier = [];
            $scope.kriterium = {};
            $scope.startOfEra = new Date(1900, 0, 1); // Month is 0-indexed
            $scope.today = new Date();
            $scope.editMode = true;
            $scope.showSpinner = false;
            $scope.antallLedig = 0;

            function cleanupRow() {
                $scope.kriterium = {identtype: "FNR", kjonn: undefined, foedtEtter: null, foedtFoer: null, antall: undefined};
                $scope.foedtEtterMax = $scope.startOfEra;
                $scope.foedtFoerMin = $scope.today;
            }

            $scope.foedtEtterChanged = function () {
                $scope.foedtEtterMax = $scope.kriterium.foedtEtter ? $scope.kriterium.foedtEtter : $scope.startOfEra;
            };

            $scope.foedtFoerChanged = function () {
                $scope.foedtFoerMin = $scope.kriterium.foedtFoer ? $scope.kriterium.foedtFoer : $scope.today;
            };

            function fixTimezone(date) {
                if (date && date.toString().length > 19) {
                    date.setMinutes(date.getTimezoneOffset() * -1);
                }
            }

            function fixDates() {
                $scope.kriterier.forEach(function (kriterium) {
                    fixTimezone(kriterium.foedtFoer);
                    fixTimezone(kriterium.foedtEtter);
                });
            }

            $scope.opprettTestpersoner = function () {
                $scope.editMode = false;
                $scope.showSpinner = true;

                fixDates();
                var adresseinfo = $scope.adresseNrInfo != null && $scope.adresseNrInfo.nummertype == "Uspesifisert" ? null : $scope.adresseNrInfo;
                testdataService.opprettTestpersoner(gruppeId, $scope.kriterier, adresseinfo).then(
                    function (result) {
                        $scope.showSpinner = false;
                        opprettComplete();
                    },
                    function (error) {
                        $scope.editMode = true;
                        $scope.showSpinner = false;
                        if ("Ingen adresser funnet" == error.data.message) {
                            utilsService.showAlertDialog("S051 TPS Info: Ingen adresser funnet", "Finn gyldig adresse");
                        } else {
                            utilsService.showAlertError(error);
                        }
                    }
                );
            };

            $scope.addKriterium = function () {
                var count = 0;
                $scope.kriterier.forEach(function (kriterium) {
                    count += Number(kriterium.antall);
                });
                if (count + Number($scope.kriterium.antall) <= 10000) {
                    $scope.kriterier.push($scope.kriterium);
                    cleanupRow();
                } else {
                    var confirm = $mdDialog.alert()
                        .title('Begrensning')
                        .textContent('Maks 10 000 testpersoner kan opprettes av gangen!')
                        .ariaLabel('Maks 10 000 testpersoner kan opperettes per gang.')
                        .ok('OK');
                    $mdDialog.show(confirm).then(function () {
                        // Empty function
                    });
                }
            };

            function opprettComplete() {
                var confirm = $mdDialog.alert()
                    .title('Bekreftelse')
                    .textContent('Oppretting av testpersoner er fullført!')
                    .ariaLabel('Bekrefter oppretting av testpersoner')
                    .ok('OK');

                $mdDialog.show(confirm).then(function () {
                    locationService.redirectToVisTestdata(gruppeId);
                });
            }

            $scope.removeDialog = function (index) {
                var krit = $scope.kriterier[index];
                var confirm = $mdDialog.confirm()
                    .title('Bekreft sletting av følgende rad:')
                    .textContent(getRow(krit))
                    .ariaLabel('Bekreft sletting')
                    .ok('OK')
                    .cancel('Avbryt');

                $mdDialog.show(confirm).then(function () {
                    $scope.kriterier.splice(index, 1);
                });
            };

            $scope.avbryt = function () {
                if ($scope.kriterier.length > 0) {
                    var contents = '';
                    for (var i = 0; i < $scope.kriterier.length; i++) {
                        contents += getRow($scope.kriterier[i]) + '<br>';
                    }
                    var confirm = $mdDialog.confirm()
                        .title('Bekreft avbryt av følgende rad' + ($scope.kriterier.length > 1 ? 'er' : '') + ':')
                        .htmlContent(contents)
                        .ariaLabel('Bekreft avbryt')
                        .ok('OK')
                        .cancel('Avbryt');

                    $mdDialog.show(confirm).then(function () {
                        locationService.redirectToVisTestdata(gruppeId);
                    });
                } else {
                    locationService.redirectToVisTestdata(gruppeId);
                }
            };

            function getRow(kriterium) {
                return 'Type = "' + kriterium.identtype + '"' +
                    (kriterium.kjonn ? ', Kjonn = "' + kriterium.kjonn + '"' : '') +
                    (kriterium.foedtEtter ? ', Født etter =" ' + $filter('date')(kriterium.foedtEtter, 'dd-MM-yyyy') + '"' : '') +
                    (kriterium.foedtFoer ? ', Født før = "' + $filter('date')(kriterium.foedtFoer, 'dd-MM-yyyy') + '"' : '') +
                    (kriterium.antall ? ', Antall = "' + kriterium.antall + '"' : '');
            }

            $scope.sjekkIdenter = function () {
                if ($scope.identRaw) {
                    $scope.showSjekkSpinner = true;
                    var identer = $scope.identRaw.split(/[\W\s]+/);
                    identer = identer.sort().filter(function (elem, index, self) {
                        return index === self.indexOf(elem);
                    });
                    $scope.identRaw = identer.join('\n');
                    testdataService.validerListe(identer).then(
                        function (result) {
                            $scope.showSjekkSpinner = false;
                            $scope.kandidater = result.data;
                            antallGyldig();
                        },
                        function (error) {
                            $scope.showSjekkSpinner = false;
                            utilsService.showAlertError(error);
                        });
                }
                ;
            };

            $scope.slettRadDialog = function (ident) {
                for (var i = 0; i < $scope.kandidater.length; i++) {
                    if (($scope.kandidater[i].ident === ident)) {
                        $scope.kandidater.splice(i, 1);
                        break;
                    }
                }
                antallGyldig();
            };

            $scope.avbrytFraIdentliste = function () {
                if ($scope.antallLedig > 0) {
                    var confirm = $mdDialog.confirm()
                        .title('Oppretting av ' + $scope.antallLedig + ' person' + ($scope.antallLedig > 1 ? 'er' : '') + ' har ikke blitt fullført')
                        .textContent('Bekreft avbryt!')
                        .ariaLabel('Bekreft avbryt oppretting av person fra liste')
                        .ok('OK')
                        .cancel('Avbryt');
                    $mdDialog.show(confirm).then(function () {
                        locationService.redirectToVisTestdata(gruppeId);
                    });
                } else {
                    locationService.redirectToVisTestdata(gruppeId);
                }
            };

            function antallGyldig() {
                var antall = 0;
                for (var i = 0; i < $scope.kandidater.length; i++) {
                    if ($scope.kandidater[i].status === 'LOG') {
                        antall++;
                    }
                }
                $scope.antallLedig = antall;
            }

            $scope.opprettFraIdentliste = function () {
                $scope.showOpprettSpinner = true;
                var identer = [];
                for (var i = 0; i < $scope.kandidater.length; i++) {
                    if ($scope.kandidater[i].status === 'LOG') {
                        identer.push($scope.kandidater[i].ident);
                    }
                }
                testdataService.opprettFraListe(gruppeId, identer).then(
                    function (result) {
                        $scope.showOpprettSpinner = false;
                        opprettComplete();
                    },
                    function (error) {
                        $scope.showOpprettSpinner = false;
                        utilsService.showAlertError(error);
                    });
            };

            // Prep for unbiased row
            cleanupRow();

        }]);
