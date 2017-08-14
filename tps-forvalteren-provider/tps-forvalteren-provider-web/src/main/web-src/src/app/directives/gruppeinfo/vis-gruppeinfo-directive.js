angular.module('tps-forvalteren.directives')
    .directive('visGruppeinfo', function () {
        return {
            restrict: 'E',
            replace: true,
            scope: {
                gruppeId: '='
            },
            templateUrl: 'app/directives/gruppeinfo/vis-gruppeinfo.html',
            controller: ["$scope", 'testdataService', 'utilsService', function ($scope, testdataService, utilsService) {

                function hentGruppe () {
                    testdataService.getTestpersoner($scope.gruppeId).then(
                        function (result) {
                            $scope.gruppe = result.data;
                        },
                        function (error) {
                            utilsService.showAlertError(error);
                        }
                    );
                }

                $scope.$on('gruppeEvent', function (event, arg) {
                    hentGruppe();
                });

                hentGruppe();
            }]
        };
    });
