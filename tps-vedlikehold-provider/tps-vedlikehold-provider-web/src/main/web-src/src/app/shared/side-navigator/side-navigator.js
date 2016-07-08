/**
 * @author Frederik de Lichtenberg (Visma Consulting AS).
 * */
angular.module('tps-vedlikehold')
    .controller('navigatorCtrl', ['$scope', function($scope) {
            $scope.serviceRoutines = [
                {code: "S004", name: "Hent personopplysninger"},
                {code: "S322", name: "Test"}
            ];

        $scope.getRequestForm = function(serviceCode) {
            // console.log(serviceCode);
        };
    }]);
