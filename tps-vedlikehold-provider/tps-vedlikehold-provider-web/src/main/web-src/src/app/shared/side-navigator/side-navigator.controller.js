/**
 * @author Frederik de Lichtenberg (Visma Consulting AS).
 * */
angular.module('tps-vedlikehold')
    .controller('SideNavigatorCtrl', ['$scope', '$mdDialog', 'serviceRutineFactory', 'serviceRutinesPromise',
        function($scope, $mdDialog, serviceRutineFactory, serviceRutinesPromise) {

        $scope.getServiceRutineInternalName = function(serviceRutinenavn) {
            return serviceRutineFactory.getServiceRutineInternalName(serviceRutinenavn);
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
            if (serviceRutinesPromise) {
                $scope.serviceRutinenavns = serviceRutineFactory.getServiceRutinenavns();
            } else {
                showAlertApiError();
            }
        }
        
        init();
    }]);
