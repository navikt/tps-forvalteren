/**
 * @author Kristian Kyvik (Visma Consulting AS).
 */
angular.module('tps-vedlikehold.service')
    .service('utilsService', [function(){

        var self = this;

        self.authHeaders = function(credentials) {
            return {'Authorization': 'Basic ' + btoa(credentials.username + ":" + credentials.password)};
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
                            ret[i + '.' + x] = flatObject[x];
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

    }]);
