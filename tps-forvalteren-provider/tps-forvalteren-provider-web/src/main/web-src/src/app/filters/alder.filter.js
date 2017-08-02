angular.module('tps-forvalteren.filter')
    .filter('alder', function() {

        // Create the return function
        return function (ident, dodsdato) {

            function gregorianAge (birthDate, ageAtDate) {
                // convert birthDate to date object if already not
                if (Object.prototype.toString.call(birthDate) !== '[object Date]')
                    birthDate = new Date(birthDate);

                // use today's date if ageAtDate is not provided
                if (typeof ageAtDate == "undefined")
                    ageAtDate = new Date();

                // convert ageAtDate to date object if already not
                else if (Object.prototype.toString.call(ageAtDate) !== '[object Date]')
                    ageAtDate = new Date(ageAtDate);

                // if conversion to date object fails return null
                if (ageAtDate === null || birthDate === null)
                    return null;


                var _m = ageAtDate.getMonth() - birthDate.getMonth();

                // answer: ageAt year minus birth year less one (1) if month and day of
                // ageAt year is before month and day of birth year
                return (ageAtDate.getFullYear()) - birthDate.getFullYear() -
                    ((_m < 0 || (_m === 0 && ageAtDate.getDate() < birthDate.getDate()))?1:0);
            }

            var fnr = angular.copy(ident);
            // Fix D-number
            if (fnr.charAt(0) >= 4) {
                fnr = (fnr.charAt(0) - 4).toString() + fnr.substring(1);
            }
            // Fix B-number
            if (fnr.charAt(2) >= 2) {
                fnr = fnr.substring(0,2) + (fnr.charAt(2) - 2).toString() + fnr.substring(3);
            }

            var individ = parseInt(fnr.substring(6, 9));
            var aarhundre = individ < 500 || individ >= 900 ? '19' : '20';

            return (gregorianAge(new Date(aarhundre + fnr.substring(4, 6), fnr.substring(2, 4) - 1, fnr.substring(0, 2)), dodsdato)) + ' år';
        };
    });
