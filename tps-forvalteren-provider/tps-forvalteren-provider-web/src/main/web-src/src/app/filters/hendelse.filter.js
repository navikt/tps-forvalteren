angular.module('tps-forvalteren.filter')
    .filter('hendelse', function () {

        return function (hendelse) {

            return hendelse.substring(0, 30);
        };
    });
