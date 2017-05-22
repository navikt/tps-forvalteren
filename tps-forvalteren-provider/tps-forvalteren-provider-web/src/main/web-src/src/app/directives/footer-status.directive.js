angular.module('tps-forvalteren.directives')
    .directive('footerStatus', function() {
    return {
        restrict: 'E',
        replace: true,
        scope: {
            status: '=',
            antallTreff: '@'
        },
        link: function (scope, elem, attr) {
            scope.prepStatus = function(status) {
                if (status) {
                    scope.okStatus = status.kode == '00';

                    var svarStatus = "Status: " + status.kode;
                    if (!scope.okStatus) {
                        svarStatus += "; Melding: " + status.melding + ": " + status.utfyllendeMelding;
                    }
                    return svarStatus;
                }
            };
        },
        template:
            '<md-content class="tps-vk-status-container md-whiteframe-z2" layout="row" layout-align="start center">' +
                '<div class="tps-vk-hits" flex="50" layout="row" layout-align="start center">' +
                    '<div class="tps-vk-hits-meesage" flex="20" layout-align="center center" ng-if="status && antallTreff">' +
                        'Treff: {{antallTreff}}' +
                    '</div>' +
                '</div>' +
                '<div class="tps-vk-status" flex="50" ng-if="status">' +
                    '<ng-md-icon class="tps-vk-status-icon" ng-class="okStatus ? \'success\' : \'error\'" icon="fiber_manual_record"></ng-md-icon>' +
                    '<div class="tps-vk-status-message">{{prepStatus(status)}}</div>' +
                '</div>' +
            '</md-content>'
    };
});
