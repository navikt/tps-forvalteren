/**
 * @author Frederik de Lichtenberg (Visma Consulting AS).
 * */
angular.module('tps-vedlikehold').directive('tpsOutputField', function(){

    return {
        restrict: 'E',
        replace: true,
        scope: {
            fieldData: '=',
            label: '@',
            flexSize: '@',
            styleClass: '@'
        },
        template:
        '<div class="tps-output-field {{ styleClass }}" layout="column" >' +
        '<span class="output-label">{{ label }}</span>' +
        '<input class="output-data" type="text" ng-model="fieldData" readonly>' +
        '</div>'
    };
});
