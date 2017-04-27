
angular.module('tps-forvalteren')
    .controller('HeaderCtrl', ['$scope', 'authenticationService', 'locationService', '$mdSidenav',
        function ($scope, authenticationService, locationService, $mdSidenav) {

            $scope.logout = function () {
                authenticationService.invalidateSession(function () {
                    locationService.redirectToLoginState();
                });
            };

            $scope.toggleSideNav = function (menuId) {
                $mdSidenav(menuId).toggle();
            };

            $scope.endringState = function () {
                locationService.redirectToEndringState();
            };

            $scope.serviceRutineState = function () {
                locationService.redirectToServiceRutineState();
            };

        }]);
