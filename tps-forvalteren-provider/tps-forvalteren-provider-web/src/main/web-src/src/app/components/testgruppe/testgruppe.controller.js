angular.module('tps-forvalteren.testgruppe', [])
    .controller('TestgruppeCtrl', ['$scope', 'headerService', 'testdataService', 'utilsService', 'locationService', '$mdDialog',
        function ($scope, headerService, testdataService, utilsService, locationService, $mdDialog) {

            headerService.setHeader('Testdata');

            var nyGruppeDialog = function (ev) {
                var confirm = $mdDialog.confirm({
                    controller: 'NyGruppeCtrl',
                    templateUrl: 'app/components/testgruppe/nygruppe/nygruppe.html',
                    parent: angular.element(document.body),
                    targetEvent: ev
                });
                $mdDialog.show(confirm).then(function () {
                    hentTestgrupper();
                });
            };

            headerService.setButtons([{
                text: 'Ny gruppe',
                icon: 'assets/icons/ic_add_circle_outline_black_24px.svg',
                click: nyGruppeDialog
            }]);

            $scope.testgrupper = [];

            $scope.aapneFane = function (index) {
                locationService.redirectToVisTestdata(index);
            };

            var hentTestgrupper = function () {
                testdataService.hentTestgrupper().then(
                    function (result) {
                        $scope.testgrupper = result.data;
                    },
                    function (error) {
                        utilsService.showAlertError(error);
                    }
                );
            };

            hentTestgrupper();
        }]);