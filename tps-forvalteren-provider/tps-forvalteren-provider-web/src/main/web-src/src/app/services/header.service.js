angular.module('tps-forvalteren.service')
    .service('headerService', ['$rootScope', function($rootScope) {

        var self =  this;
        $rootScope.header = {};

        self.setHeader = function (title) {
            $rootScope.header = {};
            $rootScope.header.name = title;
        };

        self.setButtons = function (buttons) {
            $rootScope.header.buttons = buttons;
        };

        self.setIcons = function (icons) {
            $rootScope.header.icons = icons;
        };
    }]);