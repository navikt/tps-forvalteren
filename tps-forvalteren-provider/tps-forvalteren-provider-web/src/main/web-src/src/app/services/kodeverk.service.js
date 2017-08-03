angular.module('tps-forvalteren.service')
    .service('kodeverkService', ['$http', '$q', function($http, $q) {

        var self =  this;
        var url = 'api/v1/kodeverk/';

        self.hentKommuner = function () {
            var defer = $q.defer();
            $http.get(url + 'knr').then(
                function (data) {
                    defer.resolve(data);
                },
                function (error) {
                    defer.reject(error);
                }
            );
            return defer.promise;
        };

        self.hentPoststeder = function () {
            var defer = $q.defer();
            $http.get(url + 'postnummer').then(
                function (data) {
                    defer.resolve(data);
                },
                function (error) {
                    defer.reject(error);
                }
            );
            return defer.promise;
        };
    }]);
