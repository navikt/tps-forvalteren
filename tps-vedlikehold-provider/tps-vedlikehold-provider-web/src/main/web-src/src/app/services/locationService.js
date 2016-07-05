/**
 * @author Frederik de Lichtenberg (Visma Consulting AS).
 */
angular.module('tps-vedlikehold.service')
    .service('locationService', ['$rootScope', '$location', function($rootScope, $location) {

        var self = this;

        var returnUrl = "/dashboard";

        self.updateLoginReturnUrl = function(){
            returnUrl = $location.path();
        };

        self.redirectToLoginReturnUrl = function() {
            $location.path(returnUrl).replace();
        };

        self.redirectToLoginUrl = function() {
            $location.path("/").replace();
        };
    }]);