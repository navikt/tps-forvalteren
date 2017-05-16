angular.module('tps-forvalteren.directives')
    .directive('statusField', function() {
        return {
            restrict: 'E',
            replace: true,
            scope: {
                status: '='
            },
            link: function (scope, elem, attr) {
                scope.prepStatus = function(status) {
                    if (status) {
                        scope.okStatus = (status.kode === '00' || status.kode === '04');

                        var svarStatus = "Status: " + status.kode;
                        if (status.kode !== '00') {
                            svarStatus += "; Melding: " + status.melding + ": " + status.utfyllendeMelding;
                        }
                        return svarStatus;
                    }
                };
            },
            template:
            '<md-content class="tps-vk-status-container" layout="row" layout-align="start center" style="border-top: 0px">' +
            '<div class="tps-vk-status"  ng-if="status">' +
            '<ng-md-icon ng-mouseenter="show=true" ng-mouseleave="show=false"  class="tps-vk-status-icon" ng-class="okStatus ? \'success\' : \'error\'" icon="fiber_manual_record"></ng-md-icon>' +
            '<div class="tps-vk-status-message" layout="row" ><p ng-show="show">{{prepStatus(status)}}</p></div>' +
            '</div>' +
            '</md-content>'
        };
    });

