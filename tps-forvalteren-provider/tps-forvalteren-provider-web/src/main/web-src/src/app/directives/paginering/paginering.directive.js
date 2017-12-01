angular.module('tps-forvalteren.directives')
    .directive('paginering', [function () {
        return {
            restrict: 'E',
            scope: {
                length: '=',
                pager: '='
            },
            templateUrl: 'app/directives/paginering/paginering.html',
            controller: ['$scope', '$timeout', 'pagerService', function ($scope, $timeout, pagerService) {

                $scope.pageLen = 20;
                $scope.pager = {};

                $scope.$watch('length', function (newVal) {
                    if (newVal) {
                        $scope.setPage(1);
                    }
                });

                $scope.setPage = function (page) {
                    if (page < 1 || page > $scope.pager.totalPages) {
                        return;
                    }

                    // get pager object from service
                    $scope.pager = pagerService.getPager($scope.length, page, $scope.pageLen);
                }
            }]
        };
    }]);