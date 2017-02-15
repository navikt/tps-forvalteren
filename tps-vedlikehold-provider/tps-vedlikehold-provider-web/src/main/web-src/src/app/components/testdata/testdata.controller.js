/**
 * Peter Fløgstad Visma Consulting AS
 * */

angular.module('tps-vedlikehold.testdata')
    .controller('TestDataCtrl', ['$scope', '$mdDialog', 'serviceRutineFactory', 'environmentsPromise','utilsService',
        function($scope, $mdDialog, serviceRutineFactory,environmentsPromise, utilsService) {

            $scope.formFields = ["aksjonsdato", "antalltestbrukere"];   //TODO lag på annen måte senere
            $scope.formData = {};
            $scope.fnrListe = [];

            $scope.submit = function(){
                var params = createParams($scope.formData);
                serviceRutineFactory.getTestdataResponse(params).then(function (responseObject) {
                    //$scope.loading = false;
                    $scope.fnrListe = [];
                    for(var index in responseObject.data){
                       var response = responseObject.data[index].response;
                       $scope.fnrListe = $scope.fnrListe.concat(response.data);
                    }

                    var statusObject = responseObject.status;
                    var statusMelding = statusObject.melding;
                    var statusKode = statusObject.kode;
                    var statusUtfyllendeMelding = statusObject.utfyllendeMelding;
                    console.log("Status: " + statusMelding + " Kode: " + statusKode + " Utfyllende: " + statusUtfyllendeMelding);

                }, function (error) {
                    //$scope.loading = false;
                });
            };

            $scope.showTabDialog = function (event) {
                $mdDialog.show(
                    {
                        templateUrl: "app/components/testdata/identerTabDialog.tmpl.html",
                        targetEvent: event,
                        clickOutsideToClose:true
                    }
                );
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

            function init(){
                if (environmentsPromise) {
                    $scope.environments = utilsService.sortEnvironments(serviceRutineFactory.getEnvironments());
                    $scope.formData.environment = $scope.environments ? $scope.environments[0] : null;
                }
            }

            // Run Init
            init();
        }]);
