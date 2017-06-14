angular.module('tps-forvalteren.vis-testdata')
    .controller('VisTestdataCtrl', ['$scope', 'testdataService', 'utilsService', 'locationService', '$mdDialog',
        function ($scope, testdataService, utilsService, locationService, $mdDialog) {

            $scope.allePersoner = false;
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
                        $scope.control[i].velg = !$scope.allePersoner;
                        enabled++;
                    }
                }
                $scope.antallValgt = !$scope.allePersoner ? enabled : 0;
                oppdaterFunksjonsknapper();
            };

            var hentTestpersoner = function () {
                $scope.personer = undefined;
                testdataService.getTestpersoner().then(
                    function (result) {
                        originalPersoner = result.data;
                        $scope.personer = angular.copy(originalPersoner);
                        $scope.control = [];
                        $scope.antallEndret = 0;
                        $scope.antallValgt = 0;
                        oppdaterFunksjonsknapper();
                    },
                    function (error) {
                        utilsService.showAlertError(error);
                    }
                );
            };

            var lukkingPaagaar = undefined;
            var oppdaterFane = undefined;

            $scope.aapneFane = function (index) {
                if (lukkingPaagaar || oppdaterFane) {
                    lukkingPaagaar = false;
                    oppdaterFane = false;
                } else {
                    if (!$scope.control[index]) {
                        $scope.control[index] = {};
                    }
                    $scope.control[index].aapen = true;
                }
            };

            $scope.lukkFane = function (index) {
                if ($scope.control[index].aapen) {
                    $scope.control[index].aapen = undefined;
                    lukkingPaagaar = true;
                } else {
                    $scope.control[index].aapen = true;
                }
            };

            $scope.opprettPersoner = function () {
                locationService.redirectToOpprettTestdata();
            };

            $scope.sletteDialog = function(index) {
                var confirm = $mdDialog.confirm()
                    .title('Bekreft sletting')
                    .textContent('Bekreft sletting av valgte personer')
                    .ariaLabel('Bekreft sletting')
                    .ok('OK')
                    .cancel('Avbryt');

                $mdDialog.show(confirm).then(function() {
                    sletteTestpersoner();
                });
            };

            $scope.oppdaterValgt = function() {
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
                $scope.allePersoner = (endret == 0 && $scope.personer.length == valgt) ||
                    (endret > 0 && endret == valgt);
                $scope.antallEndret = endret;
                $scope.antallValgt = valgt;
                $scope.visEndret = endret > 0;
            };

            var sletteTestpersoner = function () {
                var identer = [];
                for (var i = 0; i < $scope.personer.length; i++) {
                    if ($scope.control[i].velg) {
                        identer.push($scope.personer[i].id);
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

            $scope.getKjonn = function( kjonn ) {
                if (kjonn) {
                    return kjonn == 'K' ? 'Kvinne' : 'Mann';
                } else {
                    return '';
                }
            };

            $scope.lagre = function () {
                var buffer = [];
                for (var i = 0; i < $scope.personer.length; i++) {
                    if ($scope.control[i] && $scope.control[i].velg) {
                        buffer.push($scope.personer[i]);
                    }
                }
                testdataService.oppdaterTestpersoner(buffer).then(
                    function (result) {
                        for (var i = 0; i < $scope.personer.length; i++) {
                            if ($scope.control[i] && $scope.control[i].velg) {
                                nullstillControl(i);
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

            var oppdaterFunksjonsknapper = function() {
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
                var originalPerson = JSON.stringify(originalPersoner[index]).replace(/null/g,'""') // Angular legger på $$hashKey, fjerner den
                    .replace(/,*"[A-Za-z0-9_]+":""/g,'')
                    .replace(/{}/g,'');
                var endretPerson = JSON.stringify($scope.personer[index]).replace(/null/g,'""')
                    .replace(/,*"\$\$hashKey":"[A-Za-z0-9_:]+"/g,'')
                    .replace(/,*"[A-Za-z0-9_]+":""/g,'')
                    .replace(/{}/g,'');

                $scope.control[index].endret = originalPerson != endretPerson;
                $scope.control[index].velg = $scope.control[index].endret;
                $scope.oppdaterValgt();
            };

            var avbrytLagring = function () {
                for (var i = 0; i < $scope.personer.length; i++) {
                    if ($scope.control[i] && $scope.control[i].velg) {
                        $scope.personer[i]= JSON.parse(JSON.stringify(originalPersoner[i]));
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

            var bekrefterLagring = function(index) {
                var confirm = $mdDialog.confirm()
                    .title('Bekrefter lagring')
                    .textContent('Lagring er utført')
                    .ariaLabel('Bekrefter lagring')
                    .ok('OK')

                $mdDialog.show(confirm).then(function() {
                });
            };

            $scope.avbryteDialog = function(index) {
                var confirm = $mdDialog.confirm()
                    .title('Bekreft avbryt endring')
                    .textContent('Endringer som er gjort vil gå tapt')
                    .ariaLabel('Bekreft avbryt')
                    .ok('OK')
                    .cancel('Avbryt');

                $mdDialog.show(confirm).then(function() {
                    avbrytLagring();
                });
            };

            $scope.spesregChanged = function (index) {
                if (personer[index].spesreg == ' ') {
                    personer[index].spesreg = '';
                }
                endret(index);
            };

            hentTestpersoner();
        }]);
