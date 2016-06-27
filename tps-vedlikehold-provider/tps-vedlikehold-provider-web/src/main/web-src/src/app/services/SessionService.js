angular.module('tps-vedlikehold.service')
    .service('sessionService', ['$rootScope', function($rootScope) {

        var self =  this;
        var token = '';

        self.setIsAuthenticated = function(val){
            $rootScope.authenticated = val;
        };

        self.isAuthorized = function(authorizedRoles) {
            if(!angular.isArray(authorizedRoles)){
                authorizedRoles = [authorizedRoles];
            }
            angular.forEach(self.getCurrentUser().roles, function(role) {
                if (authorizedRoles.indexOf(role) !== -1 ) {
                    return true;
                }
                return false;
            });
            return self.getIsAuthenticated() && authorizedRoles.indexOf();
        };

        self.getIsAuthenticated = function(){
            return $rootScope.authenticated;
        };

        self.setIsSignedIn = function(val){
            $rootScope.signedIn = val;
        };

        self.getIsSignedIn = function(){
            return $rootScope.signedIn;
        };

        self.setToken = function(val) {
            token = val;
        };

        self.getToken = function(){
            return token;
        };

        self.setCurrentUser = function(user) {
            $rootScope.currentUser = {
                username: user.username,
                name: user.name,
                roles: user.roles
            };
        };

        self.getCurrentUser = function(){
            return $rootScope.currentUser;
        };

    }]);
