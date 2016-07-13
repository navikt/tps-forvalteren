/**
 * @author Frederik de Lichtenberg (Visma Consulting AS).
 * */
angular.module('tps-vedlikehold').controller('headerController', ['$scope', '$location', 'authenticationService', '$mdSidenav', function($scope, $location, authenticationService, $mdSidenav) {

    $scope.logout = function(){
        authenticationService.invalidateSession(function(){
            console.log("Logger ut");
            $location.path('/');
        });
    };

    $scope.toggleSideNav = function(menuId) {
        $mdSidenav(menuId).toggle();
    };
}]);
