/**
 * @author Frederik de Lichtenberg (Visma Consulting AS).
 * */
angular.module('tps-vedlikehold').directive('tpsInputField', function(){
    var templatesPath = 'app/components/servicerutine/inputfields/';
    var templateSuffix = 'Input.html';

    return {
        restrict: 'E',
        replace: true,
        scope: true,//{
            //fieldData: '='
        //},
        link: function(scope, element, attrs) {
            scope.getTemplateUrl = function () {
                var type = scope.fieldData || 'error';
                return templatesPath + '' + type + '' + templateSuffix;
            };
        },
        template: '<div class="dynamic-field" ng-include="getTemplateUrl()"></div>'
    };
});
