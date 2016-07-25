/**
 * @author Frederik de Lichtenberg (Visma Consulting AS).
 */
angular.module('tps-vedlikehold.service')
    .service('serverServiceRutineService', ['serviceRutineFactory', function(serviceRutineFactory) {
    
    return serviceRutineFactory.loadFromServerServiceRutines();
    }]);
