/**
 * @author Frederik de Lichtenberg (Visma Consulting AS).
 * */
angular.module('tps-vedlikehold')
    .factory('servicerutineFactory', ['$http', function($http) {
        var servicerutineFactory = {};
        
        var urlBase = 'api/v1/service';
        var urlBaseEnv = 'api/v1/environments';

        // ####################################
        // extend for additional serviceRutiner

        // order of input fields
        var servicerutineFieldsTemplate = {
            'FS03-FDNUMMER-PERSDATA-O': ['fnr', 'aksjonsDato']
        };

        // name on data object returned by tps
        var servicerutineReturnedDataLabel = {
            'FS03-FDNUMMER-PERSDATA-O': 'personDataS004'
        };

        // objects in response with non-unique fields
        // used to flatten object without creating duplicates/removing fields
        var nonUniquePropertiesContainer = {
            'FS03-FDNUMMER-PERSDATA-O': ['tlfPrivat', 'tlfJobb', 'tlfMobil']
        };
        // ####################################

        servicerutineFactory.servicerutiner = {};
        servicerutineFactory.environments = [];
        
        var isSetServicerutiner = false;
        var isSetEnvironments = false;
        
        // var serverError = false;
        //
        // servicerutineFactory.setServerError = function() {
        //     serverError = true;
        // };
        
        servicerutineFactory.isSetServicerutiner = function () {
            return isSetServicerutiner;
        };

        servicerutineFactory.isSetEnvironments = function () {
            return isSetEnvironments;
        };
        
        servicerutineFactory.getFromServerServicerutiner = function () {
            return $http({method: 'GET', url: urlBase});
        };

        servicerutineFactory.getFromServerEnvironments = function () {
            return $http({method: 'GET', url: urlBaseEnv});
        };
        
        servicerutineFactory.setServicerutiner = function(data) {
            servicerutineFactory.servicerutiner = data.tpsPersonData;
            isSetServicerutiner = true;
        };

        servicerutineFactory.setEnvironments = function(data) {
            servicerutineFactory.environments = data;
            isSetEnvironments = true;
        };
        
        servicerutineFactory.getServicerutiner = function() {
            return servicerutineFactory.servicerutiner;
        };

        servicerutineFactory.getServiceRutinenavn = function() {
            var ret = [];
            angular.forEach(servicerutineFactory.servicerutiner, function(value, key) {
                this.push(key);
            }, ret);
            return ret;
        };
        
        servicerutineFactory.getServicerutineInternNavn = function(serviceRutinenavn) {
            return servicerutineFactory.servicerutiner[serviceRutinenavn].internNavn;
        };

        servicerutineFactory.getServicerutineAttributesNames = function(serviceRutinenavn) {
            // want the fields from servicerutineFieldsTemplate in a certain order
            // could be done in a better way
            var filter = [];
            angular.forEach(servicerutineFactory.servicerutiner[serviceRutinenavn].attributes.attribute, function(value, key) {
                this.push(value.name);
            }, filter);

            var ret = [];
            for (var i=0; i<servicerutineFieldsTemplate[serviceRutinenavn].length; i++) {
                if (filter.indexOf(servicerutineFieldsTemplate[serviceRutinenavn][i]) > -1) {
                    ret.push(servicerutineFieldsTemplate[serviceRutinenavn][i]);
                }
            }
            return ret;
        };

        servicerutineFactory.getServicerutineRequiredAttributesNames = function(serviceRutinenavn) {
            var ret = [];
            angular.forEach(servicerutineFactory.servicerutiner[serviceRutinenavn].attributes.attribute, function(value, key) {
                if (value.use === "required") {
                    this.push(value.name);
                }
            }, ret);
            return ret;
        };
        
        servicerutineFactory.getServicerutineAksjonsKoder = function(serviceRutinenavn) {
            return servicerutineFactory.servicerutiner[serviceRutinenavn].aksjonsKodes.aksjonsKode;
        };

        servicerutineFactory.getResponse = function(serviceRutinenavn, params) {
            return $http({method: 'GET', url:urlBase+'/'+serviceRutinenavn, params:params});
        };

        servicerutineFactory.getEnvironments = function() {
            return servicerutineFactory.environments;
        };
        
        servicerutineFactory.getNonUniqueProperties = function(serviceRutinenavn) {
            return nonUniquePropertiesContainer[serviceRutinenavn];
        };
        
        servicerutineFactory.getServicerutineReturnedDataLabel = function(serviceRutinenavn) {
            return servicerutineReturnedDataLabel[serviceRutinenavn];
        };

        return servicerutineFactory;
    }]);
