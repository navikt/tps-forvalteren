/**
 * @author Kristian Kyvik (Visma Consulting AS).
 */

angular.module('tps-vedlikehold.service')
    .service('utilsService', [function(){

        var self = this;

        self.authHeaders = function(credentials) {
            return {'Authorization': 'Basic ' + btoa(credentials.username + ":" + credentials.password)};
        };

    }]);