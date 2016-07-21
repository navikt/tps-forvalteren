/**
 * @author Frederik de Lichtenberg (Visma Consulting AS).
 * */
angular.module('tps-vedlikehold').directive('tpsResponseForm', function(){
<<<<<<< HEAD
    var templatesPath = 'app/components/servicerutine/responsetemplates/';
=======
    
    var templatesPath = 'app/components/servicerutine/responseTemplates/';
>>>>>>> 30300e0cdad3762881440c1ac10fe47dec4e1dc7
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
