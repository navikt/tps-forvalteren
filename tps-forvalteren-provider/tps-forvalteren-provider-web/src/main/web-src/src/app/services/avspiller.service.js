angular.module('tps-forvalteren.service')
    .service('avspillerService', ['$http', function ($http) {

        var self = this;
        var url = 'api/v1/avspiller';

        self.getTyperOgKilder = function (request) {
            return $http.get(url + '/meldingstyper' + prepareBasicParams(request))
                .then(function (response) {
                    return response.data;
                });
        };

        self.getMeldinger = function (request) {
            return $http.get(url + '/meldinger' + prepareBasicParams(request) + prepareOptionalParams(request) + prepareBufferParam(request))
                .then(function (response) {
                    return response.data;
                });
        };

        self.sendMeldinger = function (request, target) {
            return $http.post(url + '/meldinger', {
                miljoeFra: request.miljoe,
                datoFra: date2String(request.periodeFra),
                datoTil: date2String(request.periodeTil),
                format: target.format,
                typer: request.typer,
                kilder: request.kilder,
                identer: request.identer ? request.identer.split(/[\W\s]+/) : undefined,
                miljoeTil: target.miljoe,
                queue: target.messageQueue ? target.messageQueue : target.privateQueue,
                ownQueue: !!target.privateQueue
            })
                .then(function (response) {
                    return response.data;
                });
        };

        self.getMeldingskoer = function (request) {
            return $http.get(url + '/meldingskoer?miljoe=' + request.miljoe + '&format=' + request.format)
                .then(function (response) {
                    return response.data;
                })
        };

        self.getStatus = function (bestillingId) {
            return $http.get(url + '/statuser?bestilling=' + bestillingId)
                .then(function (response) {
                    return response.data;
                })
        };

        self.getMelding = function (request) {
            return $http.get(url + '/melding?miljoe=' + request.miljoe + '&format=' + request.format + '&meldingnr=' + request.meldingnr)
                .then(function (response) {
                    return response.data;
                })
        };

        self.cancelSendMeldinger = function (request) {
            return $http.delete(url + '/meldinger?bestillingId=' + request)
                .then(function (response) {
                    return response.data;
                })
        };

        function prepareBasicParams(request) {
            return '?miljoe=' + request.miljoe +
                '&format=' + request.format +
                '&periode=' + (request.periodeFra ? date2String(request.periodeFra) : '') +
                '$' + (request.periodeTil ? date2String(request.periodeTil) : '') +
                '$' + request.timeout;
        }

        function prepareOptionalParams(request) {
            return (request.typer ? '&typer=' + request.typer.join(",") : '') +
                (request.kilder ? '&kilder=' + request.kilder.join(",") : '') +
                (request.identer ? '&identer=' + request.identer.split(/[\W\s]+/).join(',') : '');
        }

        function prepareBufferParam(request) {
            return request.buffersize ? '&buffer=' + request.buffernumber + '$' + request.buffersize : '';
        }

        function date2String(dato) {
            return dato ? (dato.getFullYear().toString() + '-' + padWithZero(dato.getMonth() + 1) + '-' + padWithZero(dato.getDate()) + 'T' +
                padWithZero(dato.getHours()) + ':' + padWithZero(dato.getMinutes()) + ':' + padWithZero(dato.getSeconds())) : null;
        }

        function padWithZero(nummer) {
            return (nummer.toString().length == 1 ? "0" : "") + nummer;
        }
    }]);
