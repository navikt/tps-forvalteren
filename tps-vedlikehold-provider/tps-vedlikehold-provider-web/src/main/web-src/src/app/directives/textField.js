/**
 * @author Frederik de Lichtenberg (Visma Consulting AS).
 * */
angular.module('tps-vedlikehold').directive('tpsTextField', function(){
    return {
            template: '<md-input-container> <label>{{ field.label }}</label> <input ng-model="field.value"> </md-input-container>'
    };
});