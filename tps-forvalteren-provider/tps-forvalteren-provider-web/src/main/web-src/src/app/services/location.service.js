
angular.module('tps-forvalteren.service')
    .service('locationService', ['$state', '$location', function($state, $location) {

        var self = this;
        var returnState = 'root';

        self.redirectToHomeState = function() {
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
            $state.go("vis-testdata", {gruppeId: index});
        };

        self.redirectToOpprettTestdata = function(index) {
            $state.go("opprett-testdata", {gruppeId: index});
        };

        self.isServicerutineState = function(){
            return $state.current.name === 'servicerutine';
        };

        self.redirectUrl = function(url, param) {
            if ('/' === url) {
                self.redirectToHomeState();
            } else {
                if (url.indexOf('/:') !== -1) {
                    $state.go(url.substr(1).replace(/\/:\s*\S*/, ''), param);
                }
                else {
                    $state.go(url.substr(1)); // Ta bort ledende "/"}
                }
            }
        };

        self.isRoot = function () {
            return $location.url() === '/';
        };
    }]);