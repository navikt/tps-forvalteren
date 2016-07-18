/**
 * @author Frederik de Lichtenberg (Visma Consulting AS).
 * */
angular.module('tps-vedlikehold')
    .factory('servicerutineFactory', ['$http', function($http) {
        var urlBase = 'api/v1/service';
        var urlBaseEnv = 'api/v1/environments';
        var servicerutineFactory = {};
        var servicerutineFieldsTemplate = ['fnr', 'aksjonsDato', 'fremtidigFelt'];
        var nonUniquePropertiesContainer = {
            'S004': ['tlfPrivat', 'tlfJobb', 'tlfMobil']
        };

        servicerutineFactory.servicerutiner = {};
        servicerutineFactory.environments = {};

        servicerutineFactory.fetchServicerutiner = function() {
            // console.log('fetchServicerutiner');
            // $http({method: 'GET', url:urlBase}).then(function(res){
            //     servicerutineFactory.servicerutiner = res.data.tpsPersonData;
            //     return true; //?
            // }, function(error){
            //     return false;
            // });

            // TEST VALUES
            var res = {};
            // res.data = {"tpsPersonData":{"S004":{"attributes":{"aksjonsDato":"2001-01-01","fnr":12345678901},"internNavn":"S004 hentPerson","serviceRutinenavn":"FS03-FDNUMMER-PERSDATA-O","aksjonsKode":["A0","A2","B0","B2","C0","D0","E0"]},"S322":{"attributes":{"aksjonsDato":"2001-01-01","fnr":12345678901},"internNavn":"S322 alt servicerutine","serviceRutinenavn":"FS03-FDNUMMER-PERSDATA-O","aksjonsKode":["A0","A2","B0","B2","C0","D0","E0"]}}};
            // res.data = {"tpsPersonData":{"S004":{"requiredAttributes":{"fnr":12345678901},"optionalAttributes":{"aksjonsDato":"2001-01-01"},"internNavn":"S004 hentPerson","serviceRutinenavn":"FS03-FDNUMMER-PERSDATA-O","aksjonsKode":["E0","A0","A2","B0","B2","C0","D0"]}, "S322":{"requiredAttributes":{"fnr":12345678901},"optionalAttributes":{"aksjonsDato":"2001-01-01"},"internNavn":"S322 ALT serv rut","serviceRutinenavn":"FS03-FDNUMMER-PERSDATA-O","aksjonsKode":["E0","A0","A2","B0","B2","C0","D0"]}}};
            res.data = {"tpsPersonData":{"S004":{"aksjonsKodes":{"aksjonsKode":["E0","A0","A2","B0","B2","C0","D0"]},"attributes":{"attribute":[{"use":"required","name":"fnr","type":"xs:string"},{"use":"optional","name":"aksjonsDato","type":"xs:date"}]},"internNavn":"S004 hentPerson","serviceRutinenavn":"FS03-FDNUMMER-PERSDATA-O"}}};
            servicerutineFactory.servicerutiner = res.data.tpsPersonData;
            return true;
        };
        
        servicerutineFactory.getServicerutiner = function() {
            return servicerutineFactory.servicerutiner;
        };

        servicerutineFactory.getServicerutineNames = function() {
            var ret = [];
            angular.forEach(servicerutineFactory.servicerutiner, function(value, key) {
                this.push({code:key, name:value.internNavn});
            }, ret);
            return ret;
        };
        
        servicerutineFactory.getServicerutineAttributesNames = function(servicerutineCode) {
            // vil ha feltene fra servicerutineFieldsTemplate i bestemt rekkefølge
            // kunne nok vært gjort på en bedre måte
            var filter = [];
            angular.forEach(servicerutineFactory.servicerutiner[servicerutineCode].attributes.attribute, function(value, key) {
                this.push(value.name);
            }, filter);

            var ret = [];
            for (var i=0; i<servicerutineFieldsTemplate.length; i++) {
                if (filter.indexOf(servicerutineFieldsTemplate[i]) > -1) {
                    ret.push(servicerutineFieldsTemplate[i]);
                }
            }
            return ret;
        };
        
        servicerutineFactory.getServicerutineRequiredAttributesNames = function(servicerutineCode) {
            // return servicerutineFactory.servicerutiner[servicerutineCode].requiredAttributes;
            var ret = [];
            angular.forEach(servicerutineFactory.servicerutiner[servicerutineCode].attributes.attribute, function(value, key) {
                if (value.use === "required") {
                    this.push(value.name);
                }
            }, ret);
            return ret;
        };
        
        servicerutineFactory.getServicerutineAksjonskoder = function(servicerutineCode) {
            return servicerutineFactory.servicerutiner[servicerutineCode].aksjonsKodes.aksjonsKode;
        };

        servicerutineFactory.getResponse = function(servicerutineCode, formData) {
            return $http({method: 'GET', url:urlBase+'/'+servicerutineCode, params:formData}); // not correct url yet
        };

        servicerutineFactory.fetchEnvironments = function() {
            // console.log('fetchEnvironments');
            // $http({method: 'GET', url:urlBaseEnv}).then(function(res){
            //         servicerutineFactory.environments = res.data;
            //         return true; //?
            //     }, function(error){
            //         return false;
            //     });
            servicerutineFactory.environments = ["t4","t5","t6","t7","t8","t9","t10","t12","t11","q0","q1","u5","u6","q2","q3","o1","q4","q5","q6","q7","q8","qx","q9","p","t0","t1","t2","t3"];
            return true;
        };

        servicerutineFactory.getEnvironments = function() {
            return servicerutineFactory.environments;
        };
        
        servicerutineFactory.getNonUniqueProperties = function(servicerutineCode) {
            return nonUniquePropertiesContainer[servicerutineCode];
        };

        return servicerutineFactory;
    }]);
