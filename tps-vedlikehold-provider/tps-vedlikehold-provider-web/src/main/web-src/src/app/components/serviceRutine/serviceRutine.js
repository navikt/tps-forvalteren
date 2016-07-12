/**
 * @author Frederik de Lichtenberg (Visma Consulting AS).
 */
angular.module('tps-vedlikehold.servicerutine', ['ngMessages'])
    .controller('servicerutineCtrl', ['$scope', '$stateParams', 'servicerutineFactory',
        function($scope, $stateParams, servicerutineFactory) {

            $scope.formData = {
                fnr:''
            };

            // var servicerutineKode = $stateParams.servicerutineKode;
            
            //requestFactory
            // $scope.miljo = {
            //     values:['U', 'T', 'Q', 'P']
            // };
            
            $scope.formData.dato = new Date();

            // $scope.fields = requestFactory.getFields(servicerutineKode);

            $scope.submit = function() {
                console.log("Send til tps pressed med fnr:\n" +
                    $scope.formData.fnr);

                // requestFactory.getResponse(servicerutineKode, $scope.formData).then(function(res){
                //     //something
                // }, function(error){
                //     //something else
                // });
            };

            function init() {
                $scope.fields = servicerutineFactory.getServicerutineAttributes($stateParams.servicerutineKode);
                $scope.fields.push('aksjonskode');
                $scope.aksjonskoder = servicerutineFactory.getServicerutineAksjonskoder($stateParams.servicerutineKode);
                $scope.miljoer = servicerutineFactory.getMiljoer();
                // console.log('hallo2?');
                // console.log($scope.aksjonskoder);
                // angular.forEach($scope.aksjonskoder, function(value, key, obj) {
                //     console.log(value);
                // });
            }
            init();
            
            // function init() {
            //     requestFactory.getFields($stateParams.servicerutineKode).then(function(res){
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