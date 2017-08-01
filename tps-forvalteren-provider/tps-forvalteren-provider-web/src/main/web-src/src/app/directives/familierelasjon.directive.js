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
            templateUrl: 'app/components/vis-testdata/relasjon/relasjoner.html',
            controller: ["$scope", '$mdDialog', function ($scope, $mdDialog) {
                $scope.leggTilRelasjonDialog = function () {

                    var confirm = $mdDialog.confirm({
                        locals: {
                            personer: $scope.personer,
                            endretFn: $scope.endretFn,
                            index: $scope.index
                        },
                        controller: relasjonerCtrl,
                        templateUrl: 'app/components/vis-testdata/relasjon/legg-til-relasjon-dialog.html',
                        parent: angular.element(document.body)
                    });
                    $mdDialog.show(confirm);

                    $scope.removePerson = function(relasjon) {
                        var confirm = $mdDialog.confirm()
                            .title('Bekreft sletting')
                            .textContent('Ønsker du å slette relasjon "' + relasjon.relasjonTypeKode +
                                '" med navn ' + relasjon.personRelasjonMed.fornavn + ' ' + relasjon.personRelasjonMed.etternavn + '?')
                            .ariaLabel('Bekreft sletting')
                            .ok('OK')
                            .cancel('Avbryt');

                        $mdDialog.show(confirm).then(function () {
                            for (var i = 0; $scope.person.relasjoner.length; i++) {
                                if ($scope.person.relasjoner[i].personRelasjonMed.ident == relasjon.personRelasjonMed.ident) {
                                    $scope.person.relasjoner.splice(i, 1);
                                    $scope.endretFn($scope.index);
                                    break;
                                }
                            }
                        });
                    };
                };

                var relasjonerCtrl = function ($scope, $mdDialog, personer, index, endretFn) {

                    $scope.relasjoner = [{opt: 'Mor'}, {opt: 'Far'}, {opt: 'Barn'}, {opt: 'Ektefelle'}];

                    $scope.person = personer[index];
                    $scope.personer = angular.copy(personer);

                    function removePerson(ident) {
                        for (var i = 0; i < $scope.personer.length; i++) {
                            if ($scope.personer[i].ident == ident) {
                                $scope.personer.splice(i, 1);
                            }
                        }
                    }

                    $scope.addRelasjon = function() {
                        var personUtenRelasjoner = angular.copy($scope.person);
                        personUtenRelasjoner.relasjoner = undefined;
                        $scope.person.relasjoner.push({person: personUtenRelasjoner,
                            personRelasjonMed: $scope.personForRelasjonSelector,
                            relasjonTypeNavn: $scope.relasjonValgt.toUpperCase()});
                        endretFn(index);
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
