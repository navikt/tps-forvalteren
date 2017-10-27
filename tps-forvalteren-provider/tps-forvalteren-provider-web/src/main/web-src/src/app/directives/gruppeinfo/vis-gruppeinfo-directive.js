angular.module('tps-forvalteren.directives')
    .directive('visGruppeinfo', function () {
        return {
            restrict: 'E',
            replace: true,
            scope: {
                gruppeId: '=',
                service: '='
            },
            templateUrl: 'app/directives/gruppeinfo/vis-gruppeinfo.html',
            controller: ["$scope", 'utilsService', function ($scope, utilsService) {

                function hentGruppe () {
                    $scope.service.getGruppe($scope.gruppeId).then(
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
