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

        self.getGruppe = function (id) {
            var defer = $q.defer();
            $http.get(url + '/skd/gruppe/' + id).then(
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
            $http.post(url + '/skd/gruppe', {skdEndringsmeldingGruppe: gruppe}).then(
                function (data) {
                    defer.resolve(data);
                },
                function (error) {
                    defer.reject(error);
                }
            );
            return defer.promise;
        };

        self.deleteMeldinger = function (idListe) {
            var defer = $q.defer();
            $http.post(url + '/skd/deleteMeldinger', {rsMeldingIdListe: idListe}).then(
                function (data) {
                    defer.resolve(data);
                },
                function (error) {
                    defer.reject(error);
                }
            );
            return defer.promise;
        };

        self.deleteGruppe = function (gruppeId) {
            var defer = $q.defer();
            $http.post(url + '/skd/deleteGruppe/' + gruppeId).then(
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
