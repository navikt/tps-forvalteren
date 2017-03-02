/**
 * Peter Fløgstad Visma Consulting AS
 * */

angular.module('tps-vedlikehold.testdata')
    .controller('TestDataCtrl', ['$scope', '$mdDialog', 'serviceRutineFactory', 'environmentsPromise','utilsService',
        function($scope, $mdDialog, serviceRutineFactory,environmentsPromise, utilsService) {

            //TODO Laget noen arrays bare for testing
            $scope.formFields = ["aksjonsdato", "antalltestbrukere"];   //TODO lag på annen måte senere

            $scope.formData = {};
            $scope.fnrListe = [];
            $scope.testpersoner = [];
            $scope.editMode = false;

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

            $scope.loadTestpersoner = function(){
                serviceRutineFactory.loadTestdataPersoner({}).then(function (responseObject) {
                    var responseData = responseObject.data;
                    $scope.testpersoner = responseData.response;

                }, function (error) {

                });
            };

            $scope.updateTestdataPersoner = function () {
                //var data = {data: $scope.testpersoner};
                var data = {"testi": "test"};
                serviceRutineFactory.updateTestdataPersoner(data).then(function (responseObject){
                    str = JSON.stringify(responseObject, null, 2);
                    console.log("Tester:" + str);
                }, function(error){

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

                $scope.loadTestpersoner();
            }

            // Run Init
            init();
        }]);
