angular.module('tps-forvalteren.service')
    .service('testdataService', ['$http', '$q', function($http, $q) {

        var self =  this;
        var url = 'api/v1/testdata/';

        var gruppeCache;

        self.getGruppe = function(id, fromSource){
            if (!gruppeCache || fromSource) {
                gruppeCache = $http.get(url + 'gruppe/' + id);
            }
            return gruppeCache;
        };

        self.opprettTestpersoner = function(gruppeId, kriterier, withAdresse){
            return $http.post(url + 'personer/' + gruppeId, {personKriterierListe: kriterier, withAdresse: withAdresse});
        };

        self.sletteTestpersoner = function(identer){
            return $http.post(url + 'deletepersoner', {ids: identer});
        };

        self.oppdaterTestpersoner = function(personer){
            return $http.post(url + 'updatepersoner', personer);
        };

        self.validerListe = function(identer){
            return $http.post(url + 'checkpersoner', identer);
        };

        self.opprettFraListe = function(gruppeId, identer){
            return $http.post(url + 'createpersoner/' + gruppeId, identer);
        };

        self.hentTestgrupper = function () {
            return $http.get(url + 'grupper');
        };

        self.lagreTestgruppe = function (gruppe) {
            return $http.post(url + 'gruppe', gruppe);
        };

        self.sletteTestgruppe = function (gruppeId) {
            return $http.post(url + 'deletegruppe/' + gruppeId);
        };

        self.sendTilTps = function (gruppeId, miljoer) {
            return $http.post(url + 'tps/' + gruppeId, miljoer);
        };
        
        self.opprettSkdEndringsmeldingGruppe = function (gruppeId) {
            return $http.get(url + "skd/" + gruppeId);
        }
        
    }]);
