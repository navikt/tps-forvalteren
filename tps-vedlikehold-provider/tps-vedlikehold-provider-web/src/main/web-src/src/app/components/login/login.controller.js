/**
 * @author Frederik de Lichtenberg (Visma Consulting AS).
 * */
angular.module('tps-vedlikehold.login')
    .controller('LoginCtrl', ['$scope', 'authenticationService', 'locationService',
        function($scope, authenticationService, locationService) {

            $scope.title = 'TPS Vedlikeholdsklient';
            $scope.authenticationError = false;
            $scope.serverError = false;
            $scope.pendingRequest = false;
            $scope.forbiddenError = false;

            var callback = function(authResponse){
                switch(authResponse.status) {
                    case 200:
                        $scope.authenticationError = false;
                        $scope.forbiddenError = false;
                        locationService.redirectToLoginReturnState();
                        break;
                    case 401:
                        $scope.authenticationError = true;
                        break;
                    case 403:
                        $scope.forbiddenError = true;
                        break;
                    default:
                        $scope.serverError = true;
                }
                $scope.pendingRequest = false;
            };

            $scope.login = function() {
                $scope.pendingRequest = true;
                $scope.authenticationError = false;
                $scope.serverError = false;
                authenticationService.authenticate($scope.credentials, callback);
            };

            function init() {
                authenticationService.authenticate(false, function(authResponse) {
                    if (authResponse.status === 200) {
                        locationService.redirectToLoginReturnState();
                    }
                });
            }

            init();
    }]);
