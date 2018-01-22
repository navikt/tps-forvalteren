angular.module('tps-forvalteren.doedsmeldinger', ['ngMaterial'])
    .controller('SendDoedsmeldingerCtrl', ['$scope', '$mdDialog', '$rootScope', '$stateParams', 'locationService', 'utilsService', 'headerService',
        function ($scope, $mdDialog, $rootScope, $stateParams, locationService, utilsService, headerService) {

            headerService.setHeader('Dødsmelding');








            var init = function() {
                var environments = $scope.$resolve.environmentsPromise;
                if(environments.status !== undefined){
                    utilsService.showAlertError(environments);
                } else {
                    $scope.environments = utilsService.sortEnvironments(environments.environments);
                }
            };

            init();

        }]);