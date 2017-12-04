angular.module('tps-forvalteren.directives')
    .directive('paginering', [function () {
        return {
            restrict: 'E',
            scope: {
                contents: '=',
                slice: '=',
                pager: '=',
                pageSize: '='
            },
            templateUrl: 'app/directives/paginering/paginering.html',
            controller: ['$scope', '$timeout', 'pagerService', function ($scope, $timeout, pagerService) {

                $scope.pageLen = $scope.pageSize || 20;
                $scope.pager = {};

                $scope.$watch('contents', function (newVal) {
                    if (newVal) {
                        $scope.setPage(1);
                    }
                });

                $scope.setPage = function (page) {
                    if (page < 1 || page > $scope.pager.totalPages) {
                        return;
                    }

                    // get pager object from service
                    $scope.pager = pagerService.getPager($scope.contents.length, page, $scope.pageLen);

                    $scope.slice = $scope.contents.slice($scope.pager.startIndex, $scope.pager.endIndex + 1);
                }
            }]
        };
    }]);