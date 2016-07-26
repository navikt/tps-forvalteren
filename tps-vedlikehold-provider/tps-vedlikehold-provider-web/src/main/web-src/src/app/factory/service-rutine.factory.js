/**
 * @author Frederik de Lichtenberg (Visma Consulting AS).
 * */
angular.module('tps-vedlikehold')
    .factory('serviceRutineFactory', ['$http', 'serviceRutineConfig', function($http, serviceRutineConfig) {

        var serviceRutineFactory = {};
        
        var urlBase = 'api/v1/service';
        var urlBaseEnv = 'api/v1/environments';

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
            var serviceRutineNames = [];

            angular.forEach(serviceRutines, function(value, key) {
                this.push(key);
            }, serviceRutineNames);
            return serviceRutineNames;
        };

        serviceRutineFactory.getServiceRutineInternalName = function(serviceRutineName) {
            return serviceRutines[serviceRutineName].internalName;
        };

        serviceRutineFactory.getServiceRutineAttributesNames = function(serviceRutineName) {
            var serviceRutineAttributesNames = [];

            angular.forEach(serviceRutines[serviceRutineName].attributes, function (value, key) {
                this.push(value.name);
            }, serviceRutineAttributesNames);

            return serviceRutineAttributesNames;
        };

        serviceRutineFactory.getServiceRutineAttributesNamesInOrder = function(serviceRutineName) {
            // want the fields from serviceRutineFieldsTemplate in a certain order
            // could probably be done in a better way
            var serviceRutineAttributesNamesInOrder = [];
            var restServiceRutineAttributesNames = serviceRutineFactory.getServiceRutineAttributesNames(serviceRutineName);
            var serviceRutineFieldsOrderTemplate = serviceRutineConfig[serviceRutineName].serviceRutineFieldsOrderTemplate;

            for (var i = 0; i < serviceRutineFieldsOrderTemplate.length; i++) {
                var index = restServiceRutineAttributesNames.indexOf(serviceRutineFieldsOrderTemplate[i]);

                if (index > -1) {
                    serviceRutineAttributesNamesInOrder.push(serviceRutineFieldsOrderTemplate[i]);
                    restServiceRutineAttributesNames.splice(index, 1);
                }
            }
            return serviceRutineAttributesNamesInOrder.concat(restServiceRutineAttributesNames);
        };

        serviceRutineFactory.getServiceRutineRequiredAttributesNames = function(serviceRutineName) {
            var serviceRutineRequiredAttributesNames = [];

            if (serviceRutines[serviceRutineName].attributes) {
                angular.forEach(serviceRutines[serviceRutineName].attributes, function (value, key) {
                    if (value.use === "required") {
                        this.push(value.name);
                    }
                }, serviceRutineRequiredAttributesNames);
            }
            return serviceRutineRequiredAttributesNames;
        };

        serviceRutineFactory.hasAksjonsKodes = function(serviceRutineName) {
            var aksjonsKodes = serviceRutines[serviceRutineName].aksjonsKodes;

            return aksjonsKodes && aksjonsKodes.length;
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
            return serviceRutineConfig[serviceRutineName].nonUniquePropertiesContainer;
        };

        serviceRutineFactory.getServiceRutineReturnedDataLabel = function(serviceRutineName) {
            return serviceRutineConfig[serviceRutineName].serviceRutineReturnedDataLabel;
        };

        return serviceRutineFactory;
    }]);
