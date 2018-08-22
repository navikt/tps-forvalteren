angular.module('tps-forvalteren.service')
    .service('gyldigAdresseService', ['$http', '$q', function ($http, $q) {

        this.finnGyldigAdresse = function (gateadresse) {
            var query = createQuery(gateadresse);

            return $http.get('api/v1/gyldigadresse/autocomplete?' + query);
        }

        function createQuery(gateadresse) {
            function appendQueryParamIfDefined(paramName, value) {
                if (typeof value !== 'undefined') {
                    queryParam.push(paramName + "=" + value);
                }
            }

            var queryParam = [];
            appendQueryParamIfDefined("adresseNavnsok", gateadresse.gateadresse);
            appendQueryParamIfDefined("husNrsok", gateadresse.husnummer);
            appendQueryParamIfDefined("kommuneNrsok", gateadresse.kommunenr);
            appendQueryParamIfDefined("postNrsok", gateadresse.postnr);
            queryParam.push("alleSkrivevarianter=J");

            if (typeof gateadresse.postnr === 'undefined') {
                queryParam.push("visPostnr=J");
            } else {
                queryParam.push("visPostnr=N");
            }

            return queryParam.join('&');

        }
    }]);