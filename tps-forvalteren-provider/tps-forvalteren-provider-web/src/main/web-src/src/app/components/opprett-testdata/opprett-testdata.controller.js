angular.module('tps-forvalteren.opprett-testdata')
    .controller('OpprettTestdataCtrl', ['$scope', 'testdataService', 'utilsService', '$mdDialog', 'locationService', '$filter',
        function ($scope, testdataService, utilsService, $mdDialog, locationService, $filter) {

            $scope.kriterier = [];
            $scope.kriterium = {};
            $scope.startOfEra = new Date(1900,0,1); // Month is 0-indexed
            $scope.today = new Date();
            $scope.editMode = true;
            $scope.showSpinner = false;

            var cleanupRow = function () {
                $scope.kriterium = {identtype: "FNR", kjonn: " ", foedtEtter: undefined, foedtFoer: undefined, antall: undefined};
                $scope.foedtEtterMax = $scope.startOfEra;
                $scope.foedtFoerMin = $scope.today;
            };

            $scope.foedtEtterChanged = function () {
                $scope.foedtEtterMax = $scope.kriterium.foedtEtter ? $scope.kriterium.foedtEtter : $scope.startOfEra;
            };

            $scope.foedtFoerChanged = function () {
                $scope.foedtFoerMin = $scope.kriterium.foedtFoer ? $scope.kriterium.foedtFoer : $scope.today;
            };

            $scope.opprettTestpersoner = function () {
                $scope.editMode = false;
                $scope.showSpinner = true;
                testdataService.opprettTestpersoner($scope.kriterier).then(
                    function (result) {
                        $scope.showSpinner = false;
                        opprettComplete();
                    },
                    function (error) {
                        $scope.editMode = true;
                        $scope.showSpinner = false;
                        utilsService.showAlertError(error);
                    }
                );
            };

            $scope.addKriterium = function() {
                $scope.kriterier.push($scope.kriterium);
                cleanupRow();
            };

            var opprettComplete = function () {
                var confirm = $mdDialog.confirm()
                    .title('Bekreftelse')
                    .textContent('Oppretting av testpersoner er fullført!')
                    .ariaLabel('Bekrefter oppretting av testpersoner')
                    .ok('OK');

                $mdDialog.show(confirm).then(function() {
                    locationService.redirectToVisTestdata();
                });
            };

            $scope.removeDialog = function(index) {
                var krit = $scope.kriterier[index];
                var confirm = $mdDialog.confirm()
                    .title('Bekreft sletting av følgende rad:')
                    .textContent(getRow(krit))
                    .ariaLabel('Bekreft sletting')
                    .ok('OK')
                    .cancel('Avbryt');

                $mdDialog.show(confirm).then(function() {
                    $scope.kriterier.splice(index, 1);
                });
            };

            $scope.avbryt = function () {
                if ($scope.kriterier.length > 0) {
                    var contents = '';
                    for (var i = 0; i < $scope.kriterier.length; i++) {
                        contents += getRow($scope.kriterier[i]) + ' / ';
                    }
                    var confirm = $mdDialog.confirm()
                        .title('Bekreft avbryt av følgende rad' + ($scope.kriterier.length > 1 ? 'er' : '') + ':')
                        .textContent(contents)
                        .ariaLabel('Bekreft avbryt')
                        .ok('OK')
                        .cancel('Avbryt');

                    $mdDialog.show(confirm).then(function() {
                        locationService.redirectToVisTestdata();
                    });
                } else {
                    locationService.redirectToVisTestdata();
                }
            };

            var getRow = function (krit) {
                return 'Type = "' + krit.identtype + '"' +
                    (krit.kjonn != ' ' ? ', Kjonn = "' + krit.kjonn + '"' : '') +
                    (krit.foedtEtter ? ', Født etter =" ' + $filter('date')(krit.foedtEtter, 'dd-MM-yyyy') + '"' : '') +
                    (krit.foedtFoer ? ', Født før = "' + $filter('date')(krit.foedtFoer, 'dd-MM-yyyy') + '"' : '') +
                    (krit.antall ? ', Antall = "' + krit.antall + '"' : '');
            };

            // Prep for unbiased row
            cleanupRow();
        }]);
