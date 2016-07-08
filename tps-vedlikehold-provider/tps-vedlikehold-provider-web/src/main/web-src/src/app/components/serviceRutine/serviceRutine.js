/**
 * @author Frederik de Lichtenberg (Visma Consulting AS).
 */
angular.module('tps-vedlikehold.servicerutine', ['ngMessages'])
    .controller('servicerutineCtrl', ['$scope', '$stateParams', 'requestFactory',
        function($scope, $stateParams, requestFactory) {

            var servicerutineKode = $stateParams.serviceRutineNr;
            
            //requestFactory
            $scope.miljo = {
                values:['U', 'T', 'Q', 'P']
            };

            $scope.dato = new Date();

            $scope.fields = requestFactory.getFields(servicerutineKode);

            $scope.submit = function() {
                //
                console.log("Send til tps pressed med verdier:\n" +
                    $scope.fields[0].value);
            };
        }]);