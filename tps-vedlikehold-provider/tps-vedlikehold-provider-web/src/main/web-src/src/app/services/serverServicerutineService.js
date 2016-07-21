/**
 * @author Frederik de Lichtenberg (Visma Consulting AS).
 */
angular.module('tps-vedlikehold.service')
    .service('serverServicerutineService', ['servicerutineFactory', function(servicerutineFactory) {
    
    return servicerutineFactory.loadFromServerServicerutiner();
    }]);
