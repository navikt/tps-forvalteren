
angular.module('tps-forvalteren.directives')
    .directive('tpsInputField', ['$templateRequest', '$compile', function($templateRequest, $compile){

        var templatesPath = 'app/components/input-fields/';
        var templateSuffix = '-input.html';

        var getTemplateUrl = function(inputParamName) {
            return templatesPath + '' + inputParamName.toLowerCase() + '' + templateSuffix;
        };

        var linkerFunction = function(scope, element, attrs) {
            $templateRequest(getTemplateUrl(scope.inputParamName)).then(function(html) {
                var template = angular.element(html);
                element.replaceWith(template);
                $compile(template)(scope);
            });
        };

        return {
            restrict: 'E',
            scope: {
                inputParamName: '=',
                formData: '=',
                selectValues: '='
            },
            link: linkerFunction
        };
    }]);
