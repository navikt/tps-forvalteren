angular.module('tps-forvalteren.service')
    .service('endringsmeldingService', ['$http', '$q', function($http, $q) {

        var self =  this;
        var url = 'api/v1/endringsmelding/skd';

        self.getSkdMeldingsgrupper = function(){
            var defer = $q.defer();
            $http.get(url + '/grupper').then(
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
            $http.get(url + '/gruppe/' + id).then(
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
            $http.post(url + '/gruppe', gruppe).then(
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
            $http.post(url + '/deletemeldinger', {ids: idListe}).then(
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
            $http.post(url + '/deletegruppe/' + gruppeId).then(
                function (data) {
                    defer.resolve(data);
                },
                function (error) {
                    defer.reject(error);
                }
            );
            return defer.promise;
        };

        self.createMelding = function (gruppeId, melding) {
            var defer = $q.defer();
            $http.post(url + '/gruppe/' + gruppeId + (!!melding.raw ? '/raw' : ''), melding).then(
                function (data) {
                    defer.resolve(data);
                },
                function (error) {
                    defer.reject(error);
                }
            );
            return defer.promise;
        };

        self.updateMeldinger = function(meldinger) {
            var defer = $q.defer();
            $http.post(url + '/updatemeldinger', meldinger).then(
                function (data) {
                    defer.resolve(data);
                },
                function (error) {
                    defer.reject(error);
                }
            );
            return defer.promise;
        };

        self.convertMelding = function(melding) {
            var defer = $q.defer();
            $http.post(url + '/convertmelding', melding).then(
                function (data) {
                    defer.resolve(data);
                },
                function (error) {
                    defer.reject(error);
                }
            );
            return defer.promise;
        };

        self.sendTilTps = function (gruppeId, miljoe) {
            var defer = $q.defer();
            $http.post(url + '/tps/' + gruppeId, miljoe).then(
                function (data) {
                    defer.resolve(data);
                },
                function (error) {
                    defer.reject(error);
                }
            );
            return defer.promise;
        };

        self.getInnsendingslogg = function (gruppeId) {
            var defer = $q.defer();
            $http.get(url + '/gruppe/' + gruppeId + '/tpslogg').then(
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
