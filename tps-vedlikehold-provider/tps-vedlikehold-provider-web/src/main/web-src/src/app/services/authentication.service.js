/**
 * @author Kristian Kyvik (Visma Consulting AS).
 */
angular.module('tps-vedlikehold.service')
    .service('authenticationService', ['$http', '$q', '$location', 'sessionService', 'utilsService', 'locationService',
        function($http, $q, $location,  sessionService, utilsService, locationService) {

        var self = this;

        var loginRoute = 'api/v1/user';
        var logoutRoute = 'api/v1/user/logout';

        var setupSession = function(res) {
            sessionService.setIsAuthenticated(true);
            sessionService.setIsSignedIn(true);
            sessionService.setCurrentUser(res.data);
        };

        self.authenticate = function(credentials, callback) {
            var headers = credentials ?
                utilsService.authHeaders(credentials) : {};

            $http.get(loginRoute, {
                headers: headers
            })
                .then(function (res) {
                    setupSession(res);

                    if (callback) {
                        callback(res);
                    }
                }, function (res) {
                    if (callback) {
                        callback(res);
                    }
                });
        };

        self.loadUser = function() {
            var defer = $q.defer();

            if (!sessionService.getIsAuthenticated()) {
                $http.get(loginRoute, {
                    headers: {}
                })
                .then(function (res) {
                    setupSession(res);
                    defer.resolve();
                }, function (res) {
                    locationService.redirectToLoginState();
                    defer.reject();
                });
            } else {
                defer.resolve();
            }

            return defer.promise;
        };

        self.invalidateSession = function(callback){
            sessionService.setIsAuthenticated(false);
            sessionService.setIsSignedIn(false);

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
