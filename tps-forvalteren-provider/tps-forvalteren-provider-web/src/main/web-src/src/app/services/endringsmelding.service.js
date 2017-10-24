angular.module('tps-forvalteren.service')
    .service('endringsmeldingService', ['$http', '$q', function($http, $q) {

        var self =  this;
        var url = 'api/v1/endringsmelding';

        self.getSkdMeldingsgrupper = function(){
            var defer = $q.defer();
            $http.get(url + '/skd/grupper').then(
                function (data) {
                    defer.resolve(data);
                },
                function (error) {
                    defer.reject(error);
                }
            );
            return defer.promise;
        };

        self.storeSkdMeldingsgruppe = function (gruppe) {
            var defer = $q.defer();
            $http.post(url + '/skd/gruppe', {SkdEndringsmeldingGruppe: gruppe}).then(
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
