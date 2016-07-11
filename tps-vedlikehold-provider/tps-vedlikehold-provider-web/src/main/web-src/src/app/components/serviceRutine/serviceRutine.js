/**
 * @author Frederik de Lichtenberg (Visma Consulting AS).
 */
angular.module('tps-vedlikehold.servicerutine', ['ngMessages'])
    .controller('servicerutineCtrl', ['$scope', '$stateParams', 'requestFactory',
        function($scope, $stateParams, requestFactory) {

            $scope.formData = {};

            var servicerutineKode = $stateParams.serviceRutineNr;
            
            //requestFactory
            $scope.miljo = {
                values:['U', 'T', 'Q', 'P']
            };

            $scope.formData.dato = new Date();

            $scope.fields = requestFactory.getFields(servicerutineKode);

            $scope.submit = function() {
                console.log("Send til tps pressed med verdier:\n" +
                    $scope.fields[0].value);

                // requestFactory.getResponse(servicerutineKode, $scope.formData).then(function(res){
                //     //something
                // }, function(error){
                //     //something else
                // });
            };

            
            // function init() {
            //     requestFactory.getFields($stateParams.serviceRutineNr).then(function(res){
            //         $scope.fields = res.data; //?
            //     }, function (error) {
            //         //did not get fields from api
            //     });
            //
            //     requestFactory.getMiljo().then(function(res){
            //         $scope.miljo = res.data;
            //     }, function(error){
            //         //did not get miljo from api
            //     });
            //
            //     $scope.formData.dato = new Date();
            // }
            // init();
            

        }]);