angular.module('tps-forvalteren.gt')
    .controller('TestdataCtrl', ['$scope', 'testdataService', 'utilsService',
        function ($scope, testdataService, utilsService) {

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
                        // Temp dummy data
                        $scope.personer = [{ident: "32345678901", identtype: "DNR", fornavn: "Elenora", mellomnavn: "Susan", etternavn: "Dixon", kjonn: "K"},
                            {ident: "15059023541", identtype: "FNR", fornavn: "Frank", mellomnavn: "Erland", etternavn: "Aaserud", kjonn: "M"}];
                    }
                );
            }();

        }]);
