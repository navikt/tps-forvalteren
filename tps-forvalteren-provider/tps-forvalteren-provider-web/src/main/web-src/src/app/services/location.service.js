
angular.module('tps-forvalteren.service')
    .service('locationService', ['$state', '$location', function($state, $location) {

        var self = this;
        var returnState = 'root';

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

        self.redirectToTestgruppe = function() {
            $state.go("testgruppe");
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

        self.redirectUrl = function(url) {
            $state.go(url.substring(1)); // Ta bort ledende "/"
        };

        self.isRoot = function () {
            return $location.url() == '/';
        };
    }]);
