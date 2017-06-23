angular.module('tps-forvalteren.filter')
    .filter('identStatus', function () {
        return function (text, length, end) {
            var outText;
            switch(text) {
                case 'LOG':
                    outText = 'Ledig og gyldig';
                    break;
                case 'IL':
                    outText = 'Ikke ledig';
                    break;
                case 'IG':
                    outText = 'Ikke gyldig';
                    break;
            }
            return outText;
        };
    });
