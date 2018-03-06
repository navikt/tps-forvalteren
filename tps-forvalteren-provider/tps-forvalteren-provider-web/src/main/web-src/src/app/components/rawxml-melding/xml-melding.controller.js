angular.module('tps-forvalteren.generell-melding', ['ngMaterial'])
    .controller('RawXmlCtrl', ['$scope', '$mdDialog', '$mdConstant', 'utilsService', 'headerService', 'XmlmeldingService',
        function ($scope, $mdDialog, $mdConstant, utilsService, headerService, XmlmeldingService) {

            headerService.setHeader('Raw XML-melding');


        }]);