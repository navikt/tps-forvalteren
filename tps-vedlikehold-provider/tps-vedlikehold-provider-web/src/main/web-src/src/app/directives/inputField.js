/**
 * @author Frederik de Lichtenberg (Visma Consulting AS).
 * */
angular.module('tps-vedlikehold').directive('tpsInputField', function($compile){
    var templates = {
        textTemplate : '' +
            '<md-input-container>' +
            '<label>{{ content.label }}</label>' +
            '<input ng-model="content.value">' +
            '</md-input-container>',
        dateTemplate : '' +
            '<md-datepicker ng-model="content.value" md-placeholder="{{ content.label }}" flex-gt-sm=""></md-datepicker>',
        selectTemplate : '' +
            '<md-input-container>' +
            '<label>{{ content.label }}</label>' +
            '<md-select ng-model="content.value">' +
            '<md-option ng-repeat="val in content.values" value="{{val}}">{{ val }}</md-option>' +
            '</md-select>' +
            '</md-input-container>'
    };

    var getTemplate = function(content, attrs) {
        var template = {};
        template = templates[content.type+'Template'];
        if(typeof template != 'undefined' && template != null) {
            return template;
        }
        else {
            return '';
        }
    };

    var linker = function(scope, element, attrs) {
        element.html(getTemplate(scope.content, attrs)).show();
        $compile(element.contents())(scope);
    };

    return {
        restrict: 'E',
        link: linker,
        scope: {
            content:'='
        }
    };
});
// angular.module('tps-vedlikehold').directive('tpsInputField', function($compile){
//     var textTemplate = '' +
//         '<md-input-container>' +
//         '<label>{{ content.label }}</label>' +
//         '<input ng-model="content.value">' +
//         '</md-input-container>';
//
//     var dateTemplate = '' +
//         '<md-datepicker ng-model="content.value" md-placeholder="{{ content.label }}" flex-gt-sm=""></md-datepicker>';
//
//     var selectTemplate = '' +
//         '<md-input-container>' +
//         '<label>{{ content.label }}</label>' +
//         '<md-select ng-model="content.value">' +
//         '<md-option ng-repeat="val in content.values" value="{{val}}">{{ val }}</md-option>' +
//         '</md-select>' +
//         '</md-input-container>';
//
//     var getTemplate = function(fieldType) {
//         var template = '';
//         switch(fieldType) {
//             case 'text':
//                 template = textTemplate;
//                 break;
//             case 'date':
//                 template = dateTemplate;
//                 break;
//             case 'select':
//                 template = selectTemplate;
//                 break;
//         }
//
//         return template;
//     };
//
//     var linker = function(scope, element, attrs) {
//         scope.rootDirectory = '/dashboard';
//
//         element.html(getTemplate(scope.content.type)).show();
//
//         $compile(element.contents())(scope);
//     };
//
//     return {
//         restrict: 'E',
//         link: linker,
//         scope: {
//             content:'='
//         }
//     };
// });