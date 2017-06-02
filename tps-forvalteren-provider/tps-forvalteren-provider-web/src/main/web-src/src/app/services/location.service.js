
angular.module('tps-forvalteren.service')
    .service('locationService', ['$state', function($state) {

        var self = this;
        var returnState = 'servicerutine';

        self.redirectToLoginReturnState = function() {
            $state.go(returnState);
        };

        self.redirectToLoginState = function() {
            $state.go('login');
        };

        self.redirectToEndringState = function () {
            $state.go('endringer');
        };

        self.redirectToServiceRutineState = function () {
            $state.go('servicerutine');
        };

        self.redirectToGT = function() {
            $state.go("gt");
        };

        self.redirectToVisTestdata = function() {
            $state.go("vis-testdata");
        };


        self.redirectToOpprettTestdata = function() {
            $state.go("opprett-testdata");
        };

        self.isServicerutineState = function(){
            return $state.current.name === 'servicerutine';
        };

    }]);