/**
 * @author Frederik de Lichtenberg (Visma Consulting AS).
 * */
angular.module('tps-vedlikehold')
    .controller('SideNavigatorCtrl', ['$scope', '$mdDialog', '$state', 'serviceRutineFactory', 'serviceRutinesPromise', 'locationService',
        function($scope, $mdDialog,$state, serviceRutineFactory, serviceRutinesPromise, locationService) {


        $scope.getServiceRutineInternalName = function(serviceRutineName) {
            return serviceRutineFactory.getServiceRutineInternalName(serviceRutineName);
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

            $scope.isTestdataState = function () {
                return locationService.isTestdataState();
            };

            $scope.isServicerutineState = function () {
                return locationService.isServicerutineState();
            };

        function init() {
            switch ($state.current.name){
                case 'servicerutine':
                    if (serviceRutinesPromise ){
                        $scope.serviceRutineNames = serviceRutineFactory.getServiceRutineNames();
                    } else {
                        showAlertApiError("servicerutiner");
                    }
                    break;
                case 'testdata':
                    $scope.testdataNames = ["first","second"];      //TODO Kun for testing.
                    break;
                default:
                    break;
            }
        }
        
        init();
    }]);
