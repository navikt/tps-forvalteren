/**
 * @author Frederik de Lichtenberg (Visma Consulting AS).
 * */
angular.module('tps-vedlikehold')
    .factory('requestFactory', ['$http', '$q', function($http, $q) {
        var urlBase = '';
        var requestFactory = {};

        //temporary values
        var fields = [
            {label:'Fødselsnummer', type: 'text'},
            {label:'Dato', type: 'date'},
            {label:'Variant', type: 'select', values:['E0', 'E1', 'E2']},
            {label:'Miljø', type: 'text'},
            {label:'Miljø', type: 'text'},
            {label:'Dato', type: 'date'},
            {label:'Variant', type: 'select', values:['E0', 'E1', 'E2']}
        ];

        requestFactory.getFields = function () {
            return fields;
        };

        return requestFactory;
    }]);