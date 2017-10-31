angular.module('tps-forvalteren.skd-vis-meldingsgruppe.nymelding', ['ngMaterial'])
    .controller('SkdNyMeldingCtrl', ['$scope', '$mdDialog', '$rootScope', '$stateParams', 'endringsmeldingService', 'locationService', 'utilsService',
        function ($scope, $mdDialog, $rootScope, $stateParams, endringsmeldingService, locationService, utilsService) {

            var gruppeId = $stateParams.gruppeId;
            $scope.melding = {};

            $scope.rawCheck = function () {
                $scope.gruppeForm2.rawMelding.$error.validationError = $scope.melding.raw && $scope.melding.raw.length % 1500;
            };

            $scope.avbryt = function () {
                $mdDialog.hide();
            };

            $scope.selectTab = function (tabNo) {
                $scope.selectedTab = tabNo;
            };

            $scope.createMelding = function () {
                if ($scope.selectedTab === 1) {
                    $scope.melding.raw = undefined;
                } else {
                    $scope.melding.meldingstype = undefined;
                    $scope.melding.navn = undefined;
                }
                endringsmeldingService.createMelding(gruppeId, $scope.melding).then(
                    function () {
                        $mdDialog.hide();
                        locationService.redirectToOpprettSkdMeldinger(gruppeId);
                    },
                    function (error) {
                        $mdDialog.hide();
                        utilsService.showAlertError(error);
                    }
                );
            };
        }]);