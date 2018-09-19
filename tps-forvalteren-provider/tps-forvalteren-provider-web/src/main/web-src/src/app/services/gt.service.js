angular.module('tps-forvalteren.service')
    .service('gtService', ['$http', function ($http) {

        var self = this;
        var url = 'api/v1/gt';

        self.getKjerneinfo = function (params) {
            return $http.get(url + '/kerninfo', {params: params});
        };

        self.getAdressehistorikk = function (params) {
            return $http.get(url + '/adrhist', {params: params});
        };

        self.getAdresselinjehistorikk = function (params) {
            return $http.get(url + '/adrlinjhist', {params: params});
        };

        self.getSoaihistorikk = function (params) {
            return $http.get(url + '/soaihist', {params: params});
        };
    }]);
