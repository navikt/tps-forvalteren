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
                        controller: ['$scope', '$mdDialog', '$timeout', 'locals',
                            function ($scope, $mdDialog, $timeout, locals) {
                                var MOR = 'MOR', FAR = 'FAR', BARN = 'BARN', EKTEFELLE = 'EKTEFELLE';

                                $scope.relasjoner = [{opt: MOR}, {opt: FAR}, {opt: BARN}, {opt: EKTEFELLE}];

                                $scope.endretFn = locals.endretFn;
                                $scope.index = locals.index;

                                $scope.person = locals.personer[$scope.index];
                                $scope.personer = locals.personer;
                                $scope.tilgjengelige = angular.copy($scope.personer);

                                function removePerson(ident) {
                                    for (var i = 0; i < $scope.tilgjengelige.length; i++) {
                                        if ($scope.tilgjengelige[i].ident === ident) {
                                            $scope.tilgjengelige.splice(i, 1);
                                        }
                                    }
                                }

                                function bestemRelasjon (relasjon, kjonn) {
                                    switch (relasjon) {
                                        case EKTEFELLE:
                                            return EKTEFELLE;
                                        case FAR:
                                        case MOR:
                                            return BARN;
                                        case BARN:
                                            return kjonn === 'K' ? MOR : FAR;
                                    }
                                }

                                $scope.addRelasjon = function() {
                                    var personUtenRelasjoner = angular.copy($scope.person);
                                    personUtenRelasjoner.relasjoner = undefined;
                                    $scope.person.relasjoner = $scope.person.relasjoner || [];
                                    $scope.person.relasjoner.push({person: personUtenRelasjoner,
                                        personRelasjonMed: $scope.personForRelasjonSelector,
                                        relasjonTypeNavn: $scope.relasjonValgt});
                                    $scope.endretFn($scope.index);
                                    for (var i = 0; i < $scope.personer.length; i++) {
                                        if ($scope.personer[i].ident === $scope.personForRelasjonSelector.ident) {
                                            $scope.personer[i].relasjoner = $scope.personer[i].relasjoner || [];
                                            $scope.personer[i].relasjoner.push({person: $scope.personForRelasjonSelector,
                                                personRelasjonMed: personUtenRelasjoner,
                                                relasjonTypeNavn: bestemRelasjon($scope.relasjonValgt, personUtenRelasjoner.kjonn)});
                                            $timeout(function () {
                                                $scope.endretFn(i);
                                            });
                                            break;
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
                                if ($scope.person.relasjoner) {
                                    for (var i = 0; i < $scope.person.relasjoner.length; i++) {
                                        removePerson($scope.person.relasjoner[i].personRelasjonMed.ident);
                                    }
                                }
                            }],
                        templateUrl: 'app/directives/familierelasjon/legg-til-relasjon-dialog.html',
                        parent: angular.element(document.body)
                    });
                    $mdDialog.show(confirm);
                };

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
                                $scope.endretFn($scope.index);
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
                        $scope.endretFn(ident);
                    });
                };
            }]
        };
    }]);
