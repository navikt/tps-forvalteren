angular.module('tps-forvalteren.filter')
    .filter('miljoefilter', function() {
    return function(items, miljoe) {

        if (!items || !miljoe || '' === miljoe || 'Velg' === miljoe) {
            return items;
        }

        return items.filter(function(element, index, array) {
            return element.miljoe === miljoe;
        });
    };
});