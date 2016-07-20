/**
 * @author Frederik de Lichtenberg (Visma Consulting AS).
 */
angular.module('tps-vedlikehold.service')
    .service('serverService', ['$http', 'servicerutineFactory', function($http, servicerutineFactory) {
        var self = this;
        var urlBase = 'api/v1/service';
        var urlBaseEnv = 'api/v1/environments';

        self.getFromServerServicerutiner = function() {
            $http({method: 'GET', url: urlBase}).then(function (res) {
                servicerutineFactory.setServicerutiner(res.data);
            }, function (error) {
                // servicerutineFactory.setServerError();
            });
        };
        
        self.getFromServerEnvironments = function() {
            $http({method: 'GET', url: urlBaseEnv}).then(function(res) {
                servicerutineFactory.setEnvironments(res);
            });
        };
    }]);