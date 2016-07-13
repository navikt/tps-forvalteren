/**
 * @author Frederik de Lichtenberg (Visma Consulting AS).
 * */
angular.module('tps-vedlikehold').directive('tpsResponseForm', function(){
    var templatesPath = 'app/components/servicerutine/responseTemplates/';
    var templateSuffix = 'ResponseTemplate.html';

    return {
        restrict: 'E',
        replace: true,
        scope: true,
        link: function(scope, element, attrs) {
            scope.getTemplateUrl = function () {
                var type = scope.servicerutineCode || 'error';
                return templatesPath + '' + type + '' + templateSuffix;
            };
        },
        template: '<div class="tps-response-form" ng-include="getTemplateUrl()"></div>'
    };
});
