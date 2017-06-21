
angular.module('tps-forvalteren.service')
    .service('testdataService', ['$http', '$q', function($http, $q) {

        var self =  this;
        var url = 'api/v1/testdata/';

        self.getTestpersoner = function(){
            var defer = $q.defer();
            $http.get(url + "personer").then(
                function (data) {
                    defer.resolve(data);
                },
                function (error) {
                    defer.reject(error);
                }
            );
            return defer.promise;
        };

        self.opprettTestpersoner = function(kriterier){
            var defer = $q.defer();
            $http.post(url + "personer", {personKriterierListe: kriterier}).then(
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
            $http.post(url + "deletePersoner", {ids: identer}).then(
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
            $http.post(url + "updatePersoner", personer).then(
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
            $http.post(url + "checkPersoner", identer).then(
                function (data) {
                    defer.resolve(data);
                },
                function (error) {
                    defer.reject(error);
                }
            );
            return defer.promise;
        };

        self.opprettFraListe = function(identer){
            var defer = $q.defer();
            $http.post(url + "createPersoner", identer).then(
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
