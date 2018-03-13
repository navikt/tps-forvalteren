angular.module('tps-forvalteren.service')
    .service('XmlmeldingService', ['$http', function ($http) {

        var self = this;
        var url = 'api/v1/'; //Skal endres.

        self.hentKoer = function () {
            return $http.get(url + "/queues");
        };

        self.send = function () {
            return $http.post(url + "/xmlmelding", melding);
        }

    }]);
