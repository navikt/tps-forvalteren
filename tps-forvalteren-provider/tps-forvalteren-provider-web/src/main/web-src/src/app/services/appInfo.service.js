angular.module('tps-forvalteren.service')
    .service('appInfoService', ['$http', '$q', function($http, $q) {
        var self =  this;
        var url = 'api/v1/appinfo/';

        self.getInfo = function(id){
            var defer = $q.defer();
            $http.get(url).then(
                function (data) {
                    defer.resolve(data);
                },
                function (error) {
                    defer.reject(error);
                }
            );
            return defer.promise;
        };
    }]);