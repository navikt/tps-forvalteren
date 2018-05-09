angular.module('tps-forvalteren.service')
    .service('xmlmeldingService', ['$http', function ($http) {

        var self = this;
        var url = 'api/v1';

        self.hentKoer = function () {
            return $http.get(url + "/queues");
        };

        self.mqDispatcher = function (load, managerName, hostname, port, queueName, channel) {
            return $http.post(url + "/mqdispatch?"
                + "name="+ managerName
                + "&hostname=" + hostname
                + "&port=" + port
                + "&queueName=" +  queueName
                + "&channel=" + channel, load);
        };

        self.hentAppRessurser = function (appnavn) {
            return $http.get(url + "/fasitresources?appNavn=" + appnavn);
        };

        self.send = function (melding) {
            return $http.post(url + "/xmlmelding", melding);
        }

    }]);
