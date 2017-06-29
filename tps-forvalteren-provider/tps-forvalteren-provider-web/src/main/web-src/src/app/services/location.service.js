
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

        self.redirectToVisTestdata = function(index) {
            $state.go("vis-testdata", {id: index});
        };

        self.redirectToOpprettTestdata = function(index) {
            $state.go("opprett-testdata", {id: index});
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
