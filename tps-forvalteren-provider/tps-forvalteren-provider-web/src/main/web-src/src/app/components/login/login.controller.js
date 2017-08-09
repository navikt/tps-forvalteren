angular.module('tps-forvalteren.login', ['ngMessages'])
    .controller('LoginCtrl', ['$scope', 'authenticationService', 'locationService',
        function($scope, authenticationService, locationService) {

            $scope.title = 'TPS Forvalteren';
            $scope.authenticationError = false;
            $scope.serverError = false;
            $scope.pendingRequest = false;
            $scope.forbiddenError = false;

            var callback = function(authResponse){
                switch(authResponse.status) {
                    case 200:
                        $scope.authenticationError = false;
                        $scope.forbiddenError = false;
                        locationService.redirectToHomeState();
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
                return false;
            };

            function init() {
                authenticationService.authenticate(false, function(authResponse) {
                    if (authResponse.status === 200) {
                        locationService.redirectToHomeState();
                    }
                });
            }

            init();
    }]);
