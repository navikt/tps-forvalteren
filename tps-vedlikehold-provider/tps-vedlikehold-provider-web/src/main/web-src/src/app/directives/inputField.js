/**
 * @author Frederik de Lichtenberg (Visma Consulting AS).
 * */
angular.module('tps-vedlikehold').directive('tpsInputField', ['$templateRequest', '$compile', 
    function($templateRequest, $compile){

    var templatesPath = 'app/components/service-rutine/inputfields/';
    var templateSuffix = 'Input.html';

    var getTemplateUrl = function(type) {
        return templatesPath + '' + type + '' + templateSuffix;
    };

    var linker = function(scope, element, attrs) {
        $templateRequest(getTemplateUrl(scope.fieldData)).then(function(html) {
            var template = angular.element(html);
            element.replaceWith(template);
            $compile(template)(scope);
        });
    };

    return {
        restrict: 'E',
        scope: true,
        link: linker
    };
}]);
