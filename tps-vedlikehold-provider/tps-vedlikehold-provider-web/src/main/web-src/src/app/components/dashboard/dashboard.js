/**
 * Created by K148233 on 24.06.2016.
 */
angular.module('tps-vedlikehold.dashboard', ['ngMessages'])
    .controller('dashboardCtrl', ['$scope', 'sessionService',
        function($scope, sessionService) {
            // $scope.currentUser = sessionService.getCurrentUser();
            // $scope.logout = function() {
            //     console.log("logout");
            // };
            $scope.fNr = '';
            $scope.myDate = new Date();
            $scope.variant = '';
            $scope.varianter = ['E0 - Sikkerhetstiltak', 'E1', 'E2'];
    }]);