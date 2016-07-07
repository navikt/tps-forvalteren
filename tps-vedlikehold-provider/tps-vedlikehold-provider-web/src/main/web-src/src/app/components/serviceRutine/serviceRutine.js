/**
 * @author Frederik de Lichtenberg (Visma Consulting AS).
 */
angular.module('tps-vedlikehold.servicerutine', ['ngMessages'])
    .controller('servicerutineCtrl', ['$scope', 'requestFactory',
        function($scope, requestFactory) {
            $scope.miljo = {
                values:['U', 'T', 'Q', 'P']
            };

            $scope.dato = new Date();

            $scope.fields = requestFactory.getFields();
            //     [
            //     {label:'Fødselsnummer', type: 'text'},
            //     {label:'Dato', type: 'date'},
            //     {label:'Variant', type: 'select', values:['E0', 'E1', 'E2']},
            //     {label:'Miljø', type: 'text'}
            // ];

            $scope.submit = function() {
                //
                console.log("Send til tps pressed med verdier:\n" +
                    $scope.fields[0].value);
            };
        }]);