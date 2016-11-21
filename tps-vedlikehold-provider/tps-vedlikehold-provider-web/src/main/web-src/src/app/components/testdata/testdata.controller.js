/**
 * Rizwan Ali Ahmed. Visma Consulting AS
 * */
angular.module('tps-vedlikehold.testdata')
    .controller('TestDataCtrl', ['$scope', 'authenticationService', 'locationService', '$mdSidenav',
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
