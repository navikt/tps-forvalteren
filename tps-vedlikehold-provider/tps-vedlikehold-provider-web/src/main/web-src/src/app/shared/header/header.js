/**
 * @author Frederik de Lichtenberg (Visma Consulting AS).
 * */
angular.module('tps-vedlikehold')
    .controller('headerController', ['$scope', 'authenticationService', 'locationService', '$mdSidenav',
        function($scope, authenticationService, locationService, $mdSidenav) {

    $scope.logout = function(){
        authenticationService.invalidateSession(function(){
            locationService.redirectToLoginState();
        });
    };

    $scope.toggleSideNav = function(menuId) {
        $mdSidenav(menuId).toggle();
    };
}]);
