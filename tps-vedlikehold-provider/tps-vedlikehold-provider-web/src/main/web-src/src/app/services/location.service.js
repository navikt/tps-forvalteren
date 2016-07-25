/**
 * @author Frederik de Lichtenberg (Visma Consulting AS).
 */
angular.module('tps-vedlikehold.service')
    .service('locationService', ['$rootScope', '$state', function($rootScope, $state) {

        var self = this;
        var returnState = 'serviceRutine';

        self.redirectToLoginReturnState = function() {
            $state.go(returnState);
        };

        self.redirectToLoginState = function() {
            $state.go('login');
        };
    }]);
