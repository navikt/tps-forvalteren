/**
 * @author Frederik de Lichtenberg (Visma Consulting AS).
 * */
angular.module('tps-vedlikehold')
    .controller('navigatorCtrl', ['$scope', function($scope) {
            $scope.serviceRoutines = [
                {code: "S004",
                name: "Hent personopplysninger"}
            ];

        $scope.getRequestForm = function(serviceCode) {
            console.log(serviceCode);
        };
        
        // $scope.fields = [
        //     {label:'Fødselsnummer', type: 'text'},
        //     {label:'Dato', type: 'datepicker'}
        // ];
    }]);