/**
 * @author Frederik de Lichtenberg (Visma Consulting AS).
 */
angular.module('tps-vedlikehold.service')
    .service('serverServicerutineService', ['serviceRutineFactory', function(serviceRutineFactory) {
    
    return serviceRutineFactory.loadFromServerServicerutiner();
    }]);
