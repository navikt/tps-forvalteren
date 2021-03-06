angular.module('tps-forvalteren.directives')
    .directive('visInnsendingslogg', function () {
        return {
            restrict: 'E',
            replace: true,
            scope: {
                gruppeId: '=',
                service: '='
            },
            templateUrl: 'app/directives/innsendingslogg/vis-innsendingslogg.html',
            controller: ["$scope", '$mdDialog', '$filter', 'utilsService', function ($scope, $mdDialog, $filter, utilsService) {

                $scope.logg = [];
                function getInnsendingslogg () {
                    $scope.service.getInnsendingslogg($scope.gruppeId).then(
                        function (result) {
                            $scope.logg = result.data;
                        },
                        function (error) {
                            utilsService.showAlertError(error);
                        }
                    );
                }

                $scope.$on('MsgSentEvent', function () {
                    getInnsendingslogg();
                });

                $scope.showMelding = function (melding) {
                    var details = $mdDialog.alert()
                        .title('Detaljvisning av innsendt SKD-melding')
                        .htmlContent('Miljø:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' + melding.miljoe + '<br>' +
                            'Tidspunkt: ' + $filter('date')(melding.utfoertDato, 'dd-MM-yyyy HH:mm:ss') + '<br>'  +
                            'Utført av:&nbsp;&nbsp;&nbsp;' + melding.utfoertAv + '<br><br>' +
                            'Beskrivelse:&nbsp;&nbsp;&nbsp;&nbsp;' + melding.beskrivelse + '<br>' +
                            'Melding:<pre>' + melding.raw + '</pre>')
                        .ariaLabel('Detaljvisning av SKD melding')
                        .ok('OK');

                    $mdDialog.show(details);
                };

                getInnsendingslogg();
            }]
        };
    });


