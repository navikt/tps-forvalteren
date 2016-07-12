/**
 * @author Frederik de Lichtenberg (Visma Consulting AS).
 */
angular.module('tps-vedlikehold.servicerutine', ['ngMessages'])
    .controller('servicerutineCtrl', ['$scope', '$stateParams', 'servicerutineFactory',
        function($scope, $stateParams, servicerutineFactory) {

            $scope.formData = {
                fnr:''
            };
            
            $scope.environment = ''; // 

            // var servicerutineCode = $stateParams.servicerutineCode;
            
            $scope.formData.aksjonsDato = new Date();

            $scope.submit = function() {
                console.log("Send til tps pressed med fnr:\n" +
                    $scope.formData.fnr);

                // requestFactory.getResponse(servicerutineCode, $scope.formData).then(function(res){
                //     //something
                // }, function(error){
                //     //something else
                // });
            };

            function init() {
                $scope.fields = servicerutineFactory.getServicerutineAttributes($stateParams.servicerutineCode);
                $scope.fields.push('aksjonskode');
                $scope.aksjonskoder = servicerutineFactory.getServicerutineAksjonskoder($stateParams.servicerutineCode);
                $scope.environments = servicerutineFactory.getEnvironments();
            }
            init();
        }]);
