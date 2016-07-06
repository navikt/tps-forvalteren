/**
 * @author Frederik de Lichtenberg (Visma Consulting AS).
 * */
angular.module('tps-vedlikehold').directive('tpsDateField', function(){
    return {
        template: '<md-datepicker ng-model="field.value" md-placeholder="{{ field.label }}" flex-gt-sm=""></md-datepicker>'
    };
});