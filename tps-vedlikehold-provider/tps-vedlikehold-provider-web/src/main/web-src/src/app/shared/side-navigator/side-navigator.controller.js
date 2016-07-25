/**
 * @author Frederik de Lichtenberg (Visma Consulting AS).
 * */
angular.module('tps-vedlikehold')
    .controller('SideNavigatorCtrl', ['$scope', '$mdDialog', 'serviceRutineFactory', 'servicerutinerPromise',
        function($scope, $mdDialog, serviceRutineFactory, servicerutinerPromise) {

        $scope.getServicerutineInternalName = function(serviceRutinenavn) {
            return serviceRutineFactory.getServicerutineInternalName(serviceRutinenavn);
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
                $scope.serviceRutinenavns = serviceRutineFactory.getServiceRutinenavns();
            } else {
                showAlertApiError();
            }
        }
        
        init();
    }]);
