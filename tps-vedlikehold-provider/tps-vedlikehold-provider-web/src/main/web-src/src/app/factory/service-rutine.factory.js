/**
 * @author Frederik de Lichtenberg (Visma Consulting AS).
 * */
angular.module('tps-vedlikehold')
    .factory('serviceRutineFactory', ['$http', function($http) {

        var serviceRutineFactory = {};
        
        var urlBase = 'api/v1/service';
        var urlBaseEnv = 'api/v1/environments';

        // ####################################
        // extend for additional serviceRutines

        // order of input fields
        var serviceRutineFieldsTemplate = {
            'FS03-FDNUMMER-PERSDATA-O': ['fnr', 'aksjonsDato']
        };

        // name on data object returned by tps
        var serviceRutineReturnedDataLabel = {
            'FS03-FDNUMMER-PERSDATA-O': 'personDataS004'
        };

        // objects in response with non-unique fields
        // used to flatten object without creating duplicates/removing fields
        var nonUniquePropertiesContainer = {
            'FS03-FDNUMMER-PERSDATA-O': ['tlfPrivat', 'tlfJobb', 'tlfMobil']
        };
        // ####################################

        var serviceRutines = {};
        var environments = [];
        
        var isSetServiceRutines = false;
        var isSetEnvironments = false;

        serviceRutineFactory.isSetServiceRutines = function () {
            return isSetServiceRutines;
        };

        serviceRutineFactory.isSetEnvironments = function () {
            return isSetEnvironments;
        };

        serviceRutineFactory.loadFromServerServiceRutines = function() {
            return $http({method: 'GET', url: urlBase}).then(function(res) {
                if (res.data) {
                    var serviceRutineList = res.data;

                    for (var i = 0; i < serviceRutineList.length; i++) {
                        serviceRutines[serviceRutineList[i].name] = serviceRutineList[i];
                    }

                    isSetServiceRutines = true;
                    return serviceRutines;
                } else {
                    return null;
                }
            }, function(error) {
                return null;
            });
        };

        serviceRutineFactory.loadFromServerEnvironments = function() {
            return $http({method: 'GET', url: urlBaseEnv}).then(function(res) {
                environments = res.data;
                isSetEnvironments = true;
                return environments;
            }, function(error) {
                return null;
            });
        };

        serviceRutineFactory.getServiceRutines = function() {
            return serviceRutines;
        };

        serviceRutineFactory.getServiceRutineNames = function() {
            var ret = [];
            angular.forEach(serviceRutines, function(value, key) {
                this.push(key);
            }, ret);
            return ret;
        };

        serviceRutineFactory.getServiceRutineInternalName = function(serviceRutineName) {
            return serviceRutines[serviceRutineName].internalName;
        };

        serviceRutineFactory.getServiceRutineAttributesNames = function(serviceRutineName) {
            // want the fields from serviceRutineFieldsTemplate in a certain order
            // could be done in a better way
            var ret = [];
            if (serviceRutines[serviceRutineName].attributes) {
                var filter = [];
                angular.forEach(serviceRutines[serviceRutineName].attributes, function (value, key) {
                    this.push(value.name);
                }, filter);

                for (var i = 0; i < serviceRutineFieldsTemplate[serviceRutineName].length; i++) {
                    if (filter.indexOf(serviceRutineFieldsTemplate[serviceRutineName][i]) > -1) {
                        ret.push(serviceRutineFieldsTemplate[serviceRutineName][i]);
                    }
                }
                return ret;
            }
            return ret;
        };

        serviceRutineFactory.getServiceRutineRequiredAttributesNames = function(serviceRutineName) {
            var ret = [];
            if (serviceRutines[serviceRutineName].attributes) {
                angular.forEach(serviceRutines[serviceRutineName].attributes, function (value, key) {
                    if (value.use === "required") {
                        this.push(value.name);
                    }
                }, ret);
            }
            return ret;
        };

        serviceRutineFactory.hasAksjonsKodes = function(serviceRutineName) {
            var aksjonsKodes = serviceRutines[serviceRutineName].aksjonsKodes;
            if (aksjonsKodes) {
                if (aksjonsKodes.length > 0) {
                    return true;
                }
            }
            return false;
        };

        serviceRutineFactory.getServiceRutineAksjonsKodes = function(serviceRutineName) {
            return serviceRutines[serviceRutineName].aksjonsKodes;
        };

        serviceRutineFactory.getResponse = function(serviceRutineName, params) {
            return $http({method: 'GET', url:urlBase + '/' + serviceRutineName, params:params});
        };

        serviceRutineFactory.getEnvironments = function() {
            return environments;
        };

        serviceRutineFactory.getNonUniqueProperties = function(serviceRutineName) {
            return nonUniquePropertiesContainer[serviceRutineName];
        };

        serviceRutineFactory.getServiceRutineReturnedDataLabel = function(serviceRutineName) {
            return serviceRutineReturnedDataLabel[serviceRutineName];
        };

        return serviceRutineFactory;
    }]);
