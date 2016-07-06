/**
 * @author Frederik de Lichtenberg (Visma Consulting AS).
 * */
angular.module('tps-vedlikehold').directive('tpsSelectField', function(){
    return {
        template: '<md-input-container>' +
        ' <label>{{ field.label }}</label> ' +
        '<md-select ng-model="field.value"> ' +
        '<md-option ng-repeat="val in field.values" value="{{val}}">{{ val }}</md-option> ' +
        '</md-select> ' +
        '</md-input-container>'
    };
});