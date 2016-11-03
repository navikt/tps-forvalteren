/**
 * @author Frederik de Lichtenberg (Visma Consulting AS).
 */
angular.module('tps-vedlikehold.service')
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

    }]);
