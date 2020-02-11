angular.module('tps-forvalteren.filter')
    .filter('identtype', function () {

        // Create the return function
        return function (ident) {
            if (ident) {
                return parseInt(ident.substr(0, 1)) > 3 ? 'dnr' :
                    parseInt(ident.substr(2, 1)) > 2 ? 'bost' : 'fnr';
            } else {
                return '';
            }
        };
    });