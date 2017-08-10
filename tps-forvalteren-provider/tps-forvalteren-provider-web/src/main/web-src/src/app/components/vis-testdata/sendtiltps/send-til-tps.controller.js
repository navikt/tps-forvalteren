angular.module('tps-forvalteren.vis-testdata.sendtiltps', ['ngMaterial'])
    .controller('SendTilTpsCtrl', ['$scope', '$mdDialog', 'serviceRutineFactory', 'testdataService', '$location', 'utilsService',
        function ($scope, $mdDialog, serviceRutineFactory, testdataService, $location, utilsService) {

            var gruppeId = $location.url().match(/\d+/g)[0];

            var miljoer = serviceRutineFactory.getEnvironments();
            $scope.showSpinner = false;
            $scope.valgt_u_miljoer = [];
            $scope.valgt_t_miljoer = [];
            $scope.valgt_q_miljoer = [];
            $scope.valgt_p_miljoer = [];
            $scope.alleMiljoe = false;
            $scope.miljoeValgt = false;

            $scope.avbryt = function () {
                $mdDialog.hide();
            };

            $scope.send = function () {
                $scope.showSpinner = true;
                var valgt_miljoer = $scope.hent_valgt_miljoer();
                testdataService.sendTilTps(gruppeId, valgt_miljoer).then(
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

            $scope.hent_valgt_miljoer = function () {
                var send_til_miljoer = hent_valgt_miljoer_med_navn($scope.valgt_u_miljoer, $scope.u_miljoer);
                send_til_miljoer = send_til_miljoer.concat(hent_valgt_miljoer_med_navn($scope.valgt_t_miljoer, $scope.t_miljoer));
                send_til_miljoer = send_til_miljoer.concat(hent_valgt_miljoer_med_navn($scope.valgt_q_miljoer, $scope.q_miljoer));
                send_til_miljoer = send_til_miljoer.concat(hent_valgt_miljoer_med_navn($scope.valgt_p_miljoer, $scope.p_miljoer));
                return send_til_miljoer;
            };

            function hent_valgt_miljoer_med_navn (valgt_miljoer, miljoer) {
                var miljo_liste = [];
                for (var index = 0; index < valgt_miljoer.length; index++) {
                    if (valgt_miljoer[index]) {
                        miljo_liste.push(miljoer[index]);
                    }
                }
                return miljo_liste;
            }

            $scope.velgAlleMiljoe = function (status) {
                function setValgteMiljoer (status) {
                    sett_array_til($scope.u_miljoer, $scope.valgt_u_miljoer, status);
                    sett_array_til($scope.t_miljoer, $scope.valgt_t_miljoer, status);
                    sett_array_til($scope.q_miljoer, $scope.valgt_q_miljoer, status);
                    sett_array_til($scope.p_miljoer, $scope.valgt_p_miljoer, status);
                }
                setValgteMiljoer(status !== undefined ? status : !$scope.alleMiljoe);
                $scope.miljoeValgt = status !== undefined ? status : !$scope.alleMiljoe;
            };

            $scope.oppdaterVelgAlle = function () {
                $scope.alleMiljoe = erAlleValgt($scope.valgt_u_miljoer) &&
                    erAlleValgt($scope.valgt_t_miljoer) &&
                    erAlleValgt($scope.valgt_q_miljoer) &&
                    erAlleValgt($scope.valgt_p_miljoer);
                $scope.miljoeValgt = erValgt($scope.valgt_u_miljoer) ||
                    erValgt($scope.valgt_t_miljoer) ||
                    erValgt($scope.valgt_q_miljoer) ||
                    erValgt($scope.valgt_p_miljoer);
            };

            function erAlleValgt(array) {
                for (var index = 0; index < array.length; index++) {
                    if(!array[index]){
                        return false;
                    }
                }
                return true;
            }

            function sett_array_til (array, array2, verdi) {
                for (var index = 0; index < array.length; index++) {
                    array2[index] = verdi;
                }
            }

            function hasLetter (letter) {
                for (var i = 0; i < miljoer.length; i++) {
                    if (miljoer[i].toUpperCase().substring(0, 1) === letter) {
                        return true;
                    }
                }
                return false;
            }

            if (!hasLetter('U') && hasLetter('Q')) {
                $scope.miljo = 'Q';
            } else if (hasLetter('T')) {
                $scope.miljo = 'T';
            } else if (hasLetter('U') && !hasLetter('Q')) {
                $scope.miljo = 'U';
            } else {
                $scope.miljo = 'P';
            }

            function byggMiljoliste (letter) {
                var liste = [];
                for (var i = 0; i < miljoer.length; i++) {
                    if (miljoer[i].toUpperCase().substring(0, 1) === letter) {
                        liste.push(miljoer[i]);
                    }
                }
                return liste;
            }

            function erValgt(miljoe) {
                for (var i = 0; i<miljoe.length; i++) {
                    if (miljoe[i]) {
                        return true;
                    }
                }
                return false;
            }

            miljoer.sort(function (a, b) {
                return a.substring(1) - b.substring(1);
            });
            $scope.u_miljoer = byggMiljoliste('U');
            $scope.t_miljoer = byggMiljoliste('T');
            $scope.q_miljoer = byggMiljoliste('Q');
            $scope.p_miljoer = byggMiljoliste('P');
            $scope.velgAlleMiljoe(false);
        }]);