angular.module('tps-forvalteren.vis-testdata.sendtiltps', ['ngMaterial'])
    .controller('SendTilTpsCtrl', ['$scope', '$mdDialog', 'serviceRutineFactory', 'testdataService', '$location', 'utilsService',
        function ($scope, $mdDialog, serviceRutineFactory, testdataService, $location, utilsService) {

            var gruppeId = $location.url().match(/\d+/g);

            var miljoer = serviceRutineFactory.getEnvironments();

            $scope.showSpinner = false;

            $scope.avbryt = function () {
                $mdDialog.hide();
            };

            $scope.send = function () {
                $scope.showSpinner = true;
                testdataService.sendTilTps(gruppeId).then(
                    function () {
                        $scope.showSpinner = false;
                        $mdDialog.hide();
                        var alert = $mdDialog.alert()
                            .title('Bekreftelse')
                            .textContent('Testpersoner for gruppe har blitt sendt til TPS')
                            .ariaLabel('Bekreftelse på at testpersoner har blitt sendt til TPS')
                            .ok('OK');
                        $mdDialog.show(alert);
                    },
                    function (error) {
                        $mdDialog.hide();
                        utilsService.showAlertError(error);
                    }
                );
            };

            var hasLetter = function (letter) {
                for (var i = 0; i < miljoer.length; i++) {
                    if (miljoer[i].toUpperCase().substring(0, 1) === letter) {
                        return true;
                    }
                }
                return false;
            };

            if (!hasLetter('U') && hasLetter('T') && hasLetter('Q')) {
                $scope.miljo = 'Q';
            } else if (hasLetter('U') && hasLetter('T') && hasLetter('Q')) {
                $scope.miljo = 'T';
            } else if (hasLetter('U') && hasLetter('T') && !hasLetter('Q')) {
                $scope.miljo = 'U';
            } else {
                $scope.miljo = 'P';
            }

            var byggMiljoliste = function (letter) {
                var liste = '';
                for (var i = 0; i < miljoer.length; i++) {
                    if (miljoer[i].toUpperCase().substring(0, 1) === letter) {
                        liste += ', ' + miljoer[i];
                    }
                }
                return liste.substring(2);
            };

            miljoer.sort(function (a, b) {
                return a.substring(1) - b.substring(1);
            });
            $scope.u_miljoer = byggMiljoliste('U');
            $scope.t_miljoer = byggMiljoliste('T');
            $scope.q_miljoer = byggMiljoliste('Q');
            $scope.p_miljoer = byggMiljoliste('P');

        }]);