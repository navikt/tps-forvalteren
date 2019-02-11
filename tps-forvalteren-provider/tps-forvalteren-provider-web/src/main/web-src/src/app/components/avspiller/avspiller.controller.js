angular.module('tps-forvalteren.avspiller', ['ngMessages', 'hljs'])
    .controller('AvspillerCtrl', ['$scope', 'utilsService', 'environmentsPromise', 'avspillerService', 'headerService',
        function ($scope, utilsService, environmentsPromise, avspillerService, headerService) {

            headerService.setHeader('TPS avspiller for hendelsesmeldinger');

            $scope.fagsystem = 'Distribusjonsmelding';
            $scope.tps = 'Ajourholdsmelding';

            $scope.startOfEra = new Date(2002, 0, 1); // Month is 0-indexed
            $scope.today = new Date();
            $scope.periodeFra = $scope.startOfEra;
            $scope.periodeTil = $scope.today;

            var computeDefaultPeriode = function (days) {
                var defaultPeriode = new Date();
                defaultPeriode.setTime(defaultPeriode.getTime() - (24 * 60 * 60 * 1000) * days);
                return defaultPeriode;
            };

            $scope.disableTyperOgKilder = true;
            $scope.request = {};
            $scope.request.periodeFra = computeDefaultPeriode(10);
            $scope.request.periodeTil = $scope.today;
            $scope.request.format = $scope.fagsystem;

            var oversiktOk = function (data) {
                $scope.typer = data.typer;
                $scope.kilder = data.kilder;
                $scope.disableTyperOgKilder = false;
                $scope.request.typer = undefined;
                $scope.request.kilder = undefined;
            };

            var meldingerOk = function (data) {
                $scope.meldinger = data.meldinger;
                $scope.showResponse = data.antallTotalt > 0;
            };

            var meldingskoerOk = function (data) {
                $scope.koer = data;
            };

            var error = function (disrupt) {
                utilsService.showAlertError(disrupt);
            };

            $scope.checkOversikt = function () {
                $scope.periodeFra = $scope.request.periodeFra || $scope.startOfEra;
                $scope.periodeTil = $scope.request.periodeTil || $scope.today;
                if ($scope.request.miljoe && ((!$scope.request.periodeFra && !$scope.request.periodeTil) || ($scope.request.periodeFra && $scope.request.periodeTil))) {
                    avspillerService.getTyperOgKilder($scope.request)
                        .then(oversiktOk, error);
                }
            };

            $scope.getAntall = function (antall) {
                return antall ? ' (' + antall + ')' : '';
            };

            $scope.submit = function () {
                avspillerService.getMeldinger($scope.request)
                    .then(meldingerOk, error);
            };

            $scope.checkMeldingskoer = function() {
                $scope.request2.format = $scope.request.format;
                avspillerService.getMeldingskoer($scope.request2)
                    .then(meldingskoerOk, error);
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
        }]);