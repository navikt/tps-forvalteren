/**
 * @author Frederik Gørvell (Visma Consulting AS).
 */
angular.module('tps-vedlikehold.service')
    .service('locationService', ['$rootScope', '$location', function($rootScope, $location) {

        var self = this;

        var returnUrl = "/dashboard";

        self.updateLoginReturnUrl = function(){
            returnUrl = $location.path();
        };

        self.redirectToLoginReturnUrl = function() {
            if (returnUrl) {
                $location.path(returnUrl).replace();
            } else {
                $location.path("/").replace();
            }
        };

        self.redirectToLoginUrl = function() {
            $location.path("/").replace();
        };
    }]);