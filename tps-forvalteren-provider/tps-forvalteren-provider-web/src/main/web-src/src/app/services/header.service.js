angular.module('tps-forvalteren.service')
    .service('headerService', ['$rootScope', function($rootScope) {

        var self =  this;

        self.setHeader = function (header) {
            $rootScope.header = header;
        };
    }]);