
angular.module('tps-vedlikehold.service')
    .service('locationService', ['$state','$location', function($state, $location) {

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

        self.redirectToTestdataState = function () {
            $location.url('testdata/first');
        };

        self.redirectToServiceRutineState = function () {
            $state.go('servicerutine');
        };

        self.isTestdataState = function(){
            return $state.current.name === 'testdata';
        };

        self.isServicerutineState = function(){
            return $state.current.name === 'servicerutine';
        };

    }]);
