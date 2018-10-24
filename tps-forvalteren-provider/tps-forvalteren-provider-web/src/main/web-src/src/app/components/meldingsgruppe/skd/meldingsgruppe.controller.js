angular.module('tps-forvalteren.skd-meldingsgruppe', [])
    .controller('SkdMeldingsgruppeCtrl', ['$scope', 'headerService', 'endringsmeldingService', 'utilsService', 'locationService', '$mdDialog',
        function ($scope, headerService, endringsmeldingService, utilsService, locationService, $mdDialog) {

            headerService.setHeader('TPS Endringsmeldinger');

            var nyGruppeDialog = function (ev) {
                var confirm = $mdDialog.confirm({
                    controller: 'NySkdMeldingsgruppeCtrl',
                    templateUrl: 'app/components/meldingsgruppe/skd/nygruppe/ny-gruppe.html',
                    parent: angular.element(document.body),
                    targetEvent: ev
                });
                $mdDialog.show(confirm).then(function () {
                    getSkdMeldingsgrupper();
                });
            };

            headerService.setButtons([{
                text: 'Ny gruppe',
                icon: 'assets/icons/ic_add_circle_outline_black_24px.svg',
                click: nyGruppeDialog
            }]);

            $scope.meldingsgrupper = [];

            $scope.aapneFane = function (index) {
                locationService.redirectToOpprettSkdMeldinger(index);
            };

            var getSkdMeldingsgrupper = function () {
                endringsmeldingService.getSkdMeldingsgrupper().then(
                    function (result) {
                        $scope.meldingsgrupper = result.data;
                    },
                    function (error) {
                        utilsService.showAlertError(error, '0000-GA-TPSF-SKDMELDING');
                    }
                );
            };

            getSkdMeldingsgrupper();
        }]);