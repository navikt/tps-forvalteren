angular.module('tps-forvalteren.service')
    .service('doedsmeldingService', ['$http', '$q', function ($http, $q) {

        var self = this;
        var url = 'api/v1/doedsmelding/';

        self.opprett = function (melding) {
            return $http.post(url, melding);
        };

        self.slett = function (ident) {
            return $http.post(url + "delete/" + ident);
        };

        self.endre = function (melding) {
            return $http.post(url + 'edit', melding);
        };

        self.hent = function () {
            return $http.get(url);
        };

        self.sendSkjema = function (meldinger) {
            return $http.post(url + 'send', meldinger);
        };

        self.toemSkjema = function () {
            return $http.post(url + "delete");
        }
    }]);
