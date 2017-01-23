/**
 * Peter Fløgstad Visma Consulting AS
 * */

angular.module('tps-vedlikehold.testdata')
    .controller('TestDataCtrl', ['$scope', 'serviceRutineFactory',
        function($scope, serviceRutineFactory) {

            $scope.formFields = ["aksjonsdato", "antalltestbrukere"];
            $scope.formData = {};
            $scope.fnrListe = [];

            $scope.submit = function(){
                var params = createParams($scope.formData);
                serviceRutineFactory.getTestdataResponse(params).then(function (res) {
                    //$scope.loading = false;

                    var response = res.data.response;
                    var res_out = JSON.stringify(response, null,2);
                    console.log(res_out);

                    $scope.fnrListe = response.data;

                }, function (error) {
                    //$scope.loading = false;
                });
            };

            function createParams(formData) {
                var params = {};
                for (var key in formData) {
                    if (formData.hasOwnProperty(key) && formData[key]) {
                        params[key] = formData[key];
                    }
                }
                return params;
            }
        }]);
