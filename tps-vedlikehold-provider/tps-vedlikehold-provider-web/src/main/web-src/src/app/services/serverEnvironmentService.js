/**
 * @author Frederik de Lichtenberg (Visma Consulting AS).
 */
angular.module('tps-vedlikehold.service')
    .service('serverEnvironmentService', ['servicerutineFactory', function(servicerutineFactory) {
        
    return servicerutineFactory.loadFromServerEnvironments();
    }]);
