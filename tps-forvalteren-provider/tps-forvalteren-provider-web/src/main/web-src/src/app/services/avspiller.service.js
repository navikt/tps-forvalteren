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

        self.getMeldingskoer = function (request) {
            return $http.get(url + '/meldingskoer?miljoe=' + request.miljoe + '&format=' + request.format)
                .then(function (response) {
                    return response.data;
                })
        };

        var prepareBasicParams = function (request) {
            return '?miljoe=' + request.miljoe +
                '&format=' + request.format +
                (request.periodeFra ? '&periode=' + date2String(request.periodeFra) + '$' + date2String(request.periodeTil) : '');
        };

        var prepareOptionalParams = function (request) {
            return (request.typer ? '&typer=' + request.typer.join(",") : '') +
                (request.kilder ? '&kilder=' + request.kilder.join(",") : '') +
                (request.identer ? '&identer=' + request.identer.split(/[\W\s]+/).join(',') : '');
        };

        var prepareBufferParam = function (request) {
            return request.buffersize ? '&buffer=' + request.buffersize + '$' + request.buffernumber : '';
        };

        var date2String = function (dato) {
            return dato ? dato.getFullYear().toString() + '-' + padWithZero(dato.getMonth() + 1) + '-' + padWithZero(dato.getDay()) : null;
        };

        var padWithZero = function (nummer) {
            return (nummer.toString().length == 1 ? "0" : "") + nummer;
        }
    }]);
