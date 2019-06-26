angular.module('tps-forvalteren.avspiller', ['ngMessages', 'hljs'])
    .controller('AvspillerCtrl', ['$scope', '$timeout', '$mdDialog', 'utilsService', 'environmentsPromise', 'avspillerService', 'headerService',
        function ($scope, $timeout, $mdDialog, utilsService, environmentsPromise, avspillerService, headerService) {

            headerService.setHeader('TPS avspiller for hendelsesmeldinger');

            $scope.fagsystem = 'DISTRIBUSJONSMELDING';
            $scope.tps = 'AJOURHOLDSMELDING';

            $scope.pagesize = 25;
            var buffersize = 150;
            $scope.timeout = 30;

            $scope.tpsmeldinger = {};
            $scope.pager = {};

            $scope.startOfEra = new Date(2010, 0, 1); // Month is 0-indexed
            $scope.today = new Date();
            $scope.periodeFra = $scope.startOfEra;
            $scope.periodeTil = $scope.today;
            $scope.autoload = true;
            $scope.ownQueue = false;

            function computeDefaultPeriode(days) {
                var defaultPeriode = new Date();
                defaultPeriode.setTime(defaultPeriode.getTime() - (24 * 60 * 60 * 1000) * days);
                return defaultPeriode;
            }

            $scope.disableTyperOgKilder = true;
            $scope.request = {};
            $scope.request.periodeFra = computeDefaultPeriode(10);
            $scope.request.periodeTil = $scope.today;
            $scope.request.format = $scope.fagsystem;

            function oversiktOk(data) {
                $scope.typer = data.typer;
                $scope.kilder = data.kilder;
                $scope.disableTyperOgKilder = false;
                $scope.loading = false;
                if ($scope.autoload) {
                    if ($scope.request.periodeFra && $scope.request.periodeTil) {
                        $scope.loading2 = true;
                        $scope.submit();
                    } else {
                        $mdDialog.show($mdDialog.confirm()
                            .title('Detaljert melding')
                            .textContent('Tidsperiode er ikke angitt og kun oversikt på hendelser og kilder er hentet fra TPS.')
                            .ariaLabel('Detaljert melding fra TPS')
                            .clickOutsideToClose(true)
                            .ok('OK'));
                    }
                }
            }

            function display(pagenum, offset) {
                return String(($scope.pagesize * (pagenum - 1 + offset)) + 1) + '-' + String(($scope.pagesize * (pagenum + offset)))
            }

            function determineTpsBuffer(buffersize, pagenum) {
                return Math.floor((pagenum - 1) / (buffersize / $scope.pagesize));
            }

            function lagreMeldinger(data) {
                $scope.tpsmeldinger.data = data.meldinger;
                $scope.tpsmeldinger.buffersize = data.buffersize;
                $scope.tpsmeldinger.buffernumber = data.buffernumber;
                $scope.pager.totalPages = Math.ceil(data.antallTotalt / $scope.pagesize);
                $scope.pager.totalt = data.antallTotalt;
            }

            function meldingerOk(data) {
                lagreMeldinger(data);
                $scope.setPage(1);
                $scope.loading2 = false;
            }

            function kopierPage(pagenum) {
                var startAt = ($scope.pagesize * (pagenum - 1)) % buffersize;
                for (var i = startAt; i < Math.min(startAt + $scope.pagesize, $scope.tpsmeldinger.data.length); i++) {
                    $scope.meldinger.push($scope.tpsmeldinger.data[i]);
                }
            }

            function getOffset(pagenum) {
                var maxPgNum = Math.ceil($scope.pager.totalt / $scope.pagesize);
                if (pagenum === 1) {
                    return 0;
                } else if (pagenum === 2) {
                    return -1;
                } else if (pagenum === maxPgNum) {
                    return -4;
                } else if (pagenum === maxPgNum - 1) {
                    return -3;
                } else {
                    return -2;
                }
            }

            function setPages(pagenum) {
                $scope.pager.pages = [];
                var offset = getOffset(pagenum);
                if ($scope.pager.totalPages > 5) {
                    $scope.pager.pages.push({pagenum: pagenum + offset, display: display(pagenum, offset)});
                    $scope.pager.pages.push({pagenum: pagenum + offset + 1, display: display(pagenum, offset + 1)});
                    $scope.pager.pages.push({pagenum: pagenum + offset + 2, display: display(pagenum, offset + 2)});
                    $scope.pager.pages.push({pagenum: pagenum + offset + 3, display: display(pagenum, offset + 3)});
                    $scope.pager.pages.push({pagenum: pagenum + offset + 4, display: display(pagenum, offset + 4)});
                } else {
                    $scope.pager.pages.push({pagenum: 1, display: '1-25'});
                    if ($scope.pager.totalPages > 1) {
                        $scope.pager.pages.push({pagenum: 2, display: '26-50'});
                    }
                    if ($scope.pager.totalPages > 2) {
                        $scope.pager.pages.push({pagenum: 3, display: '51-75'});
                    }
                    if ($scope.pager.totalPages > 3) {
                        $scope.pager.pages.push({pagenum: 4, display: '76-100'});
                    }
                    if ($scope.pager.totalPages > 4) {
                        $scope.pager.pages.push({pagenum: 5, display: '101-125'});
                    }
                }
                $scope.pager.viser = $scope.pager.pages[-offset];

                $scope.pager.currentPage = pagenum;

                var buffernumber = determineTpsBuffer($scope.pager.request.buffersize, pagenum);
                if (buffernumber != $scope.pager.request.buffernumber) {
                    $scope.pager.request.buffernumber = buffernumber;
                    $scope.loading2 = true;
                    avspillerService.getMeldinger($scope.pager.request)
                        .then(function (data) {
                            lagreMeldinger(data);
                            $scope.meldinger = [];
                            kopierPage(pagenum);
                            $scope.loading2 = false;
                        }, error);
                } else {
                    $scope.meldinger = [];
                    kopierPage(pagenum);
                }
            }

            $scope.setPage = function (pagenum) {
                if (pagenum == 0) {
                    setPages(1);
                } else if (pagenum > $scope.pager.totalPages) {
                    setPages($scope.pager.totalPages);
                } else {
                    setPages(pagenum);
                }
            };

            function meldingskoerOk(data) {
                $scope.koer = data;
                $scope.target = $scope.target || {};
                $scope.target.messageQueue = data.length === 1 ? data[0] : undefined;
                $scope.loading = false;
            }

            function error(disrupt) {
                utilsService.showAlertError(disrupt);
                $scope.loading = false;
                $scope.loading2 = false;
                $scope.meldinger = undefined;
            }

            $scope.checkOversikt = function () {
                $scope.periodeFra = atStartOfDay($scope.request.periodeFra);
                $scope.periodeTil = atEndOfDay($scope.request.periodeTil);
                $scope.meldinger = undefined;
                $scope.target = undefined;
                $scope.request.typer = undefined;
                $scope.request.kilder = undefined;
                $scope.request.identer = undefined;
                $scope.identer = [];
                $scope.ownQueue = false;
                if ($scope.request.miljoe && ((!$scope.request.periodeFra && !$scope.request.periodeTil) || ($scope.request.periodeFra && $scope.request.periodeTil))) {
                    $scope.loading = true;
                    $scope.request.timeout = $scope.timeout;
                    avspillerService.getTyperOgKilder($scope.request)
                        .then(oversiktOk, error);
                }
            };

            $scope.getAntall = function (antall) {
                return antall ? ' (' + antall + ')' : '';
            };

            $scope.paramUpdate = function () {
                $scope.requestForm.$dirty = true;
                if ($scope.autoload) {
                    conditionalLoad();
                }
            };

            $scope.autoloadToggle = function () {
                conditionalLoad();
            };

            $scope.toggleOwnQueue = function() {
                if ($scope.ownQueue) {
                    $scope.target.privateQueue = $scope.target.messageQueue;
                    $scope.target.messageQueue = undefined;
                } else {
                    $scope.target.privateQueue = undefined;
                }
            };

            $scope.submit = function () {
                if (!$scope.request.periodeFra || !$scope.request.periodeTil) {
                    $mdDialog.show($mdDialog.confirm()
                        .title('Periode mangler')
                        .textContent('Tidsperiode er ikke angitt og detaljer kan ikke hentes.')
                        .ariaLabel('Detaljert melding fra TPS')
                        .clickOutsideToClose(true)
                        .ok('OK'));
                } else {
                    $scope.loading2 = true;
                    $scope.status = undefined;
                    $scope.request.buffersize = buffersize;
                    $scope.request.buffernumber = 0;
                    $scope.request.timeout = $scope.timeout;
                    atStartOfDay($scope.request.periodeFra);
                    atEndOfDay($scope.request.periodeTil);
                    $scope.pager.request = angular.copy($scope.request);
                    avspillerService.getMeldinger($scope.request)
                        .then(meldingerOk, error);
                    $scope.requestForm.$dirty = false;
                }
            };

            $scope.checkMeldingskoer = function () {
                $scope.target.format = $scope.request.format;
                avspillerService.getMeldingskoer($scope.target)
                    .then(meldingskoerOk, error);
            };

            $scope.sendTilTps = function () {
                $scope.status = undefined;
                $scope.progress = true;
                avspillerService.sendMeldinger($scope.request, $scope.target)
                    .then(function (data) {
                        $scope.completeProgress = 0;
                        $scope.status = data;
                        $timeout(checkStatus, 1000);
                    }, function(disrupt) {
                        utilsService.showAlertError(disrupt);
                        $scope.loading = false;
                        $scope.loading2 = false;
                        $scope.progress = false;
                    });
            };

            $scope.getMelding = function (meldingNr) {
                if (meldingNr) {
                    avspillerService.getMelding({miljoe: $scope.request.miljoe, format: $scope.request.format, meldingnr: meldingNr})
                        .then(function (data) {
                            $mdDialog.show($mdDialog.confirm()
                                .title('Detaljert melding')
                                .textContent(atob(data.data))
                                .ariaLabel('Detaljert melding fra TPS')
                                .clickOutsideToClose(true)
                                .ok('OK')
                            );
                        });
                }
            };

            $scope.addIdent = function (ident) {

                if ($scope.identer.indexOf(ident) === -1) {
                    $scope.identer.push(ident);
                } else {
                    $scope.identer.splice($scope.identer.indexOf(ident), 1);
                }
                $scope.request.identer = $scope.identer.join(',');
                $scope.requestForm.$dirty = true;
            };

            $scope.changeIdenter = function () {

                $scope.identer = $scope.request.identer ? $scope.request.identer.split(',') : [];
            };

            $scope.avbrytSendTilTps = function () {
                avspillerService.cancelSendMeldinger($scope.status.bestillingId)
                    .then(function (data) {
                        $scope.status = data;
                        $scope.progress = false;
                        $mdDialog.show($mdDialog.confirm()
                            .title('Avbrutt Sending')
                            .textContent("Sending til TPS ble avbrutt av bruker")
                            .ariaLabel('Avbrudd bekreftelse')
                            .ok('OK')
                        ).then(function () {
                            checkStatus();
                        });
                    });
            };

            function conditionalLoad() {
                if ($scope.requestForm.$dirty && $scope.request.miljoe && $scope.request.periodeFra && $scope.request.periodeTil) {
                    $scope.loading2 = true;
                    $scope.submit();
                }
            }

            function atStartOfDay(starttime) {
                if (starttime) {
                    starttime.setHours(0);
                    starttime.setMinutes(0);
                    starttime.setSeconds(0);
                }
                return starttime;
            }

            function atEndOfDay(endtime) {
                if (endtime) {
                    endtime.setHours(23);
                    endtime.setMinutes(59);
                    endtime.setSeconds(59);
                }
                return endtime;
            }

            function checkStatus() {
                avspillerService.getStatus($scope.status.bestillingId)
                    .then(function (data) {
                        $scope.status = data;
                        $scope.completeProgress = Math.floor($scope.status.progressAntall / $scope.status.antall * 100);
                        if (!$scope.status.avbrutt) {
                            if ($scope.status.ferdig) {
                                $scope.progress = false;
                                $mdDialog.show($mdDialog.confirm()
                                    .title(!$scope.status.error ? 'Sending Bekreftelse' : 'Feilmelding')
                                    .htmlContent(!$scope.status.error ?
                                        'Meldinger er sendt til valgt kø.<br>Sjekk detaljert status for resultat.' : $scope.status.error)
                                    .ariaLabel('Meldingsending bekreftelse')
                                    .ok('OK')
                                );
                            } else {
                                $timeout(checkStatus, 1000);
                            }
                        }
                    })
            }

            function sortEnvironmentsForDisplay(environments) {
                var filteredEnvironments = {};
                var sortedEnvironments = [];

                var addEnvironment = function (environment) {
                    if (filteredEnvironments[environment]) {
                        angular.forEach(filteredEnvironments[environment], function (env) {
                            sortedEnvironments.push(env);
                        });
                    }
                };

                environments = utilsService.sortEnvironments(environments);

                angular.forEach(environments, function (env) {
                    var substrMiljoe = env.charAt(0);
                    filteredEnvironments[substrMiljoe] = filteredEnvironments[substrMiljoe] ? filteredEnvironments[substrMiljoe] : [];
                    filteredEnvironments[substrMiljoe].push(env);
                });

                addEnvironment('u');
                addEnvironment('t');
                addEnvironment('q');
                addEnvironment('p');

                return sortedEnvironments;
            }

            var miljoer = $scope.$resolve.environmentsPromise;
            if (miljoer && miljoer.environments) {
                $scope.environments = sortEnvironmentsForDisplay(miljoer.environments);
            } else {
                utilsService.showAlertError(miljoer);
            }
        }

    ])
;