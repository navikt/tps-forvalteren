angular.module('tps-vedlikehold.service')
    .service('sessionService', ['$rootScope', function($rootScope) {

        var self =  this;
        var token = '';

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
        
        self.setToken = function(val) {
            token = val;
        };

        self.getToken = function(){
            return token;
        };

        self.setCurrentUser = function(user) {
            $rootScope.currentUser = {
                brukernavn: user.brukernavn,
                navn: user.navn,
                roller: user.roller
            };
        };

        self.getCurrentUser = function(){
            return $rootScope.currentUser;
        };

    }]);
