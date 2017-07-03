angular.module('tps-forvalteren.testgruppe')
    .controller('TestgruppeCtrl', ['$scope', 'headerService', 'testdataService', 'utilsService', 'locationService',
        function ($scope, headerService, testdataService, utilsService, locationService) {

        headerService.setHeader('Testdata');

        $scope.testgrupper = [];

        $scope.aapneFane = function (index) {
            locationService.redirectToVisTestdata(index);
        };

        testdataService.hentTestgrupper().then(
            function(result) {
                $scope.testgrupper = result.data;
                if ($scope.testgrupper.length == 0) {
                    testdataService.tempdata().then(
                        function (result) {
                            $scope.testgrupper = result.data;
                    },
                    function (error) {
                        utilsService.showAlertError(error);
                    });
                }
            },
            function (error) {
                utilsService.showAlertError(error);
            }
        );
    }]);