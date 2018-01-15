angular.module('tps-forvalteren.doedsmeldinger', ['ngMaterial'])
    .controller('SendDoedsmeldingerCtrl', ['$scope', '$mdDialog', '$rootScope', '$stateParams', 'endringsmeldingService', 'locationService', 'utilsService', 'headerService',
        function ($scope, $mdDialog, $rootScope, $stateParams, endringsmeldingService, locationService, utilsService, headerService) {

            headerService.setHeader('Send Dødsmeldinger');


        }]);