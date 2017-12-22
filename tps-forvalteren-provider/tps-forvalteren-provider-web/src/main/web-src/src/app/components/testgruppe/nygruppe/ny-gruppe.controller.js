angular.module('tps-forvalteren.testgruppe.nygruppe', ['ngMaterial'])
    .controller('NyGruppeCtrl', ['$scope', '$mdDialog', 'testdataService', 'utilsService',
        function ($scope, $mdDialog, testdataService, utilsService) {

            var testgrupper = [];
            testdataService.hentTestgrupper().then(
                function (result) {
                    testgrupper = result.data;
                },
                function (error) {
                }
            );

            $scope.checkNavn = function () {
                var match = undefined;
                for (var i = 0; i < testgrupper.length; i++) {
                    if ($scope.gruppe.navn.toLowerCase().trim() == testgrupper[i].navn.toLowerCase().trim()) {
                        match = true;
                        break;
                    }
                }
                $scope.gruppeForm.navn.$error.validationError = match;
                $scope.gruppeForm.navn.$invalid = !!match;
                if (match) {
                    $scope.gruppeForm.$invalid = true;
                }
            };

            $scope.avbryt = function () {
                $mdDialog.hide();
            };
            $scope.lagre = function () {
                testdataService.lagreTestgruppe($scope.gruppe).then(
                    function () {
                        $mdDialog.hide();
                    },
                    function (error) {
                        $mdDialog.hide();
                        utilsService.showAlertError(error);
                    }
                );
            };
        }]);