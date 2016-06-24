/**
 * Created by G148235 on 24.06.2016.
 */
angular.module('tps-vedlikehold.login', ['ngMessages'])
    .controller('loginCtrl', ['$scope', //, 'authenticationService', 'locationService',
        function($scope) {//, authenticationService, locationService) {

            $scope.title = 'Logg inn - TPS vedlikeholdsklient';

            $scope.authenticationError = false;
            $scope.serverError = false;
            $scope.pendingRequest = false;

            // var callback = function(authResponse){
            //     switch(authResponse.status) {
            //         case 200:
            //             $scope.authenticationError = false;
            //             locationService.redirectToLoginReturnUrl();
            //             break;
            //         case 401:
            //             $scope.authenticationError = true;
            //             break;
            //         default:
            //             $scope.serverError = true;
            //     }
            //     $scope.pendingRequest = false;
            // };
            //
            $scope.login = function() {
                $scope.pendingRequest = true;
                $scope.authenticationError = false;
                $scope.serverError = false;
                console.log("logged in");
                // authenticationService.authenticate($scope.credentials, callback);
            };
    }]);