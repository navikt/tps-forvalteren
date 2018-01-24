angular.module('tps-forvalteren.doedsmeldinger', ['ngMaterial'])
    .controller('SendDoedsmeldingerCtrl', ['$scope', '$mdDialog', '$rootScope', '$stateParams', 'locationService', 'utilsService', 'headerService', 'doedsmeldingerService',
        function ($scope, $mdDialog, $rootScope, $stateParams, locationService, utilsService, headerService, doedsmeldingerService) {

            headerService.setHeader('Dødsmelding');

            $scope.formData = {};
            $scope.mockDB = [];
            $scope.params = {};

            $scope.submit = function() {
                $scope.params = utilsService.createParametersFromFormData($scope.formData)

                console.log($scope.params);

                $scope.loggTilMockDB($scope.params)



            };

            $scope.clearForm = function() {

            };



            var init = function() {
                var environments = $scope.$resolve.environmentsPromise;
                if(environments.status !== undefined){
                    utilsService.showAlertError(environments);
                } else {
                    $scope.environments = utilsService.sortEnvironments(environments.environments);
                }

                // USING MOCK
                $scope.populerMockDB();
                $scope.hentFraMockDB();

            };

            // MOCK FUNCTIONS

            $scope.loggTilMockDB = function(params) {
                $scope.mockDB.push(params);
                console.log("mockDB: " + $scope.mockDB);

                var personTilMockDB = '<?xml version="1.0" encoding="ISO-8859-1"?>' +
                                        '<sfePersonData xmlns="http://www.rtv.no/NamespaceSFE" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"xsi:schemaLocation="http://www.rtv.no/NamespaceSFE"> ' +
                                            '<sfeAjourforing> ' +
                                                '<systemInfo> ' +
                                                    '<kilde>BI00</kilde> ' +
                                                    '<brukerID>HMA2970</brukerID> ' +
                                                '</systemInfo> ' +
                                                '<endreDodsdato> ' +
                                                    '<offentligIdent>' + $scope.params.fnrInput+'</offentligIdent> ' +
                                                    '<dodsDato>2005-06-10</dodsDato> ' +
                                                '</endreDodsdato> ' +
                                            '</sfeAjourforing> ' +
                                            '<sfeTilbakeMelding> ' +
                                                '<svarStatus> ' +
                                                    '<returStatus>00</returStatus> ' +
                                                    '<returMelding> </returMelding> ' +
                                                    '<utfyllendeMelding> </utfyllendeMelding> ' +
                                                '</svarStatus> ' +
                                            '</sfeTilbakeMelding> ' +
                                        '</sfePersonData>'


                console.log("persontil MOck: " + $scope.params.getElementsByName("fnr"));
            };

            $scope.populerMockDB = function() {
                $scope.mockDB.push('<?xml version="1.0" encoding="ISO-8859-1"?><sfePersonData xmlns="http://www.rtv.no/NamespaceSFE" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"xsi:schemaLocation="http://www.rtv.no/NamespaceSFE"> <sfeAjourforing> <systemInfo> <kilde>BI00</kilde> <brukerID>HMA2970</brukerID> </systemInfo> <endreDodsdato> <offentligIdent>26217300346</offentligIdent> <dodsDato>2005-06-10</dodsDato> </endreDodsdato> </sfeAjourforing> <sfeTilbakeMelding> <svarStatus> <returStatus>00</returStatus> <returMelding> </returMelding> <utfyllendeMelding> </utfyllendeMelding> </svarStatus> </sfeTilbakeMelding> </sfePersonData>',
                                    '<?xml version="1.0" encoding="ISO-8859-1"?><sfePersonData xmlns="http://www.rtv.no/NamespaceSFE" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"xsi:schemaLocation="http://www.rtv.no/NamespaceSFE"> <sfeAjourforing> <systemInfo> <kilde>BI00</kilde> <brukerID>HMA2970</brukerID> </systemInfo> <endreDodsdato> <offentligIdent>26217300347</offentligIdent> <dodsDato>2005-06-11</dodsDato> </endreDodsdato> </sfeAjourforing> <sfeTilbakeMelding> <svarStatus> <returStatus>00</returStatus> <returMelding> </returMelding> <utfyllendeMelding> </utfyllendeMelding> </svarStatus> </sfeTilbakeMelding> </sfePersonData>',
                                    '<?xml version="1.0" encoding="ISO-8859-1"?><sfePersonData xmlns="http://www.rtv.no/NamespaceSFE" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"xsi:schemaLocation="http://www.rtv.no/NamespaceSFE"> <sfeAjourforing> <systemInfo> <kilde>BI00</kilde> <brukerID>HMA2970</brukerID> </systemInfo> <endreDodsdato> <offentligIdent>26217300348</offentligIdent> <dodsDato>2005-06-12</dodsDato> </endreDodsdato> </sfeAjourforing> <sfeTilbakeMelding> <svarStatus> <returStatus>00</returStatus> <returMelding> </returMelding> <utfyllendeMelding> </utfyllendeMelding> </svarStatus> </sfeTilbakeMelding> </sfePersonData>');
            };

            $scope.hentFraMockDB = function() {
                for(var i = 0; i < $scope.mockDB.length; i++ ) {
                    var person = utilsService.formatXml($scope.mockDB[i]);
                    console.log(person);
                    //console.log(person.)
                }
            };



            init();

        }]);