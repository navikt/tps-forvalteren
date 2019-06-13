angular.module('tps-forvalteren.avspiller', ['ngMessages', 'hljs'])
    .controller('AvspillerCtrl', ['$scope', 'utilsService', 'environmentsPromise', 'avspillerService', 'headerService',
        function ($scope, utilsService, environmentsPromise, avspillerService, headerService) {

            headerService.setHeader('TPS avspiller for hendelsesmeldinger');

            $scope.fagsystem = 'Distribusjonsmelding';
            $scope.tps = 'Ajourholdsmelding';

            var pagesize = 25;
            var buffersize = 300;

            $scope.tpsmeldinger = {};
            $scope.pager = {};

            $scope.startOfEra = new Date(2010, 0, 1); // Month is 0-indexed
            $scope.today = new Date();
            $scope.periodeFra = $scope.startOfEra;
            $scope.periodeTil = $scope.today;

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
                $scope.request.typer = undefined;
                $scope.request.kilder = undefined;
                $scope.loading = false;
            }

            function display(pagenum, offset) {
                return String((pagesize * (pagenum - 1 + offset)) + 1) + '-' + String((pagesize * (pagenum + offset)))
            }

            function determineTpsBuffer(buffersize, pagenum) {
                return Math.floor((pagenum - 1) / (buffersize / pagesize));
            }

            function lagreMeldinger(data) {
                $scope.tpsmeldinger.data = data.meldinger;
                $scope.tpsmeldinger.buffersize = data.buffersize;
                $scope.tpsmeldinger.buffernumber = data.buffernumber;
                $scope.pager.totalPages = Math.ceil(data.antallTotalt / pagesize);
                $scope.pager.totalt = data.antallTotalt;
            }

            function meldingerOk(data) {
                lagreMeldinger(data);
                $scope.setPage(1);
            }

            function kopierPage(pagenum) {
                var startAt = (pagesize * (pagenum - 1)) % buffersize;
                for (var i = startAt; i < Math.min(startAt + pagesize, $scope.tpsmeldinger.data.length); i++) {
                    $scope.meldinger.push($scope.tpsmeldinger.data[i]);
                }
            }

            function getOffset(pagenum) {
                var maxPgNum = Math.ceil($scope.pager.totalt / pagesize);
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
                $scope.pager.pages.push({pagenum: pagenum + offset, display: display(pagenum, offset)});
                $scope.pager.pages.push({pagenum: pagenum + offset + 1, display: display(pagenum, offset + 1)});
                $scope.pager.pages.push({pagenum: pagenum + offset + 2, display: display(pagenum, offset + 2)});
                $scope.pager.pages.push({pagenum: pagenum + offset + 3, display: display(pagenum, offset + 3)});
                $scope.pager.pages.push({pagenum: pagenum + offset + 4, display: display(pagenum, offset + 4)});
                $scope.pager.viser = $scope.pager.pages[0];

                $scope.pager.currentPage = pagenum;
                $scope.meldinger = [];

                var buffernumber = determineTpsBuffer($scope.pager.request.buffersize, pagenum);
                if (buffernumber != $scope.pager.request.buffernumber) {
                    $scope.pager.request.buffernumber = buffernumber;
                    avspillerService.getMeldinger($scope.pager.request)
                        .then(function (data) {
                            lagreMeldinger(data);
                            kopierPage(pagenum);
                        }, error);
                } else {
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
                $scope.target.meldingskoe = data.length === 1 ? data[0] : undefined;
                $scope.loading = false;
            }

            function error(disrupt) {
                utilsService.showAlertError(disrupt);
                $scope.loading = false;
            }

            $scope.checkOversikt = function () {
                $scope.periodeFra = $scope.request.periodeFra || $scope.startOfEra;
                $scope.periodeTil = $scope.request.periodeTil || $scope.today;
                if ($scope.request.miljoe && ((!$scope.request.periodeFra && !$scope.request.periodeTil) || ($scope.request.periodeFra && $scope.request.periodeTil))) {
                    $scope.loading = true;
                    avspillerService.getTyperOgKilder($scope.request)
                        .then(oversiktOk, error);
                }
            };

            $scope.getAntall = function (antall) {
                return antall ? ' (' + antall + ')' : '';
            };

            $scope.submit = function () {
                $scope.request.buffersize = buffersize;
                $scope.request.buffernumber = 0;
                $scope.pager.request = angular.copy($scope.request);
                avspillerService.getMeldinger($scope.request)
                    .then(meldingerOk, error);
                $scope.requestForm.$dirty=false;
            };

            $scope.checkMeldingskoer = function () {
                $scope.target.format = $scope.request.format;
                avspillerService.getMeldingskoer($scope.target)
                    .then(meldingskoerOk, error);
            };

            $scope.sendTilTps = function () {
                avspillerService.sendMeldinger($scope.request, $scope.target)
                    .then(function (data) {
                        $scope.progress = true;
                    }, error);
            };

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
        }])
;