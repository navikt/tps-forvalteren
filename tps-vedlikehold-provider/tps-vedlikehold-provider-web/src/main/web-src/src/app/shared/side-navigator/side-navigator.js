/**
 * @author Frederik de Lichtenberg (Visma Consulting AS).
 * */
angular.module('tps-vedlikehold')
    .controller('navigatorCtrl', ['$scope', 'servicerutineFactory', function($scope, servicerutineFactory) {
        
        function init() {
            if(servicerutineFactory.fetchServicerutiner()) { //HVOR BURDE DETTE KALLET GJØRES?
                // IKKE HER HVERTFALL
                //success, data in servicerutineFactory
                $scope.servicerutiner = servicerutineFactory.getServicerutineNames();
            }
            else {
                //failure
            }
            
            if(servicerutineFactory.fetchEnvironments()) { //HVOR BURDE DETTE KALLET GJØRES?
                //success
            }
            else {
                //failure
            }
            
        }
        init();


    }]);
