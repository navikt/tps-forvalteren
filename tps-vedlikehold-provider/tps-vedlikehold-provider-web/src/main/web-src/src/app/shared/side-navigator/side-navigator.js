/**
 * @author Frederik de Lichtenberg (Visma Consulting AS).
 * */
angular.module('tps-vedlikehold')
    .controller('navigatorCtrl', ['$scope', 'servicerutineFactory', function($scope, servicerutineFactory) {
        $scope.serviceRoutines = [
            {code: "S004", name: "Hent personopplysninger"},
            {code: "S322", name: "Test"}
        ];

        $scope.getRequestForm = function(serviceCode) {
            // console.log(serviceCode);
        };

        

        function init() {
            if(servicerutineFactory.fetchServicerutiner()) { //HVOR BURDE DETTE KALLET GJØRES?
                //success, data in servicerutineFactory
                $scope.servicerutiner = servicerutineFactory.getServicerutineNames();
                // angular.forEach($scope.servicerutiner, function(value, key, obj) {
                //     console.log(value);
                // });
            }
            else {
                //failure
            }
            
            if(servicerutineFactory.fetchMiljoer()) { //HVOR BURDE DETTE KALLET GJØRES?
                //success
            }
            else {
                //failure
            }
            
        }
        init();


    }]);
