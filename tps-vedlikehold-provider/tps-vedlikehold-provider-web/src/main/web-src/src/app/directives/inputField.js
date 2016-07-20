/**
 * @author Frederik de Lichtenberg (Visma Consulting AS).
 * */
angular.module('tps-vedlikehold').directive('tpsInputField', function(){
    var templatesPath = 'app/components/servicerutine/inputfields/';
    var templateSuffix = 'Input.html';

    return {
        restrict: 'E',
        replace: true,
        scope: true,
        link: function(scope, element, attrs) {
            scope.getTemplateUrl = function () {
                var type = scope.fieldData;
                if (type) {
                    return templatesPath + '' + type + '' + templateSuffix;
                }
            };
        },
        template: '<div class="tps-input-field" ng-include="getTemplateUrl()" flex-gt-sm></div>'
    };
});
