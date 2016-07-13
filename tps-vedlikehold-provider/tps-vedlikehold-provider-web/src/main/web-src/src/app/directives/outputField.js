/**
 * @author Frederik de Lichtenberg (Visma Consulting AS).
 * */
angular.module('tps-vedlikehold').directive('tpsOutputField', function(){

    return {
        restrict: 'E',
        replace: true,
        scope: {
            fieldData: '=',
            label: '@'
        },
        template:
        '<div class="tps-output-field" layout="column" flex="33">' +
        '<span class="output-label">{{ label }}</span>' +
        '<span class="output-data">{{ fieldData }}</span>' +
        '</div>'
    };
});
