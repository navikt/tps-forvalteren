angular.module('tps-forvalteren.filter')
    .filter('doedshandling', function() {

    // Create the return function
    return function (handling) {
        if (handling) {
            switch (handling) {
                case 'C':
                    return 'Sette dødsdato';
                case 'U':
                    return 'Endre dødsdato';
                case 'D':
                    return 'Slette dødsdato';
                default:
                    return '';
            }
        } else {
            return '';
        }
    };
});