/**
 * @author Frederik de Lichtenberg (Visma Consulting AS).
 * */
angular.module('tps-vedlikehold.factory')
    .factory('serviceRutineFactory', ['$http', 'serviceRutineConfig', function($http, serviceRutineConfig) {

        var serviceRutineFactory = {};
        
        var urlBase = 'api/v1/service';
        var urlRoutinesBase = 'api/v1/serviceroutine';
        var urlEndrinsmeldinger = 'api/v1/endringsmeldinger';
        var urlEndringsmeldingBase ='api/v1/endring';
        var urlBaseEnv = 'api/v1/environments';

        var serviceRutines = {};
        var endringsmeldinger = {};
        var environments = [];
        
        var isSetServiceRutines = false;
        var isSetEnvironments = false;
        var isSetEndringsmeldinger = false;

        serviceRutineFactory.isSetServiceRutines = function () {
            return isSetServiceRutines;
        };

        serviceRutineFactory.isSetEnvironments = function () {
            return isSetEnvironments;
        };

        serviceRutineFactory.isSetEndringsmeldinger = function () {
           return isSetEndringsmeldinger;
        };

        serviceRutineFactory.loadFromServerServiceRutines = function() {
            return $http({method: 'GET', url: urlRoutinesBase}).then(function(response) {
                if (response.data) {
                    var serviceRutineList = response.data;

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

        serviceRutineFactory.loadFromServerEndringsmeldinger = function(){
          return $http({method: 'GET', url: urlEndrinsmeldinger}).then(function(response){
              if(response.data){
                  var endringsmeldingList = response.data;
                  for(var i = 0; i < endringsmeldingList.length; i++){
                      endringsmeldinger[endringsmeldingList[i].name] = endringsmeldingList[i];
                  }
                  isSetEndringsmeldinger = true;
                  return endringsmeldinger;
              } else {
                  return null;
              }
          }, function (error) {
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

        serviceRutineFactory.getEndringsmeldinger = function () {
            return endringsmeldinger;
        };

        serviceRutineFactory.getServiceRutines = function() {
            return serviceRutines;
        };

        serviceRutineFactory.getEndringsmeldingerNames = function () {
            var endringsmeldingNames = [];

            angular.forEach(endringsmeldinger, function(value, key) {
                this.push(key);
            }, endringsmeldingNames);
            return endringsmeldingNames;
        };

        serviceRutineFactory.getServiceRutineNames = function() {
            var serviceRutineNames = [];

            angular.forEach(serviceRutines, function(value, key) {
                this.push(key);
            }, serviceRutineNames);
            return serviceRutineNames;
        };

        //TODO Prøv å generaliser dette igjen...
        serviceRutineFactory.getServiceRutineInternalName = function(serviceRutineName) {
            return serviceRutines[serviceRutineName].internalName;
        };

        serviceRutineFactory.getEndringsmeldingInternalName = function (endringsmeldingName) {
           return endringsmeldinger[endringsmeldingName].internalName;
        };

        serviceRutineFactory.getServiceRutineParametersNames = function(serviceRutineName) {
            return getRequestParametersNames(serviceRutines, serviceRutineName);
        };

        serviceRutineFactory.getEndringsmeldingParametersNames = function(endringsmeldingName) {
            return getRequestParametersNames(endringsmeldinger, endringsmeldingName);
        };

        serviceRutineFactory.getServiceRutineParametersNamesInOrder = function(serviceRutineName) {
            // want the fields from serviceRutineFieldsTemplate in a certain order
            // could probably be done in a better way
            var serviceRutineParametersNamesInOrder = [];
            var restServiceRutineParametersNames = serviceRutineFactory.getServiceRutineParametersNames(serviceRutineName);
            var serviceRutineFieldsOrderTemplate = [];

            if (serviceRutineConfig[serviceRutineName]) {
                serviceRutineFieldsOrderTemplate = serviceRutineConfig[serviceRutineName].serviceRutineFieldsOrderTemplate;
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

        serviceRutineFactory.getEndringsmeldingParameterNamesInOrder = function(endringsmeldingName){
             return serviceRutineFactory.getEndringsmeldingParametersNames(endringsmeldingName);
        };

        serviceRutineFactory.getServiceRutineRequiredParametersNames = function(serviceRutineName) {
            return getRequestRequiredParametersNames(serviceRutines, serviceRutineName);
        };

        serviceRutineFactory.getEndringsmeldingerRequiredParametersNames = function(endringsmeldingName) {
            return getRequestRequiredParametersNames(endringsmeldinger, endringsmeldingName);
        };


        serviceRutineFactory.getSelectValuesServiceRutine = function (serviceRutineName) {
            return getSelectValues(serviceRutines, serviceRutineName);
        };

        serviceRutineFactory.getSelectValuesEndringsmelding = function (endringsmeldingName) {
            return getSelectValues(endringsmeldinger,endringsmeldingName);
        };

        serviceRutineFactory.getServiceRutineResponse = function(serviceRutineName, params) {
            return $http({method: 'GET', url:urlBase + '/' + serviceRutineName, params:params});
        };

        serviceRutineFactory.getEndringsmeldingResponse = function (endringsmeldingName, params) {
            return $http({method: 'GET', url:urlEndringsmeldingBase + '/' + endringsmeldingName, params:params});
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

        function getRequestParametersNames(requestMap, requestName) {
            var serviceRutineParametersNames = [];

            angular.forEach(requestMap[requestName].parameters, function (value, key) {
                this.push(value.name);
            }, serviceRutineParametersNames);

            return serviceRutineParametersNames;
        }

        function getRequestRequiredParametersNames(requestMap, requestName){
            var requestRequiredParametersNames = [];

            angular.forEach(requestMap[requestName].parameters, function (value, key) {
                if (value.use === "required") {
                    this.push(value.name);
                }
            }, requestRequiredParametersNames);

            return requestRequiredParametersNames;
        }

        function getSelectValues(requestMap, requestName){
            var selectValues = {};

            angular.forEach(requestMap[requestName].parameters, function (value, key) {
                if (value.values) {
                    this[value.name] = value.values;
                }
            }, selectValues);

            return selectValues;
        }

        return serviceRutineFactory;
    }]);
