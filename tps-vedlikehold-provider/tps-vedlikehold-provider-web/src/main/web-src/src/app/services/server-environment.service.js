/**
 * @author Frederik de Lichtenberg (Visma Consulting AS).
 */
angular.module('tps-vedlikehold.service')
    .service('serverEnvironmentService', ['serviceRutineFactory', function(serviceRutineFactory) {
        
    return serviceRutineFactory.loadFromServerEnvironments();
    }]);
