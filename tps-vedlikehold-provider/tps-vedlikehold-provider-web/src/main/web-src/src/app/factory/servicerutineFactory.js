/**
 * @author Frederik de Lichtenberg (Visma Consulting AS).
 * */
angular.module('tps-vedlikehold')
    .factory('servicerutineFactory', ['$http', function($http) {
        var servicerutineFactory = {};
        
        var urlBase = 'api/v1/service';
        var urlBaseEnv = 'api/v1/environments';
        
        var servicerutineFieldsTemplate = ['fnr', 'aksjonsDato', 'fremtidigFelt'];
        var nonUniquePropertiesContainer = {
            'S004': ['tlfPrivat', 'tlfJobb', 'tlfMobil']
        };

        servicerutineFactory.servicerutiner = {};
        servicerutineFactory.environments = {};
        
        // var getFromServerServicerutinerSuccess = false;
        // var getFromServerEnvironmentsSuccess = false;
        var isSetServicerutiner = false;
        var isSetEnvironments = false;
        
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

        // servicerutineFactory.getFromServerServicerutiner = function() {
        //     // console.log('fetchServicerutiner');
        //     if (!getFromServerServicerutinerSuccess) {
        //         $http({method: 'GET', url: urlBase}).then(function (res) {
        //             console.log('servicerutineFactory.getFromServerServicerutiner - success');
        //             servicerutineFactory.servicerutiner = res.data.tpsPersonData;
        //             getFromServerServicerutinerSuccess = true;
        //             console.log('servicerutiner: ' + servicerutineFactory.servicerutiner);
        //             return true;
        //         }, function (error) {
        //             console.log('servicerutineFactory.getFromServerServicerutiner - failure');
        //             return false;
        //         });
        //     }
        //     return true;
        //
        //     // TEST VALUES
        //     // if (!getFromServerServicerutinerSuccess) {
        //     //     var res = {};
        //     //     // res.data = {"tpsPersonData":{"S004":{"attributes":{"aksjonsDato":"2001-01-01","fnr":12345678901},"internNavn":"S004 hentPerson","serviceRutinenavn":"FS03-FDNUMMER-PERSDATA-O","aksjonsKode":["A0","A2","B0","B2","C0","D0","E0"]},"S322":{"attributes":{"aksjonsDato":"2001-01-01","fnr":12345678901},"internNavn":"S322 alt servicerutine","serviceRutinenavn":"FS03-FDNUMMER-PERSDATA-O","aksjonsKode":["A0","A2","B0","B2","C0","D0","E0"]}}};
        //     //     // res.data = {"tpsPersonData":{"S004":{"requiredAttributes":{"fnr":12345678901},"optionalAttributes":{"aksjonsDato":"2001-01-01"},"internNavn":"S004 hentPerson","serviceRutinenavn":"FS03-FDNUMMER-PERSDATA-O","aksjonsKode":["E0","A0","A2","B0","B2","C0","D0"]}, "S322":{"requiredAttributes":{"fnr":12345678901},"optionalAttributes":{"aksjonsDato":"2001-01-01"},"internNavn":"S322 ALT serv rut","serviceRutinenavn":"FS03-FDNUMMER-PERSDATA-O","aksjonsKode":["E0","A0","A2","B0","B2","C0","D0"]}}};
        //     //     res.data = {
        //     //         "tpsPersonData": {
        //     //             "S004": {
        //     //                 "aksjonsKodes":
        //     //                     {"aksjonsKode": ["E0", "A0", "A2", "B0", "B2", "C0", "D0"]},
        //     //                 "attributes": {
        //     //                     "attribute": [
        //     //                         {"use": "required", "name": "fnr", "type": "xs:string"},
        //     //                         {"use": "optional", "name": "aksjonsDato", "type": "xs:date"}
        //     //                     ]
        //     //                 },
        //     //                 "internNavn": "S004 hentPerson",
        //     //                 "serviceRutinenavn": "FS03-FDNUMMER-PERSDATA-O"
        //     //             }
        //     //         }
        //     //     };
        //     //     servicerutineFactory.servicerutiner = res.data.tpsPersonData;
        //     //     getFromServerServicerutinerSuccess = true;
        //     //     return true;
        //     // }
        //     // return true;
        // };
        
        // servicerutineFactory.resetServerErrorFlags = function() {
        //     getFromServerEnvironmentsSuccess = false;
        //     getFromServerServicerutinerSuccess = false;
        // };
        
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
            // want the fields from servicerutineFieldsTemplate in a certain order
            // could be done in a better way
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

        // servicerutineFactory.getFromServerEnvironments = function() {
        //     // console.log('fetchEnvironments');
        //     if (!getFromServerEnvironmentsSuccess) {
        //         $http({method: 'GET', url: urlBaseEnv}).then(function (res) {
        //             servicerutineFactory.environments = res.data;
        //             getFromServerEnvironmentsSuccess = true;
        //             return true;
        //         }, function (error) {
        //             return false;
        //         });
        //     }
        //     return true;
        //
        //     // if (!getFromServerEnvironmentsSuccess) {
        //     //     servicerutineFactory.environments = ["t4", "t5", "t6", "t7", "t8", "t9", "t10", "t12", "t11", "q0", "q1", "u5", "u6", "q2", "q3", "o1", "q4", "q5", "q6", "q7", "q8", "qx", "q9", "p", "t0", "t1", "t2", "t3"];
        //     //     getFromServerEnvironmentsSuccess = true;
        //     //     return true;
        //     // }
        //     // return true;
        // };

        servicerutineFactory.getEnvironments = function() {
            return servicerutineFactory.environments;
        };
        
        servicerutineFactory.getNonUniqueProperties = function(servicerutineCode) {
            return nonUniquePropertiesContainer[servicerutineCode];
        };

        return servicerutineFactory;
    }]);
