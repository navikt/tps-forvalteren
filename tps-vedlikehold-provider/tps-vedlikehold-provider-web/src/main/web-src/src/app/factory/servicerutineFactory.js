/**
 * @author Frederik de Lichtenberg (Visma Consulting AS).
 * */
angular.module('tps-vedlikehold')
    .factory('servicerutineFactory', ['$http', function($http) {
        var urlBase = 'api/v1/service';
        var urlBaseEnv = 'api/v1/environments';
        var servicerutineFactory = {};
        var servicerutineFieldsTemplate = ['fnr', 'aksjonsDato', 'fremtidigFelt'];

        servicerutineFactory.servicerutiner = {};
        servicerutineFactory.environments = {};

        servicerutineFactory.fetchServicerutiner = function() {
            // $http({method: 'GET', url:urlBase}).then(function(res){
            //     servicerutineFactory.servicerutiner = res.data;
            //     return true; //?
            // }, function(error){
            //     return false;
            // });

            // TEST VALUES
            var res = {};
            res.data = {"tpsPersonData":{"S004":{"attributes":{"aksjonsDato":"2001-01-01","fnr":12345678901},"internNavn":"S004 hentPerson","serviceRutinenavn":"FS03-FDNUMMER-PERSDATA-O","aksjonsKode":["A0","A2","B0","B2","C0","D0","E0"]},"S322":{"attributes":{"aksjonsDato":"2001-01-01","fnr":12345678901},"internNavn":"S322 alt servicerutine","serviceRutinenavn":"FS03-FDNUMMER-PERSDATA-O","aksjonsKode":["A0","A2","B0","B2","C0","D0","E0"]}}};
            servicerutineFactory.servicerutiner = res.data.tpsPersonData;
            return true;
        };

        servicerutineFactory.getServicerutineNames = function() {
            var ret = [];
            angular.forEach(servicerutineFactory.servicerutiner, function(value, key) {
                this.push({code:key, name:value.internNavn});
            }, ret);
            return ret;
        };
        
        servicerutineFactory.getServicerutineAttributes = function(servicerutineCode) {
            //
            var filter = [];
            angular.forEach(servicerutineFactory.servicerutiner[servicerutineCode].attributes, function(value, key) {
                this.push(key);
            }, filter);

            var ret = [];
            for (var i=0; i<servicerutineFieldsTemplate.length; i++) {
                if (filter.indexOf(servicerutineFieldsTemplate[i]) !== -1) {
                    ret.push(servicerutineFieldsTemplate[i]);
                }
            }
            return ret;
        };
        
        servicerutineFactory.getServicerutineAksjonskoder = function(servicerutineCode) {
            return servicerutineFactory.servicerutiner[servicerutineCode].aksjonsKode;
        };

        servicerutineFactory.getResponse = function(servicerutineCode, formData) {
            return $http({method: 'GET', url:urlBase+'/'+servicerutineCode, params:formData}); // not correct url yet
        };

        servicerutineFactory.fetchEnvironments = function() {
            $http({method: 'GET', url:urlBaseEnv}).then(function(res){
                    servicerutineFactory.environments = res.data;
                    return true; //?
                }, function(error){
                    return false;
                });
        };

        servicerutineFactory.getEnvironments = function() {
            return servicerutineFactory.environments;
        };

        return servicerutineFactory;
    }]);
