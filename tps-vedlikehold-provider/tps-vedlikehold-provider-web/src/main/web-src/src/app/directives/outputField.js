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
        '<span class="output-data">{{ fieldData }}</span>' +
        '</div>'
        // flex="{{ flexSize }}"
        // '<md-input-container flex="33"><label>{{ label }}</label><input readonly ng-model="fieldData"/></md-input-container>'
    };
});
