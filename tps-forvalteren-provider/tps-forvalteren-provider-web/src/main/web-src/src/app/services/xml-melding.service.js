angular.module('tps-forvalteren.service')
    .service('xmlmeldingService', ['$http', function ($http) {

        var self = this;
        var url = 'api/v1';

        self.hentKoer = function () {
            return $http.get(url + "/queues");
        };

        self.send = function (melding) {
            return $http.post(url + "/endringsmelding/skd/xmlmelding", melding);
        }

    }]);
