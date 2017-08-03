angular.module('tps-forvalteren.directives')
    .directive('familierelasjon', [function () {
        return {
            restrict: 'E',
            scope: {
                person: "=",
                personer: "=",
                endretFn: "=",
                index: "="
            },
            templateUrl: 'app/directives/familierelasjon/relasjoner.html',
            controller: ["$scope", '$mdDialog', '$filter', '$timeout', function ($scope, $mdDialog, $filter, $timeout) {
                $scope.leggTilRelasjonDialog = function () {

                    var confirm = $mdDialog.confirm({
                        locals: {
                            personer: $scope.personer,
                            endretFn: $scope.endretFn,
                            index: $scope.index
                        },
                        controller: relasjonerCtrl,
                        templateUrl: 'app/directives/familierelasjon/legg-til-relasjon-dialog.html',
                        parent: angular.element(document.body)
                    });
                    $mdDialog.show(confirm);

                    $scope.removePerson = function(relasjon) {
                        var confirm = $mdDialog.confirm()
                            .title('Bekreft sletting')
                            .textContent('Ønsker du å slette relasjon "' + $filter('titlecase')(relasjon.relasjonTypeNavn) +
                                '" med navn ' + relasjon.personRelasjonMed.fornavn + ' ' + relasjon.personRelasjonMed.etternavn + '?')
                            .ariaLabel('Bekreft sletting')
                            .ok('OK')
                            .cancel('Avbryt');

                        $mdDialog.show(confirm).then(function () {
                            for (var i = 0; $scope.person.relasjoner.length; i++) {
                                if ($scope.person.relasjoner[i].personRelasjonMed.ident === relasjon.personRelasjonMed.ident) {
                                    $scope.person.relasjoner.splice(i, 1);
                                    $timeout(function () {
                                        $scope.endretFn($scope.index);
                                    });
                                    break;
                                }
                            }
                            var ident = undefined;
                            var relasjonMed = undefined;
                            for (var i = 0; i < $scope.personer.length; i++) {
                                if ($scope.personer[i].ident === relasjon.personRelasjonMed.ident) {
                                    for (var j = 0; j < $scope.personer[i].relasjoner.length; j++) {
                                        if ($scope.personer[i].relasjoner[j].personRelasjonMed.ident === relasjon.person.ident) {
                                            ident = i;
                                            relasjonMed = j;
                                            break;
                                        }
                                    }
                                }
                            }
                            $scope.personer[ident].relasjoner.splice(relasjonMed, 1);
                            $timeout(function () {
                                $scope.endretFn(ident);
                            });
                        });
                    };
                };

                var relasjonerCtrl = function ($scope, $mdDialog, personer, index, endretFn, $timeout) {

                    $scope.relasjoner = [/*{opt: 'MOR'}, {opt: 'FAR'}, {opt: 'BARN'},*/ {opt: 'EKTEFELLE'}];

                    $scope.person = personer[index];
                    $scope.personer = personer;
                    $scope.tilgjengelige = angular.copy(personer);

                    function removePerson(ident) {
                        for (var i = 0; i < $scope.tilgjengelige.length; i++) {
                            if ($scope.tilgjengelige[i].ident == ident) {
                                $scope.tilgjengelige.splice(i, 1);
                            }
                        }
                    }

                    $scope.addRelasjon = function() {
                        var personUtenRelasjoner = angular.copy($scope.person);
                        personUtenRelasjoner.relasjoner = undefined;
                        $scope.person.relasjoner.push({person: personUtenRelasjoner,
                            personRelasjonMed: $scope.personForRelasjonSelector,
                            relasjonTypeNavn: $scope.relasjonValgt});
                        endretFn(index);
                        for (var i = 0; i < $scope.personer.length; i++) {
                            if ($scope.personer[i].ident === $scope.personForRelasjonSelector.ident) {
                                if ($scope.relasjonValgt == 'EKTEFELLE') {
                                    $scope.personer[i].relasjoner.push({person: $scope.personForRelasjonSelector,
                                        personRelasjonMed: personUtenRelasjoner,
                                        relasjonTypeNavn: $scope.relasjonValgt});
                                    $timeout(function () {
                                        endretFn(i);
                                    });
                                    break;
                                }
                            }
                        }
                        $mdDialog.cancel();
                    };

                    $scope.cancelRelasjon = function() {
                        $mdDialog.cancel();
                    };

                    // Ta bort egen identitet fra kandidatliste
                    removePerson($scope.person.ident);
                    // Fjerne eksisterende relasjoner fra kandidatliste
                    for (var i = 0; i < $scope.person.relasjoner.length; i++) {
                        removePerson($scope.person.relasjoner[i].personRelasjonMed.ident);
                    }
                };
            }]
        };
    }]);
