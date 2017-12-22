angular.module('tps-forvalteren.directives')
    .directive('kommunenr', [function () {
        return {
            restrict: 'E',
            scope: {
                kommunenr: '=',
                endretFn: '=',
                index: '=',
                paakrevet: '='
            },
            templateUrl: 'app/directives/kommunenr/kommunenr.html',
            controller: ["$scope", 'kodeverkService', 'utilsService', '$timeout', function ($scope, kodeverkService, utilsService, $timeout) {

                var kommuner = [];

                function hentKommuner() {
                    kodeverkService.hentKommuner().then(
                        function (result) {
                            for (var index in result.data) {
                                var kommune = {};
                                kommune.navn = result.data[index].term.toUpperCase();
                                kommune.nummer = result.data[index].navn;
                                kommune.visningsnavn = result.data[index].navn + '  ' + result.data[index].term.toUpperCase();
                                kommuner[kommune.nummer] = kommune;
                            }
                            $scope.kommune = kommuner[$scope.kommunenr];
                        },
                        function (error) {
                            utilsService.showAlertError(error);
                        }
                    );
                }

                $scope.getKommuneMatches = function (searchText) {
                    var result = [];
                    var searchTextUpperCase = searchText.toUpperCase();
                    for (var index in kommuner) {
                        if (kommuner[index].visningsnavn.indexOf(searchTextUpperCase) !== -1) {
                            result.push(kommuner[index]);
                        }
                    }
                    return result;
                };

                $scope.endring = function() {
                    $scope.kommunenr = $scope.kommune ? $scope.kommune.nummer : undefined;
                    $timeout(function() {
                        $scope.endretFn($scope.index);
                    });
                };

                hentKommuner();
            }]
        };
    }]);