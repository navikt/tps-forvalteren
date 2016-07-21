/**
 * @author Frederik de Lichtenberg (Visma Consulting AS).
 * */
angular.module('tps-vedlikehold').directive('tpsResponseForm', function(){
    var templatesPath = 'app/components/service-rutine/responseTemplates/';
    var templateSuffix = '-responseTemplate.html';

    return {
        restrict: 'E',
        replace: true,
        scope: true,
        link: function(scope, element, attrs) {
            scope.getTemplateUrl = function () {
                var type = scope.serviceRutinenavn;
                if (type) {
                    return templatesPath + '' + type + '' + templateSuffix;
                }
            };
        },
        template: '<div class="tps-response-form md-padding" ng-include="getTemplateUrl()"></div>'
    };
});
