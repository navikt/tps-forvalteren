angular.module('tps-forvalteren.directives')
    .directive('statsborgerskap', [function () {
        return {
            restrict: 'E',
            scope: {
                landkoden: '=',
                endretFn: '=',
                index: '='
                
            },
            templateUrl: 'app/directives/statsborgerskap/statsborgerskap.html',
            controller: ["$scope", 'kodeverkService', 'utilsService', '$timeout', function ($scope, kodeverkService, utilsService, $timeout) {

                var landkoder = [];

                function hentLandkoder() {
                    kodeverkService.hentLandkoder().then(
                        function (result) {
                            for (var index in result.data) {
                                var landkode = {};
                                landkode.land = result.data[index].term.toUpperCase();
                                landkode.kode = result.data[index].navn;
                                landkode.visningsnavn = result.data[index].navn + '  ' + result.data[index].term.toUpperCase();
                                landkoder[landkode.kode] = landkode;
                            }
                            $scope.landkode = landkoder[$scope.landkoden];
                        },
                        function (error) {
                            utilsService.showAlertError(error);
                        }
                    );
                }

                $scope.getLandkodeMatches = function (searchText) {
                    var result = [];
                    var searchTextUpperCase = searchText.toUpperCase();
                    for (var index in landkoder) {
                        if (landkoder[index].visningsnavn.indexOf(searchTextUpperCase) !== -1) {
                            result.push(landkoder[index]);
                        }
                    }
                    return result;
                };

                $scope.endring = function() {
                    $scope.landkoden = $scope.landkode ? $scope.landkode.kode : undefined;
                    $timeout(function() {
                        $scope.endretFn($scope.index);
                    });
                };

                hentLandkoder();
            }]
        };
    }]);