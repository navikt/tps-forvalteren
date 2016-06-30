angular.module('tps-vedlikehold.service')
    .service('authenticationService', ['$http', '$location', 'sessionService', 'utilsService', function($http, $location, sessionService, utilsService) {

        var self = this;

        var loginRoute = 'api/v1/user';
        var logoutRoute = 'api/v1/user/logout';

        self.authenticate = function(credentials, callback) {

            var headers = credentials ?
                utilsService.authHeaders(credentials) : {};

            $http.get(loginRoute, {
                headers: headers
            })
                .then(function (res) {
                        sessionService.setIsAuthenticated(true);
                        sessionService.setIsSignedIn(true);
                        sessionService.setCurrentUser(res.data);
                        sessionService.setToken(res.data.token);

                        if (callback) {
                            callback(res);
                        }
                    },
                    function (res) {

                        if (callback) {
                            callback(res);
                        }
                    });
        };

        self.validateToken = function(callback){
            self.authenticate(false, callback);
        };

        self.invalidateSession = function(callback){
            sessionService.setIsAuthenticated(false);
            sessionService.setIsSignedIn(false);
            sessionService.setToken('');
            $http.post(logoutRoute).then(function(){
                self.authenticate(false, function(res){
                    sessionService.setCurrentUser(res.data);
                    if (callback) {callback(res);}
                });
            }, function(){
                console.log("En feil oppstod ved utlogging");
                self.authenticate(false, function(res){
                    sessionService.setCurrentUser(res.data);
                    if (callback) {callback(res);}
                });
            });
        };
    }]);