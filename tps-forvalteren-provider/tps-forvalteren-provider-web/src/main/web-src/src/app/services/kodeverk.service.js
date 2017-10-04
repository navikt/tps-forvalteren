angular.module('tps-forvalteren.service')
    .service('kodeverkService', ['$http', '$q', function($http, $q) {

        var self =  this;
        var url = 'api/v1/kodeverk/';

        self.hentKommuner = function () {
            var defer = $q.defer();
            $http({cache: true, url: url + 'knr', method: 'GET'}).then(
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
            $http({cache: true, url: url + 'postnummer', method: 'GET'}).then(
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
