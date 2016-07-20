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
        '<div layout="column">' +
            '<md-input-container class="tps-output-input-container md-input-focused" >' +
            '<label class="tps-output-label">{{ label }}</label>' +
            '<input class="tps-output-input" ng-class="{filled : fieldData.trim().length, empty : fieldData.trim().length == 0}" type="text" ng-model="fieldData" readonly/>' +
            '</md-input-container>' +
        '</div>'
    };
});
