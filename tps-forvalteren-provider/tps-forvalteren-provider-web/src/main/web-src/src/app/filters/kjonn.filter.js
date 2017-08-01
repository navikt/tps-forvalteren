angular.module('tps-forvalteren.filter')
    .filter('kjonn', function() {

    // Create the return function
    return function (kjonn) {
        if (kjonn) {
            return kjonn == 'K' ? 'Kvinne' : 'Mann';
        } else {
            return '';
        }
    };
});