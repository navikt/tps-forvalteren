
angular.module('tps-forvalteren.factory')
    .factory('serviceRutineFactory', ['$http', function ($http) {

        var serviceRutineFactory = {};

        var urlBase = 'api/v1/service';
        var urlRoutinesBase = 'api/v1/serviceroutine';
        var urlEndrinsmeldinger = 'api/v1/endringsmeldinger';
        var urlBaseEnv = 'api/v1/environments';
        var urlConfig = 'assets/config/';

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

        serviceRutineFactory.loadFromServerServiceRutines = function () {
            return $http({method: 'GET', url: urlRoutinesBase}).then(function (response) {
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
            }, function (error) {
                return null;
            });
        };


        serviceRutineFactory.loadFromServerEndringsmeldinger = function () {
            return $http({method: 'GET', url: urlEndrinsmeldinger}).then(function (response) {
                if (response.data) {
                    var endringsmeldingList = response.data;
                    for (var i = 0; i < endringsmeldingList.length; i++) {
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

        serviceRutineFactory.loadFromServerEnvironments = function () {
            return $http({method: 'GET', url: urlBaseEnv}).then(function (res) {
                environments = res.data;
                isSetEnvironments = true;
                return environments;
            }, function (error) {
                return null;
            });
        };

        serviceRutineFactory.getServiceRutines = function () {
            return serviceRutines;
        };


        serviceRutineFactory.getServiceRutineNames = function () {
            var serviceRutineNames = [];

            angular.forEach(serviceRutines, function (value, key) {
                this.push(key);
            }, serviceRutineNames);
            return serviceRutineNames;
        };

        serviceRutineFactory.getServiceRutineInternalName = function (serviceRutineName) {
            return serviceRutines[serviceRutineName].internalName;
        };

        serviceRutineFactory.getServiceRutineParametersNames = function (serviceRutineName) {
            return getRequestParametersNames(serviceRutines, serviceRutineName);
        };

        serviceRutineFactory.getServiceRutineRequiredParametersNames = function (serviceRutineName) {
            return getRequestRequiredParametersNames(serviceRutines, serviceRutineName);
        };

        serviceRutineFactory.getSelectValuesServiceRutine = function (serviceRutineName) {
            return getSelectValues(serviceRutines, serviceRutineName);
        };

        serviceRutineFactory.getServiceRutineResponse = function (serviceRutineName, params) {
            return $http({method: 'GET', url: urlBase + '/' + serviceRutineName, params: params});
        };

        serviceRutineFactory.getServiceRoutineConfig = function (serviceRutineName) {
            var srn = serviceRutineName.toLowerCase();
            return $http.get(urlConfig + srn + '.json');
        };

        serviceRutineFactory.getEnvironments = function () {
            return environments;
        };

        function getRequestParametersNames(requestMap, requestName) {
            var serviceRutineParametersNames = [];

            angular.forEach(requestMap[requestName].parameters, function (value, key) {
                this.push(value.name);
            }, serviceRutineParametersNames);

            return serviceRutineParametersNames;
        }

        function getRequestRequiredParametersNames(requestMap, requestName) {
            var requestRequiredParametersNames = [];

            angular.forEach(requestMap[requestName].parameters, function (value, key) {
                if (value.use === "required") {
                    this.push(value.name);
                }
            }, requestRequiredParametersNames);

            return requestRequiredParametersNames;
        }

        function getSelectValues(requestMap, requestName) {
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
