
angular.module('tps-forvalteren.service')
    .service('testdataService', ['$http', '$q', function($http, $q) {

        var self =  this;
        var url = 'api/v1/testdata/';

        self.getTestpersoner = function(id){
            var defer = $q.defer();
            $http.get(url + 'gruppe/' + id).then(
                function (data) {
                    defer.resolve(data);
                },
                function (error) {
                    defer.reject(error);
                }
            );
            return defer.promise;
        };

        self.opprettTestpersoner = function(gruppeId, kriterier){
            var defer = $q.defer();
            $http.post(url + 'personer/' + gruppeId, {personKriterierListe: kriterier}).then(
                function (data) {
                    defer.resolve(data);
                },
                function (error) {
                    defer.reject(error);
                }
            );
            return defer.promise;
        };

        self.sletteTestpersoner = function(identer){
            var defer = $q.defer();
            $http.post(url + 'deletePersoner', {ids: identer}).then(
                function (data) {
                    defer.resolve(data);
                },
                function (error) {
                    defer.reject(error);
                }
            );
            return defer.promise;
        };

        self.oppdaterTestpersoner = function(personer){
            var defer = $q.defer();
            $http.post(url + 'updatePersoner', personer).then(
                function (data) {
                    defer.resolve(data);
                },
                function (error) {
                    defer.reject(error);
                }
            );
            return defer.promise;
        };

        self.validerListe = function(identer){
            var defer = $q.defer();
            $http.post(url + 'checkPersoner', identer).then(
                function (data) {
                    defer.resolve(data);
                },
                function (error) {
                    defer.reject(error);
                }
            );
            return defer.promise;
        };

        self.opprettFraListe = function(gruppeId, identer){
            var defer = $q.defer();
            $http.post(url + 'createPersoner/' + gruppeId, identer).then(
                function (data) {
                    defer.resolve(data);
                },
                function (error) {
                    defer.reject(error);
                }
            );
            return defer.promise;
        };

        self.hentTestgrupper = function () {
            var defer = $q.defer();
            $http.get(url + 'grupper').then(
                function (data) {
                    defer.resolve(data);
                },
                function (error) {
                    defer.reject(error);
                }
            );
            return defer.promise;
        };

        self.lagreTestgruppe = function (gruppe) {
            var defer = $q.defer();
            $http.post(url + 'gruppe', gruppe).then(
                function (data) {
                    defer.resolve(data);
                },
                function (error) {
                    defer.reject(error);
                }
            );
            return defer.promise;
        }

        self.tempdata = function () {
            var defer = $q.defer();
            $http.get(url + 'tempdata').then(
                function (data) {
                    defer.resolve(data);
                },
                function (error) {
                    defer.reject(error);
                }
            );
            return defer.promise;
        }
    }]);
