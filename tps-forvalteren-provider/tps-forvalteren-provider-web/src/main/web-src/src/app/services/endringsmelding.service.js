angular.module('tps-forvalteren.service')
    .service('endringsmeldingService', ['$http', '$q', function($http, $q) {

        var self =  this;
        var url = 'api/v1/endringsmelding/skd';
        var gruppeCache;

        self.getSkdMeldingsgrupper = function(){
            return $http.get(url + '/grupper');
        };

        self.getGruppe = function (id, fromSource) {
            if (!gruppeCache || fromSource) {
                gruppeCache = $http.get(url + '/gruppe/' + id);
            }
            return gruppeCache;
        };

        self.storeSkdMeldingsgruppe = function (gruppe) {
            return $http.post(url + '/gruppe', gruppe);
        };

        self.deleteMeldinger = function (idListe) {
           return $http.post(url + '/deletemeldinger', {ids: idListe});
        };

        self.deleteGruppe = function (gruppeId) {
            return $http.post(url + '/deletegruppe/' + gruppeId);
        };

        self.createMelding = function (gruppeId, melding) {
            return $http.post(url + '/gruppe/' + gruppeId + (!!melding.raw ? '/raw' : ''), melding);
        };

        self.updateMeldinger = function(meldinger) {
            return $http.post(url + '/updatemeldinger', meldinger);
        };

        self.convertMelding = function(melding) {
            return $http.post(url + '/convertmelding', melding);
        };

        self.sendTilTps = function (gruppeId, miljoe) {
            return $http.post(url + '/tps/' + gruppeId, miljoe);
        };

        self.getInnsendingslogg = function (gruppeId) {
            return $http.get(url + '/gruppe/' + gruppeId + '/tpslogg');
        };
    }]);
