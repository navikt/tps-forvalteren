angular.module('tps-forvalteren.vis-testdata.sendtiltps', ['ngMaterial'])
    .controller('SendTilTpsCtrl', ['$scope', '$mdDialog', '$stateParams','$rootScope', 'serviceRutineFactory', 'testdataService', 'utilsService',
        function ($scope, $mdDialog, $stateParams, $rootScope, serviceRutineFactory, testdataService, utilsService) {

            var gruppeId = $stateParams.gruppeId;

            $scope.showSpinner = false;
            $scope.alleMiljoe = false;
            $scope.alleUMiljoer = false;
            $scope.alleTMiljoer = false;
            $scope.alleQMiljoer = false;
            $scope.miljoeValgt = false;

            $scope.avbryt = function () {
                $mdDialog.hide();
            };

            $scope.send = function () {
                $scope.showSpinner = true;
                testdataService.sendTilTps(gruppeId, getSelectedMiljoer()).then(
                    function () {
                        $scope.showSpinner = false;
                        $mdDialog.hide();
                        var alert = $mdDialog.alert()
                            .title('Bekreftelse')
                            .htmlContent('Testpersoner for gruppe har blitt sendt til TPS.')
                            .ariaLabel('Bekreftelse på at testpersoner har blitt sendt til TPS')
                            .ok('OK');
                        $mdDialog.show(alert);
                        $rootScope.$broadcast('tps-sent');
                    },
                    function (error) {
                        $mdDialog.hide();
                        utilsService.showAlertError(error);
                    }
                );
            };

            function getSelectedMiljoer() {
                var miljoer = [];

                Object.keys($scope.sortedMiljoer).forEach(function (key) {
                    $scope.sortedMiljoer[key].forEach(function (env, index) {
                        if ($scope.valgteMiljoer[key][index]) {
                            miljoer.push(env);
                        }
                    })
                });

                return miljoer;
            }

            $scope.velgAlleMiljoe = function () {

                if ($scope.sortedMiljoer.u) {
                    $scope.sortedMiljoer.u.forEach(function (env, index) {
                        $scope.valgteMiljoer.u[index] = !$scope.alleMiljoe;
                    });

                    var env_status = true;
                    $scope.valgteMiljoer.u.forEach(function (env) {
                        if (!env) {
                            env_status = false;
                        }
                    });
                    $scope.alleUMiljoer = env_status;
                }

                if ($scope.sortedMiljoer.t) {
                    $scope.sortedMiljoer.t.forEach(function (env, index) {
                        $scope.valgteMiljoer.t[index] = !$scope.alleMiljoe;
                    });
                    var env_status = true;
                    $scope.valgteMiljoer.t.forEach(function (env) {
                        if (!env) {
                            env_status = false;
                        }
                    });
                    $scope.alleTMiljoer = env_status;
                }

                if ($scope.sortedMiljoer.q) {
                    $scope.sortedMiljoer.q.forEach(function (env, index) {
                        $scope.valgteMiljoer.q[index] = !$scope.alleMiljoe;
                    });

                    var env_status = true;
                    $scope.valgteMiljoer.q.forEach(function (env) {
                        if (!env) {
                            env_status = false;
                        }
                    });
                    $scope.alleQMiljoer = env_status;
                }

                isMiljoeSelected();
            };

            $scope.velgAlleFraUMiljoe = function () {
                if ($scope.sortedMiljoer.u) {
                    $scope.sortedMiljoer.u.forEach(function (env, index) {
                        $scope.valgteMiljoer.u[index] = !$scope.alleUMiljoer;
                    });
                }

                var sortedMiljoer_length = antallMiljoerTotalt();
                var valgteMiljoer_counter = antallValgteMiljoer();

                if (sortedMiljoer_length === valgteMiljoer_counter) {
                    $scope.alleMiljoe = true;
                } else {
                    $scope.alleMiljoe = false;
                }

                isMiljoeSelected();
            };

            $scope.velgAlleFraTMiljoe = function () {
                if ($scope.sortedMiljoer.t) {
                    $scope.sortedMiljoer.t.forEach(function (env, index) {
                        $scope.valgteMiljoer.t[index] = !$scope.alleTMiljoer;
                    });
                }

                var sortedMiljoer_length = antallMiljoerTotalt();
                var valgteMiljoer_counter = antallValgteMiljoer();

                if (sortedMiljoer_length === valgteMiljoer_counter) {
                    $scope.alleMiljoe = true;
                } else {
                    $scope.alleMiljoe = false;
                }

                isMiljoeSelected();
            };

            $scope.velgAlleFraQMiljoe = function () {
                if ($scope.sortedMiljoer.q) {
                    $scope.sortedMiljoer.q.forEach(function (env, index) {
                        $scope.valgteMiljoer.q[index] = !$scope.alleQMiljoer;
                    });
                }

                var sortedMiljoer_length = antallMiljoerTotalt();
                var valgteMiljoer_counter = antallValgteMiljoer();

                if (sortedMiljoer_length === valgteMiljoer_counter) {
                    $scope.alleMiljoe = true;
                } else {
                    $scope.alleMiljoe = false;
                }

                isMiljoeSelected();
            };

            $scope.oppdaterVelgAlle = function () {
                var allEnvironmentsChecked = true;
                var sortedMiljoer_length = antallMiljoerTotalt();
                var valgteMiljoer_counter = antallValgteMiljoer();

                if (!(sortedMiljoer_length === valgteMiljoer_counter)) {

                    if ($scope.valgteMiljoer.u) {
                        var env_status = true;
                        $scope.valgteMiljoer.u.forEach(function (env) {
                            if (!env) {
                                env_status = false;
                            }
                        });

                        if ($scope.sortedMiljoer.u.length !== $scope.valgteMiljoer.u.length) {
                            env_status = false;
                        }
                        $scope.alleUMiljoer = env_status;
                    }

                    if ($scope.valgteMiljoer.t) {
                        var env_status = true;
                        $scope.valgteMiljoer.t.forEach(function (env) {
                            if (!env) {
                                env_status = false;
                            }
                        });

                        if ($scope.sortedMiljoer.t.length !== $scope.valgteMiljoer.t.length) {
                            env_status = false;
                        }
                        $scope.alleTMiljoer = env_status;
                    }

                    if ($scope.valgteMiljoer.q) {
                        var env_status = true;
                        $scope.valgteMiljoer.q.forEach(function (env) {
                            if (!env) {
                                env_status = false;
                            }
                        });

                        if ($scope.sortedMiljoer.q.length !== $scope.valgteMiljoer.q.length) {
                            env_status = false;
                        }
                        $scope.alleQMiljoer = env_status;
                    }

                    allEnvironmentsChecked = false;

                } else {
                    allEnvironmentsChecked = true;
                }

                $scope.alleMiljoe = allEnvironmentsChecked;

                isMiljoeSelected();
            };

            function isMiljoeSelected() {
                $scope.miljoeValgt = false;

                Object.keys($scope.valgteMiljoer).forEach(function (key) {
                    $scope.valgteMiljoer[key].forEach(function (env) {
                        if (env) {
                            $scope.miljoeValgt = true;
                        }
                    });
                });
            }

            $scope.reloadPage = function () {
                location.reload();
            }

            function antallValgteMiljoer() {
                var valgteMiljoer_counter = 0; //Counts how many environments that are checked.

                Object.keys($scope.valgteMiljoer).forEach(function (key) {
                    if ($scope.valgteMiljoer[key]) {
                        $scope.valgteMiljoer[key].forEach(function (env_checked) {
                            if (env_checked) {
                                valgteMiljoer_counter++; //Increments if a 'checked' environment is found.
                            }
                        })
                    }
                });

                return valgteMiljoer_counter;
            }

            function antallMiljoerTotalt() {
                var sortedMiljoer_length = 0;

                Object.keys($scope.sortedMiljoer).forEach(function (key) {
                    sortedMiljoer_length += $scope.sortedMiljoer[key].length;
                });

                return sortedMiljoer_length;
            }

            function init() {
                var miljoer = serviceRutineFactory.getEnvironments().environments;
                $scope.valgteMiljoer = {};

                miljoer.sort(function (a, b) {
                    return a.substring(1) - b.substring(1);
                });

                $scope.sortedMiljoer = {};
                angular.forEach(miljoer, function (miljoe) {
                    var substrMiljoe = miljoe.charAt(0);

                    if ($scope.sortedMiljoer[substrMiljoe]) {
                        $scope.sortedMiljoer[substrMiljoe].push(miljoe);
                    } else {
                        $scope.sortedMiljoer[substrMiljoe] = [];
                        $scope.sortedMiljoer[substrMiljoe].push(miljoe);
                        $scope.valgteMiljoer[substrMiljoe] = [];
                    }
                });

            }

            init();
        }]);