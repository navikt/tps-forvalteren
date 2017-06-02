
angular.module('tps-forvalteren.service')
    .service('testdataService', ['$http', '$q', function($http, $q) {

        var self =  this;
        var url = 'api/v1/testdata/';

        self.getTestpersoner = function(){
            var defer = $q.defer();
            $http.get(url + "personer").then(
                function (data) {
                    defer.resolve(data);
                },
                function (error) {
                    defer.reject(error);
                }
            );
            return defer.promise;
        };

        self.postTestpersoner = function(kriterier){
            var defer = $q.defer();
            $http.post(url + "personer", kriterier).then(
                function (data) {
                    defer.resolve(data);
                },
                function (error) {
                    defer.reject(error);
                }
            );
            return defer.promise;
        };

        self.deleteTestpersoner = function(identer){
            var defer = $q.defer();
            $http.delete(url + "personer", {params: {personIdListe: identer}}).then(
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
