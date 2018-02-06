angular.module('tps-forvalteren.service')
    .service('doedsmeldingService', ['$http', function ($http) {

        var self = this;
        var url = 'api/v1/doedsmelding/';

        self.opprett = function (melding) {
            return $http.post(url + "opprett/", melding);
        };

        self.slett = function (ident) {
            return $http.post(url + "delete/" + ident);
        };

        self.endre = function (melding) {
            return $http.post(url + 'edit', melding);
        };

        self.hent = function () {
            return $http.get(url + "meldinger");
        };

        self.sendSkjema = function (meldinger) {
            return $http.post(url + 'send', meldinger);
        };

        self.toemSkjema = function (miljoe) {
            return $http.post(url + "clearskjema/" + miljoe);
        };

        self.sjekkgyldig = function (personer) {
            return $http.post(url + 'checkpersoner', personer);
        };
    }]);
