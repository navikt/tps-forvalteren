/**
 * @author Frederik de Lichtenberg (Visma Consulting AS).
 * */
angular.module('tps-vedlikehold')
    .factory('requestFactory', ['$http', function($http) {
        var urlBase = 'api/v1/service';
        var urlBaseEnv = 'api/v1/environments';
        var requestFactory = {};

        // requestFactory.getFields = function(servicerutineKode) {
        //     return $http({method: 'GET', url:urlBase+'/'+servicerutineKode});
        // };
        //
        // requestFactory.getResponse = function(servicerutineKode, formData) {
        //     return $http({method: 'GET', url:urlBase+'/'+servicerutineKode, params:formData}); // not correct url yet
        // };
        //
        // requestFactory.getMiljo = function() {
        //     return $http({method: 'GET', url:urlBaseEnv});
        // };






        //temporary values
        var fields = [
            {label:'Fødselsnummer', type: 'fnr'},
            {label:'Variant', type: 'aksjonskode', values:['A0', 'A2', 'B0', 'B2', 'B9', 'C0', 'D0', 'E0']},
            {label:'Dato', type: 'dato'},

            {label:'Fødselsnummer', type: 'fnr'},
            {label:'Adresse 1', type: 'fnr'},
            {label:'Adresse 2', type: 'fnr'},
            {label:'Dato', type: 'dato'},
            {label:'Variant', type: 'aksjonskode', values:['A0', 'A2', 'B0', 'B2', 'B9', 'C0', 'D0', 'E0']}
        ];

        // må sendes til api
        requestFactory.getFields = function (servicerutineKode) {
            switch (servicerutineKode) {
                case 'S004':
                    return fields.slice(0,3);
                case 'S322':
                    return fields.slice(3, 8);
                default:
                    return [];
            }
        };

        return requestFactory;
    }]);
