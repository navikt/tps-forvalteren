
angular.module('tps-forvalteren.service')
    .service('sessionService', ['$rootScope', function($rootScope) {

        var self =  this;

        self.setIsAuthenticated = function(val){
            $rootScope.authenticated = val;
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
