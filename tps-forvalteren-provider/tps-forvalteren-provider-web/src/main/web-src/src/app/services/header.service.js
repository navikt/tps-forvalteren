angular.module('tps-forvalteren.service')
    .service('headerService', ['$rootScope', function($rootScope) {

        var self =  this;

        self.setHeader = function (title, keep) {
            if (!keep) {
                $rootScope.header = {};
            }
            $rootScope.header.name = title;
        };

        self.setButtons = function (buttons) {
            $rootScope.header.buttons = buttons;
        };

        self.setIcons = function (icons) {
            $rootScope.header.icons = icons;
        };

        self.getHeader = function () {
            return $rootScope.header;
        };

        self.eventUpdate = function () {
            $rootScope.$broadcast('updateEvent', 'Status er endret');
        }
    }]);