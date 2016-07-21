/**
 * @author Frederik de Lichtenberg (Visma Consulting AS).
 * */
angular.module('tps-vedlikehold')
    .controller('navigatorController', ['$scope', '$mdDialog', 'servicerutineFactory', 'servicerutinerPromise', 
        function($scope, $mdDialog, servicerutineFactory, servicerutinerPromise) {

        $scope.getServicerutineInternalName = function(serviceRutinenavn) {
            return servicerutineFactory.getServicerutineInternalName(serviceRutinenavn);
        };
            
        function showAlertApiError() {
            $mdDialog.show(
                $mdDialog.alert()
                    .title('Serverfeil')
                    .textContent('Fikk ikke hentet informasjon om servicerutiner fra server.')
                    .ariaLabel('Feil ved henting av servicerutiner')
                    .ok('OK')
            );
        }
        
        function init() {
            if (servicerutinerPromise) {
                $scope.serviceRutinenavns = servicerutineFactory.getServiceRutinenavns();
            } else {
                showAlertApiError();
            }
        }
        
        init();
    }]);
