angular.module('tps-forvalteren.gt')
    .controller('OpprettTestdataCtrl', ['$scope', 'testdataService', 'utilsService',
        function ($scope, testdataService, utilsService) {

            $scope.allePersoner = false;

            $scope.velgAlle = function () {
                for (var i = 0; i < $scope.personer.length; i++) {
                    $scope.personer[i].velg = !$scope.allePersoner;
                }
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
            }
        }]);
