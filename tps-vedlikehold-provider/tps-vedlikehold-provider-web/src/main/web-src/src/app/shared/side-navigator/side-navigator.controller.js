/**
 * @author Frederik de Lichtenberg (Visma Consulting AS).
 * */
angular.module('tps-vedlikehold')
    .controller('SideNavigatorCtrl', ['$scope', '$mdDialog', 'serviceRutineFactory', 'serviceRutinesPromise',
        function($scope, $mdDialog, serviceRutineFactory, serviceRutinesPromise) {

        $scope.getServiceRutineInternalName = function(serviceRutineName) {
            return serviceRutineFactory.getServiceRutineInternalName(serviceRutineName);
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
                $scope.serviceRutineNames = serviceRutineFactory.getServiceRutineNames();
            } else {
                showAlertApiError();
            }
        }
        
        init();
    }]);
