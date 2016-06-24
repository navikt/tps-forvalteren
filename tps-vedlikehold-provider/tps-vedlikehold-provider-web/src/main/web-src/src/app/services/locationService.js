/**
 * Created by G148235 on 24.06.2016.
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
    }]);