angular.module('tps-forvalteren.skd-vis-meldingsgruppe.sendtiltps', ['ngMaterial'])
    .controller('SkdSendTilTpsCtrl', ['$scope', '$rootScope', '$mdDialog', '$stateParams', 'serviceRutineFactory', 'endringsmeldingService', 'utilsService', 'meldinger',
        function ($scope, $rootScope, $mdDialog, $stateParams, serviceRutineFactory, endringsmeldingService, utilsService, meldinger) {
//
            var gruppeId = $stateParams.gruppeId;
            $scope.showSpinner = false;

            $scope.avbryt = function () {
                $mdDialog.hide();
            };

            $scope.send = function () {
                $scope.showSpinner = true;
                var skdEndringsmeldingIdObjectToTps = {
                    environment: $scope.valgtMiljoe,
                    ids: meldinger
                };

                endringsmeldingService.sendTilTps(gruppeId, skdEndringsmeldingIdObjectToTps).then(
                    function () {
                        $scope.showSpinner = false;
                        $mdDialog.hide();
                        var alert = $mdDialog.alert()
                            .title('Bekreftelse')
                            .htmlContent('SKD-melding(er) for gruppe har blitt sendt til TPS.')
                            .ariaLabel('Bekreftelse på at SKD-meldinger har blitt sendt til TPS')
                            .ok('OK');
                        $mdDialog.show(alert);
                        $rootScope.$broadcast('MsgSentEvent', 'Meldinger sendt event');
                    },
                    function (error) {
                        $scope.showSpinner = false;
                        $mdDialog.hide();
                        utilsService.showAlertError(error);
                    }
                );
            };

            function sortEnvironmentsForDisplay(environments) {
                var filteredEnvironments = {};
                var sortedEnvironments = [];

                environments = utilsService.sortEnvironments(environments);

                angular.forEach(environments, function (env) {
                    var substrMiljoe = env.charAt(0);

                    if(filteredEnvironments[substrMiljoe]) {
                        filteredEnvironments[substrMiljoe].push(env);
                    } else {
                        filteredEnvironments[substrMiljoe] = [];
                        filteredEnvironments[substrMiljoe].push(env);
                    }
                });

                if (filteredEnvironments['u']) {
                    angular.forEach(filteredEnvironments['u'], function (env) {
                        sortedEnvironments.push(env);
                    });
                }

                if (filteredEnvironments['t']) {
                    angular.forEach(filteredEnvironments['t'], function (env) {
                        sortedEnvironments.push(env);
                    });
                }

                if (filteredEnvironments['q']) {
                    angular.forEach(filteredEnvironments['q'], function (env) {
                        sortedEnvironments.push(env);
                    });
                }

                return sortedEnvironments;
            }

            function init() {
                $scope.miljoer = sortEnvironmentsForDisplay(serviceRutineFactory.getEnvironments().environments);
                $scope.miljoe = $scope.miljoer[0].substr(0,1);
            }

            init();

        }]);