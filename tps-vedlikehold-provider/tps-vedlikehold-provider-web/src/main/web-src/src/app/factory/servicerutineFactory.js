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

        var servicerutiner = {};
        var environments = [];
        
        var isSetServicerutiner = false;
        var isSetEnvironments = false;
        
        servicerutineFactory.isSetServicerutiner = function () {
            return isSetServicerutiner;
        };

        servicerutineFactory.isSetEnvironments = function () {
            return isSetEnvironments;
        };

        servicerutineFactory.loadFromServerServicerutiner = function() {
            return $http({method: 'GET', url: urlBase}).then(function(res) {
                if (res.data) {
                    var servicerutineList = res.data;

                    for (var i = 0; i < servicerutineList.length; i++) {
                        servicerutiner[servicerutineList[i].name] = servicerutineList[i];
                    }

                    isSetServicerutiner = true;
                    return servicerutiner;
                } else {
                    return null;
                }
            }, function(error) {
                return null;
            });
        };

        servicerutineFactory.loadFromServerEnvironments = function() {
            return $http({method: 'GET', url: urlBaseEnv}).then(function(res) {
                environments = res.data;
                isSetEnvironments = true;
                return environments;
            }, function(error) {
                return null;
            });
        };
        
        servicerutineFactory.getServicerutiner = function() {
            return servicerutiner;
        };

        servicerutineFactory.getServiceRutinenavns = function() {
            var ret = [];
            angular.forEach(servicerutiner, function(value, key) {
                this.push(key);
            }, ret);
            return ret;
        };
        
        servicerutineFactory.getServicerutineInternalName = function(serviceRutinenavn) {
            return servicerutiner[serviceRutinenavn].internalName;
        };

        servicerutineFactory.getServicerutineAttributesNames = function(serviceRutinenavn) {
            // want the fields from servicerutineFieldsTemplate in a certain order
            // could be done in a better way
            var ret = [];
            if (servicerutiner[serviceRutinenavn].attributes) {
                var filter = [];
                angular.forEach(servicerutiner[serviceRutinenavn].attributes, function (value, key) {
                    this.push(value.name);
                }, filter);

                for (var i = 0; i < servicerutineFieldsTemplate[serviceRutinenavn].length; i++) {
                    if (filter.indexOf(servicerutineFieldsTemplate[serviceRutinenavn][i]) > -1) {
                        ret.push(servicerutineFieldsTemplate[serviceRutinenavn][i]);
                    }
                }
                return ret;
            }
            return ret;
        };

        servicerutineFactory.getServicerutineRequiredAttributesNames = function(serviceRutinenavn) {
            var ret = [];
            if (servicerutiner[serviceRutinenavn].attributes) {
                angular.forEach(servicerutiner[serviceRutinenavn].attributes, function (value, key) {
                    if (value.use === "required") {
                        this.push(value.name);
                    }
                }, ret);
            }
            return ret;
        };

        servicerutineFactory.hasAksjonsKodes = function(serviceRutinenavn) {
            var aksjonsKodes = servicerutiner[serviceRutinenavn].aksjonsKodes;
            if (aksjonsKodes) {
                if (aksjonsKodes.length > 0) {
                    return true;
                }
            }
            return false;
        };
        
        servicerutineFactory.getServicerutineAksjonsKodes = function(serviceRutinenavn) {
            return servicerutiner[serviceRutinenavn].aksjonsKodes;
        };

        servicerutineFactory.getResponse = function(serviceRutinenavn, params) {
            return $http({method: 'GET', url:urlBase+'/'+serviceRutinenavn, params:params});
        };

        servicerutineFactory.getEnvironments = function() {
            return environments;
        };
        
        servicerutineFactory.getNonUniqueProperties = function(serviceRutinenavn) {
            return nonUniquePropertiesContainer[serviceRutinenavn];
        };
        
        servicerutineFactory.getServicerutineReturnedDataLabel = function(serviceRutinenavn) {
            return servicerutineReturnedDataLabel[serviceRutinenavn];
        };

        return servicerutineFactory;
    }]);
