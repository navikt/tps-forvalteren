/**
 * @author Frederik de Lichtenberg (Visma Consulting AS).
 * */
angular.module('tps-vedlikehold')
    .directive('tpsOutputField', function(){

    return {
        restrict: 'E',
        replace: true,
        scope: {
            fieldModel: '=',
            label: '@'
        },
        template:
        '<div layout="column">' +
        '<md-input-container class="tps-vk-output-container md-input-focused" >' +
            '<label class="tps-vk-output-label">{{ label }}</label>' +
            '<input class="tps-vk-output-data" ng-class="{filled : fieldModel.trim().length, empty : fieldModel.trim().length == 0}" ' +
                'type="text" ng-model="fieldModel" readonly tabindex="-1"/>' +
        '</md-input-container>' +
        '</div>'
    };
});
