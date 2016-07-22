/**
 * @author Frederik de Lichtenberg (Visma Consulting AS).
 * */
angular.module('tps-vedlikehold.login', ['ngMessages'])
    .controller('loginController', ['$scope', 'authenticationService', 'locationService',
        function($scope, authenticationService, locationService) {

            $scope.title = 'TPS Vedlikeholdsklient';

            $scope.authenticationError = false;
            $scope.serverError = false;
            $scope.pendingRequest = false;

            var callback = function(authResponse){
                switch(authResponse.status) {
                    case 200:
                        $scope.authenticationError = false;
                        locationService.redirectToLoginReturnState();
                        break;
                    case 401:
                        $scope.authenticationError = true;
                        break;
                    default:
                        $scope.serverError = true;
                }
                $scope.pendingRequest = false;
            };

            authenticationService.authenticate(false, function(authResponse) {
                if (authResponse.status === 200) {
                    locationService.redirectToLoginReturnState();
                }
            });

            $scope.login = function() {
                $scope.pendingRequest = true;
                $scope.authenticationError = false;
                $scope.serverError = false;
                authenticationService.authenticate($scope.credentials, callback);
            };
    }]);
