angular.module('tps-forvalteren.service')
    .service('toggleservice', [function () {
        var self =  this;

        self.toggleAlleFaner = function (state, control, pager) {
            var ui = state;
            if (pager) {
                ui = !ui;
                for (var i = pager.startIndex; i < pager.endIndex + 1; i++) {
                    control[i] = control[i] || {};
                    control[i].aapen = ui;
                }
            }
            return ui;
        };

        self.checkAggregateOpenCloseButtonNextState = function (state, control, pager, total) {
            var allOpen = true;
            var allClosed = true;
            if (!pager) {
                return false;
            }
            for (var i = pager.startIndex; i < pager.endIndex + 1; i++) {
                if (i < total && control[i] && control[i].aapen) {
                    allClosed = false;
                } else {
                    allOpen = false;
                }
            }
            if (state && allClosed || !state && allOpen)  {
                return !state;
            } else {
                return state;
            }
        };

        self.toggleFane = function (control, index) {
            control[index] = control[index] || {};
            control[index].aapen = !control[index].aapen;
        };
}]);