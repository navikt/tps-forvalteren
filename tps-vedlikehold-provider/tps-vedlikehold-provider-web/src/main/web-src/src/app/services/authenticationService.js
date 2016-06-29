angular.module('tps-vedlikehold.service')
    .service('authenticationService', ['$http', '$location', 'sessionService', function($http, $location, sessionService) {

        var self = this;

        var routePrefix = $location.protocol() + '://' + $location.host() + ':' + $location.port();
        var loginRoute = routePrefix + '/api/v1/user';
        var logoutRoute = routePrefix + '/api/v1/user/logout';

        self.authenticate = function(credentials, callback) {

            var headers = credentials ?
            {'Authorization': 'Basic ' + btoa(credentials.username + ":" + credentials.password)} : {};

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

        self.validateToken = function(){
            self.authenticate();
        };


        self.invalidateSession = function(callback){
            sessionService.setIsAuthenticated(false);
            sessionService.setIsSignedIn(false);
            sessionService.setToken('');
            $http.get(logoutRoute).then(function(){
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