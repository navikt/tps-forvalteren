angular.module('tps-forvalteren.vis-testdata')
    .controller('VisTestdataCtrl', ['$scope', 'testdataService', 'utilsService', 'locationService', '$mdDialog',
        function ($scope, testdataService, utilsService, locationService, $mdDialog) {

            $scope.showDelete = false;
            $scope.allePersoner = false;
            $scope.personer = [];

            $scope.velgAlle = function () {
                for (var i = 0; i < $scope.personer.length; i++) {
                    $scope.personer[i].velg = !$scope.allePersoner;
                }
                $scope.showDelete = !$scope.allePersoner;
            };

            var hentTestpersoner = function () {
                $scope.personer = undefined;
                testdataService.getTestpersoner().then(
                    function (result) {
                        $scope.personer = result.data;
                    },
                    function (error) {
                        utilsService.showAlertError(error);
                    }
                );
            }();

            $scope.isEditing = false;
            var currentIndex = undefined;
            var cancel = undefined;

            $scope.enableEditing = function (index) {
                if (!$scope.isEditing) {
                    $scope.selected = index;
                    currentIndex = index;
                    $scope.isEditing = true;
                } else if (cancel && index == currentIndex) {
                    $scope.isEditing = false;
                    cancel = false;
                }
            };

            $scope.cancel = function () {
                if ($scope.isEditing) {
                    cancel = true;
                }
            };

            $scope.opprettPersoner = function () {
                locationService.redirectToOpprettTestdata();
            };

            $scope.removeDialog = function(index) {

                var confirm = $mdDialog.confirm()
                    .title('Bekreft sletting')
                    .textContent('Bekreft sletting av valgte personer')
                    .ariaLabel('Bekreft sletting')
                    .ok('OK')
                    .cancel('Avbryt');

                $mdDialog.show(confirm).then(function() {
                    deleteTestpersoner();
                });
            };

            var removeConfirmDialog = function(index) {
                var confirm = $mdDialog.dialog()
                    .title('Sletting utført')
                    .textContent('Sletting har blitt utført!')
                    .ariaLabel('Bekrefter sletting utført')
                    .ok('OK');

                $mdDialog.show(confirm).then(function() {
                    hentTestpersoner();
                });
            };

            $scope.oppdaterStatus = function() {
                var showAll = true;
                var isShown = false;
                for (var i = 0; i < $scope.personer.length; i++) {
                    if ($scope.personer[i].velg) {
                        isShown = true;
                    } else {
                        showAll = false;
                    }
                }
                $scope.showDelete = isShown;
                $scope.allePersoner = showAll;
            };

            var deleteTestpersoner = function () {
                var identer = [];
                for (var i = 0; i < $scope.personer.length; i++) {
                    identer.push($scope.personer[i].ident);
                }
                testdataService.deleteTestpersoner(identer).then(
                    function (result) {
                        removeConfirmDialog();
                    },
                    function (error) {
                        utilsService.showAlertError(error);
                    }
                );
            };
        }]);
