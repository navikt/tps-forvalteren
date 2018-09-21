angular.module('tps-forvalteren.directives')
    .directive('postnummer', [function () {
        return {
            restrict: 'E',
            scope: {
                postnummer: '=',
                endretFn: '=',
                index: '=',
                paakrevet: '='
            },
            templateUrl: 'app/directives/postnummer/postnummer.html',
            controller: ['$scope', 'kodeverkService', 'utilsService', '$timeout', function ($scope, kodeverkService, utilsService, $timeout) {

                var poststeder = [];

                function hentPoststeder() {
                    kodeverkService.hentPoststeder().then(
                        function (result) {
                            for (var index in result.data) {
                                var poststed = {};
                                poststed.navn = result.data[index].term.toUpperCase();
                                poststed.nummer = result.data[index].navn;
                                poststed.visningsnavn = poststed.nummer + '  ' + poststed.navn;
                                poststeder[poststed.nummer] = poststed;
                            }
                            $scope.poststed = poststeder[$scope.postnummer];
                        },
                        function (error) {
                            utilsService.showAlertError(error);
                        }
                    );
                }

                $scope.getPostnummerMatches = function (searchText) {
                    var result = [];
                    var searchTextUpperCase = searchText.toUpperCase();
                    for (var index in poststeder) {
                        if (poststeder[index].visningsnavn.indexOf(searchTextUpperCase) !== -1) {
                            result.push(poststeder[index]);
                        }
                    }
                    return result;
                };

                $scope.endring = function () {
                    $scope.postnummer = $scope.poststed ? $scope.poststed.nummer : undefined;
                    $timeout(function () {
                        $scope.endretFn($scope.index);
                    });
                };

                hentPoststeder();
            }]
        };
    }]);