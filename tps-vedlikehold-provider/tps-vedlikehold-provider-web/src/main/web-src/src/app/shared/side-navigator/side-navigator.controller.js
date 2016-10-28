/**
 * @author Frederik de Lichtenberg (Visma Consulting AS).
 * */
angular.module('tps-vedlikehold')
    .controller('SideNavigatorCtrl', ['$scope', '$mdDialog', 'serviceRutineFactory', 'serviceRutinesPromise','endringsmeldingPromise',
        function($scope, $mdDialog, serviceRutineFactory, serviceRutinesPromise, endringsmeldingPromise) {

        $scope.getServiceRutineInternalName = function(serviceRutineName) {
            return serviceRutineFactory.getServiceRutineInternalName(serviceRutineName);
        };

        $scope.getEndringsmeldingInternalName = function(endringsmeldingName){
            return serviceRutineFactory.getEndringsmeldingInternalName(endringsmeldingName);
        };
            
        function showAlertApiError(requestType) {
            $mdDialog.show(
                $mdDialog.alert()
                    .title('Serverfeil')
                    .textContent('Fikk ikke hentet informasjon om '+ requestType +' fra server.')
                    .ariaLabel('Feil ved henting av ' + requestType)
                    .ok('OK')
            );
        }
        
        function init() {
            if (serviceRutinesPromise) {
                $scope.serviceRutineNames = serviceRutineFactory.getServiceRutineNames();
            } else {
                showAlertApiError("servicerutiner");
            }
            if(endringsmeldingPromise){
                $scope.endringsmeldingNames = serviceRutineFactory.getEndringsmeldingerNames();
            } else {
                showAlertApiError("endringsmeldinger");
            }
        }
        
        init();
    }]);
