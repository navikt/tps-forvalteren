angular.module('tps-forvalteren.skd-vis-meldingsgruppe.endregruppe', ['ngMaterial'])
    .controller('EndreSkdGruppeCtrl', ['$scope', '$mdDialog', '$rootScope', '$stateParams', 'endringsmeldingService', 'headerService', 'utilsService',
        function ($scope, $mdDialog, $rootScope, $stateParams, endringsmeldingService, headerService, utilsService) {

            var gruppeId = $stateParams.gruppeId;
            var grupper = [];
            endringsmeldingService.getSkdMeldingsgrupper().then(
                function (result) {
                    grupper = result.data;
                    for (var i = 0; i < grupper.length; i++) {
                        if (grupper[i].id == gruppeId) {
                            $scope.gruppe = grupper[i];
                            $scope.gmlTittel = $scope.gruppe.navn;
                        }
                    }
                },
                function (error) {
                }
            );

            $scope.checkNavn = function () {
                var match = undefined;
                for (var i = 0; i < grupper.length; i++) {
                    if ($scope.gruppe.navn.toLowerCase().trim() === grupper[i].navn.toLowerCase().trim() &&
                        grupper[i].id != gruppeId) {
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
                endringsmeldingService.storeSkdMeldingsgruppe($scope.gruppe).then(
                    function () {
                        $mdDialog.hide();
                        headerService.setHeader($scope.gruppe.navn, true);
                        $rootScope.$broadcast('gruppeEvent', 'Gruppe er oppdatert');
                    },
                    function (error) {
                        $mdDialog.hide();
                        utilsService.showAlertError(error);
                    }
                );
            };
        }]);