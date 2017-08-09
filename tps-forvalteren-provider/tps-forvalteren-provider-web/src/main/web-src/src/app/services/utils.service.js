
angular.module('tps-forvalteren.service')
    .service('utilsService', ['moment', '$mdDialog','locationService','authenticationService', function(moment, $mdDialog, locationService, authenticationService){

        var self = this;

        self.getCurrentFormattedDate = function() {
            return moment().format('YYYY-MM-DD');
        };

        self.createParametersFromFormData = function(formData){
            var params = {};
            for (var key in formData) {
                if (formData.hasOwnProperty(key) && formData[key]) {
                    params[key] = formData[key];
                }
            }
            return params;
        };

        self.sortEnvironments = function(env) {
            if (env.length < 2) { return env; }
            return env.sort(function(a, b) {
                if (a.charAt(0) !== b.charAt(0)) {
                    return a.localeCompare(b);
                }

                var aSubstrInt = parseInt(a.substr(1));
                var bSubstrInt = parseInt(b.substr(1));

                if (isNaN(aSubstrInt) || isNaN(bSubstrInt)) {
                    return a.substr(1).localeCompare(b.substr(1));
                }
                return aSubstrInt - bSubstrInt;
            });
        };

        self.flattenObject = function(jsonObject){
            var result = {};
            function recurse(jObject, properties){
                if(Object(jObject) !== jObject){
                    result[properties] = jObject;
                } else if(Array.isArray(jObject)){
                    for(var i=0, l=jObject.length; i<jObject.length; i++){
                        recurse(jObject[i], properties + "[" + i + "]");
                    }
                    if(l === 0){
                        result[properties] = [];
                    }
                } else {
                    var isEmpty = true;
                    for(var p in jObject){
                        isEmpty = false;
                        recurse(jObject[p], properties ? properties + "." + p : p);
                    }
                    if(isEmpty && properties){
                        result[properties] = {};
                    }
                }
            }
            recurse(jsonObject, "");
            return result;
        };

        self.isNumber = function(n){
            return !isNaN(parseFloat(n)) && isFinite(n);
        };

        //TODO: use library for this
        self.formatXml = function (xml) {
            if (xml) {
                var reg = /(>)\s*(<)(\/*)/g;
                var wsexp = / *(.*) +\n/g;
                var contexp = /(<.+>)(.+\n)/g;
                xml = xml.replace(reg, '$1\n$2$3').replace(wsexp, '$1\n').replace(contexp, '$1\n$2');
                var formatted = '';
                var lines = xml.split('\n');
                var indent = 0;
                var lastType = 'other';

                var transitions = {
                    'single->single': 0,
                    'single->closing': -1,
                    'single->opening': 0,
                    'single->other': 0,
                    'closing->single': 0,
                    'closing->closing': -1,
                    'closing->opening': 0,
                    'closing->other': 0,
                    'opening->single': 1,
                    'opening->closing': 0,
                    'opening->opening': 1,
                    'opening->other': 1,
                    'other->single': 0,
                    'other->closing': -1,
                    'other->opening': 0,
                    'other->other': 0
                };

                for (var i = 0; i < lines.length; i++) {
                    var ln = lines[i];
                    var single = Boolean(ln.match(/<.+\/>/));
                    var closing = Boolean(ln.match(/<\/.+>/));
                    var opening = Boolean(ln.match(/<[^!].*>/));
                    var type = single ? 'single' : (closing ? 'closing' : (opening ? 'opening' : 'other'));
                    var fromTo = lastType + '->' + type;
                    lastType = type;
                    var padding = '';

                    indent += transitions[fromTo];
                    for (var j = 0; j < indent; j++) {
                        padding += '\t';
                    }
                    if (fromTo == 'opening->closing')
                        formatted = formatted.substr(0, formatted.length - 1) + ln + '\n';
                    else
                        formatted += padding + ln + '\n';
                }

                return formatted;
            }
        };

        self.showAlertError = function (error) {
            var errorMessages = {
                400: {
                    title: 'Ugyldig forespørsel',
                    text: 'Du har sendt en ugyldig forspørsel.',
                    ariaLabel: 'Ugyldig forespørsel fra din bruker.'
                },
                401: {
                    title: 'Ikke autorisert',
                    text: 'Bruker er ikke autentisert. Prøv å logge inn på nytt',
                    ariaLabel: 'Bruker er ikke autentisert. Prøv å logge inn på nytt'
                },
                403: {
                    title: 'Forbudt',
                    text: 'Din bruker har ikke tillatelse til denne spørringen.',
                    ariaLabel: 'Din bruker har ikke tillatelse til denne spørringen.'
                },
                404: {
                    title: 'Ukjent side',
                    text: error.config.url + ' finnes ikke',
                    ariaLabel: 'Opprett siden på server og forsøk igjen.'
                },
                500: {
                    title: 'Serverfeil',
                    text: 'Fikk ikke hentet informasjon fra server.',
                    ariaLabel: 'Feil ved henting av data fra server'
                }
            };

            //var errorMsg = error.data.message;
            var errorObj = errorMessages[error.status];
            $mdDialog.show(
                $mdDialog.alert()
                    .title(errorObj.title)
                    .textContent(errorObj.text)
                    .ariaLabel(errorObj.ariaLabel)
                    .ok('OK')
            ).finally(function () {
                if(error.status === 401){
                    authenticationService.invalidateSession(function () {
                        locationService.redirectToLoginState();
                    });
                }
            });
        };
    }]);
