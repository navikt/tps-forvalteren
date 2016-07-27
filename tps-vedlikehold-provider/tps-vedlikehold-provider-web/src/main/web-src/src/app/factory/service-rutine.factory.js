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

        serviceRutineFactory.getServiceRutineParametersNames = function(serviceRutineName) {
            var serviceRutineParametersNames = [];

            angular.forEach(serviceRutines[serviceRutineName].parameters, function (value, key) {
                this.push(value.name);
            }, serviceRutineParametersNames);

            return serviceRutineParametersNames;
        };

        serviceRutineFactory.getServiceRutineParametersNamesInOrder = function(serviceRutineName) {
            // want the fields from serviceRutineFieldsTemplate in a certain order
            // could probably be done in a better way
            var serviceRutineParametersNamesInOrder = [];
            var restServiceRutineParametersNames = serviceRutineFactory.getServiceRutineParametersNames(serviceRutineName);

            if (serviceRutineConfig[serviceRutineName]) {
                var serviceRutineFieldsOrderTemplate = serviceRutineConfig[serviceRutineName].serviceRutineFieldsOrderTemplate;
            }

            angular.forEach(serviceRutineFieldsOrderTemplate, function (value, key) {
                var index = restServiceRutineParametersNames.indexOf(value);

                if (index > -1) {
                    serviceRutineParametersNamesInOrder.push(value);
                    restServiceRutineParametersNames.splice(index, 1);
                }
            });

            return serviceRutineParametersNamesInOrder.concat(restServiceRutineParametersNames);
        };

        serviceRutineFactory.getServiceRutineRequiredParametersNames = function(serviceRutineName) {
            var serviceRutineRequiredParametersNames = [];

            angular.forEach(serviceRutines[serviceRutineName].parameters, function (value, key) {
                if (value.use === "required") {
                    this.push(value.name);
                }
            }, serviceRutineRequiredParametersNames);

            return serviceRutineRequiredParametersNames;
        };

        serviceRutineFactory.getSelectValues = function (serviceRutineName) {
            var selectValues = {};

            angular.forEach(serviceRutines[serviceRutineName].parameters, function (value, key) {
                if (value.values) {
                    this[value.name] = value.values;
                }
            }, selectValues);

            return selectValues;
        };

        serviceRutineFactory.getResponse = function(serviceRutineName, params) {
            return $http({method: 'GET', url:urlBase + '/' + serviceRutineName, params:params});
        };

        serviceRutineFactory.getEnvironments = function() {
            return environments;
        };

        serviceRutineFactory.getNonUniqueProperties = function(serviceRutineName) {
            if (serviceRutineConfig[serviceRutineName]) {
                return serviceRutineConfig[serviceRutineName].nonUniquePropertiesContainer;
            }
            return [];
        };

        serviceRutineFactory.getServiceRutineReturnedDataLabel = function(serviceRutineName) {
            if (serviceRutineConfig[serviceRutineName]) {
                return serviceRutineConfig[serviceRutineName].serviceRutineReturnedDataLabel;
            }
        };

        return serviceRutineFactory;
    }]);
