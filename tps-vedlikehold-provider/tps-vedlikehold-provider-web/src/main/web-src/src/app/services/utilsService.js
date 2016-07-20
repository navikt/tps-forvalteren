/**
 * @author Kristian Kyvik (Visma Consulting AS).
 * @author Frederik de Lichtenberg (Visma Consulting AS).
 */
angular.module('tps-vedlikehold.service')
    .service('utilsService', [function(){

        var self = this;

        self.authHeaders = function(credentials) {
            return {'Authorization': 'Basic ' + btoa(credentials.username + ":" + credentials.password)};
        };

        self.formatDate = function(dateObj) {
            function pad(num) {
                if (num < 10) {
                    return '0' + num;
                }
                return num;
            }

            return dateObj.getFullYear() +
                    '-' + pad(dateObj.getMonth() + 1) +
                    '-' + pad(dateObj.getDate());
        };

        self.flattenObject = function(ob, nonUniques) {
            var ret = {};
            
            for (var i in ob) {
                if (!ob.hasOwnProperty(i)) continue;
                
                if ((typeof ob[i]) == 'object') {
                    var flatObject = self.flattenObject(ob[i]);
                    for (var x in flatObject) {
                        if (!flatObject.hasOwnProperty(x)) continue;

                        if (nonUniques.indexOf(i) > -1) {
                            ret[i + '_' + x] = flatObject[x];
                        } else {
                            ret[x] = flatObject[x];
                        }
                    }
                } else {
                    ret[i] = ob[i];
                }
            }
            return ret;
        };

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
                    var type = single ? 'single' : closing ? 'closing' : opening ? 'opening' : 'other';
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
    }]);
