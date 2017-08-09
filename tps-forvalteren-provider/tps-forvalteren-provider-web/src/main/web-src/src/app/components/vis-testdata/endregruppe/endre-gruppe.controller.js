angular.module('tps-forvalteren.vis-testdata.endregruppe', ['ngMaterial'])
    .controller('EndreGruppeCtrl', ['$scope', '$mdDialog', 'testdataService', '$location', 'headerService', 'utilsService',
        function ($scope, $mdDialog, testdataService, $location, headerService, utilsService) {

            var gruppeId = $location.url().match(/\d+/g);

            var testgrupper = [];
            testdataService.hentTestgrupper().then(
                function (result) {
                    testgrupper = result.data;
                    for (var i = 0; i < testgrupper.length; i++) {
                        if (testgrupper[i].id == gruppeId) {
                            $scope.gruppe = testgrupper[i];
                            $scope.gmlTittel = $scope.gruppe.navn;
                        }
                    }
                },
                function (error) {
                    utilsService.showAlertError(error);
                }
            );

            $scope.checkNavn = function () {
                var match = undefined;
                for (var i = 0; i < testgrupper.length; i++) {
                    if ($scope.gruppe.navn.toLowerCase().trim() === testgrupper[i].navn.toLowerCase().trim() &&
                        testgrupper[i].id != gruppeId) {
                        match = true;
                        break;
                    }
                } // Navn eksisterer allerede
                $scope.gruppeForm2.navn.$error.validationError = match;
                $scope.gruppeForm2.navn.$invalid = !!match;
                if (match) {
                    $scope.gruppeForm2.$invalid = true;
                }
            };

            $scope.avbryt = function () {
                $mdDialog.hide();
            };
            $scope.oppdater = function () {
                testdataService.lagreTestgruppe($scope.gruppe).then(
                    function () {
                        $mdDialog.hide();
                        headerService.setHeader($scope.gruppe.navn, true);
                    },
                    function (error) {
                        $mdDialog.hide();
                        utilsService.showAlertError(error);
                    }
                );
            };
        }]);