/**
 * Created by G148235 on 27.06.2016.
 */
angular.module('tps-vedlikehold')
    .controller('navigatorCtrl', ['$scope', 'sessionService',
        function($scope, sessionService) {
            $scope.currentUser = sessionService.getCurrentUser();
            // $scope.logout = function() {
            //     console.log("logout");
            // };
        }]);