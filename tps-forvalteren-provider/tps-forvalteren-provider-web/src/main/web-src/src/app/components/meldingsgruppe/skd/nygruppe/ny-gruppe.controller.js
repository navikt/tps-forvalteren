angular.module('tps-forvalteren.skd-meldingsgruppe.nygruppe', ['ngMaterial'])
    .controller('NySkdMeldingsgruppeCtrl', ['$scope', '$mdDialog', 'endringsmeldingService', 'utilsService',
        function ($scope, $mdDialog, endringsmeldingService, utilsService) {

            var meldingsgrupper = [];
            endringsmeldingService.getSkdMeldingsgrupper().then(
                function (result) {
                    meldingsgrupper = result.data;
                },
                function (error) {
                }
            );

            $scope.checkNavn = function () {
                var match = undefined;
                for (var i = 0; i < meldingsgrupper.length; i++) {
                    if ($scope.gruppe.navn.toLowerCase().trim() === meldingsgrupper[i].navn.toLowerCase().trim()) {
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
                endringsmeldingService.storeSkdMeldingsgruppe($scope.gruppe).then(
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