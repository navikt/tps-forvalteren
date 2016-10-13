/**
 * @author Frederik de Lichtenberg (Visma Consulting AS).
 */
angular.module('tps-vedlikehold.service-rutine')
    .controller('ServiceRutineCtrl', ['$scope', '$stateParams', '$mdDialog', '$document', 'utilsService', 'serviceRutineFactory', 'responseFormConfig', 'environmentsPromise',
        function ($scope, $stateParams, $mdDialog, $document, utilsService, serviceRutineFactory, responseFormConfig, environmentsPromise) {

            $scope.serviceRutineName = $stateParams.serviceRutineName;
            $scope.loading = false;
            $scope.formData = {};
            $scope.fields = [];
            $scope.selectValues = {};
            $scope.responseFormConfig = responseFormConfig;
            $scope.onlyNumbers = /^\d+$/;
            $scope.onlyLetters = /^[a-zA-Z0-9\s]*$/;
            $scope.personsData = {};
            $scope.toggle = false;
            $scope.buffNumbers = ["1"];

            var tpsReturnedObject = {};
            var nonUniqueProperties = []; //objects that contain non-unique properties
            var requiredParameters = [];
            var isValidServiceRutineName = false;
            var apiError = true;

            $scope.loadServiceRutineTemplate = function () {
                return isValidServiceRutineName && !apiError;
            };

            $scope.submit = function () {
                var params = createParams($scope.formData);
                $scope.loading = true;

                serviceRutineFactory.getResponse($scope.serviceRutineName, params).then(function (response) {
                    $scope.loading = false;
                    $scope.clearResponseForm();

                    $scope.xmlForm = utilsService.formatXml(response.data.xml);

                    tpsReturnedObject = response.data.data.tpsPersonData;

                    var svarStatus = tpsReturnedObject.tpsSvar.svarStatus;
                    $scope.svarStatus = "STATUS: " + svarStatus.returStatus + " " + svarStatus.returMelding + " " + svarStatus.utfyllendeMelding;
                    $scope.returStatus = svarStatus.returStatus;

                    var jsonObjectWithResultData = tpsReturnedObject.tpsSvar[serviceRutineFactory.getServiceRutineReturnedDataLabel($scope.serviceRutineName)];
                    if(jsonObjectWithResultData === undefined) return;
                    $scope.personsData = extractPersonsData(jsonObjectWithResultData, nonUniqueProperties);
                    capitalizeFirstLetterInPersonsData(jsonObjectWithResultData);
                    var antallTreff = jsonObjectWithResultData.antallTotalt;
                    if(antallTreff === undefined || antallTreff == 1) $scope.toggle = true;
                    else updateBufferChoices(antallTreff);

                }, function (error) {
                    $scope.loading = false;
                    showAlertTPSError(error);
                });
            };

            $scope.clearResponseForm = function () {
                $scope.personsData = {};
                $scope.toggle = false;
                $scope.svarStatus = null;
                $scope.xmlForm = null;
            };

            $scope.isRequired = function (type) {
                return (requiredParameters.indexOf(type) > -1);
            };

            $scope.repositionDatePicker = function (pika) {
                var parentScrollOffset = Math.abs(parseInt($document[0].body.style.top));
                parentScrollOffset = parentScrollOffset ? parentScrollOffset : 0;
                var modalInputOffset = pika.el.offsetTop;
                pika.el.style.top = parentScrollOffset + modalInputOffset + 'px';
            };

            $scope.getAntallTreff = function(object){
                var count = 0;
                for(var key in object){
                    count++;
                }
                return count;
            };

            $scope.pageForward = function pageForward(){
               $scope.formData.buffNr = (parseInt($scope.formData.buffNr)+1).toString();
                $scope.submit();
            };

            $scope.pageBackwards = function pageBackwards(){
                $scope.formData.buffNr = (parseInt($scope.formData.buffNr)-1).toString();
                $scope.submit();
            };

            //TODO Bytt navn til noe mer beskrivende.
            $scope.getStartIndex = function (buffer){
                var i = parseInt(buffer);
                return ((i-1)*34 + 1);
            };

            $scope.getEndIndex = function (buffer, antallTreff){
                var bufferNummer = parseInt(buffer);
                if(bufferNummer*34 > antallTreff) return antallTreff;
                return 34*bufferNummer;
            };

            function capitalizeFirstLetterInPersonsData(responseObject){
                if (responseObject.antallTotalt === undefined) {
                    $scope.personsData[0] = utilsService.capitalizeFirstLetterInObjectProperties($scope.personsData[0]);
                } else {
                    var i = 0;
                    while (i < responseObject.antallTotalt) {
                        $scope.personsData[i] = utilsService.capitalizeFirstLetterInObjectProperties($scope.personsData[i]);
                        i++;
                    }
                }
            }

            function updateBufferChoices(antallTreff){
                var i = 0;
                $scope.buffNumbers = [];
                while(i < Math.ceil(parseInt(antallTreff)/34)){
                    $scope.buffNumbers[i] = (i+1).toString();
                    i++;
                }
            }

            function extractPersonsData(responseObject, nonUniqueProperties) {
                var personsData = {};
                if (responseObject.antallTotalt === undefined || responseObject.antallTotalt == 1) {
                    personsData[0] = utilsService.flattenObject(responseObject, nonUniqueProperties);
                } else {
                    var i = 0;
                    while (i < responseObject.antallTotalt) {
                        personsData[i] = utilsService.flattenObject(responseObject.enPersonRes[i], nonUniqueProperties);
                        i++;
                    }
                }
                return personsData;
            }

            function showAlertTPSError(error) {
                var errorMessages = {
                    401: {
                        title: 'Ikke autorisert',
                        text: 'Din bruker har ikke tillatelse til denne spørringen.',
                        ariaLabel: 'Din bruker har ikke tillatelse til denne spørringen.'
                    },
                    500: {
                        title: 'Serverfeil',
                        text: 'Fikk ikke hentet informasjon om TPS fra server.',
                        ariaLabel: 'Feil ved henting av data fra TPS'
                    }
                };

                var errorObj = error.status == 401 ? errorMessages[401] : errorMessages[500];
                $mdDialog.show(
                    $mdDialog.alert()
                        .title(errorObj.title)
                        .textContent(errorObj.text)
                        .ariaLabel(errorObj.ariaLabel)
                        .ok('OK')
                );
            }

            function showAlertApiError() {
                $mdDialog.show(
                    $mdDialog.alert()
                        .title('Serverfeil')
                        .textContent('Fikk ikke hentet informasjon om miljøer fra server.')
                        .ariaLabel('Feil ved henting av miljøer')
                        .ok('OK')
                );
            }

            function getServiceRutineInputFieldName() {
                $scope.fields = serviceRutineFactory.getServiceRutineParametersNamesInOrder($scope.serviceRutineName);
            }

            function setIsValidServiceRutineName() {
                isValidServiceRutineName = ($scope.serviceRutineName in serviceRutineFactory.getServiceRutines());
            }

            function getServiceRutineRequiredParametersNames() {
                requiredParameters = serviceRutineFactory.getServiceRutineRequiredParametersNames($scope.serviceRutineName);
            }

            function getNonUniqueProperties() {
                nonUniqueProperties = serviceRutineFactory.getNonUniqueProperties($scope.serviceRutineName);
            }

            function createParams(formData) {
                var params = {};
                for (var key in formData) {
                    if (formData.hasOwnProperty(key) && formData[key]) {
                        params[key] = formData[key];
                    }
                }
                return params;
            }

            function setSelectValues() {
                var selectValues = serviceRutineFactory.getSelectValues($scope.serviceRutineName);

                angular.forEach(selectValues, function (value, key) {
                    $scope.selectValues[key] = value;
                });
            }

            // This is needed in order to force the first tab to focus after refresh when navigating using the tab key.
            function overwriteTabFocusBehaviour() {
                angular.element(document).ready(function () {
                    var mdTabsCanvas = angular.element(document.querySelector(".tps-vk-scrollable-tabs"))[0].children[0].children[1];
                    var firstTab = mdTabsCanvas.children[0].children[0];
                    var done = false;

                    mdTabsCanvas.addEventListener("focusin", function (e) {
                        if (!done) {
                            firstTab.classList.add("md-focused");
                            done = true;
                        }
                    });


                    mdTabsCanvas.addEventListener("focusout", function (e) {
                        firstTab.classList.remove("md-focused");
                    });

                });
            }

            // ##################################
            // These functions will maybe need additions when adding new input fields
            // See confluence for info

            function formatSelectValues() {
                if ($scope.selectValues.aksjonsKode) {
                    $scope.selectValues.aksjonsKode.sort();
                }
            }

            function initRequestForm() {
                for (var i = 0; i < $scope.fields.length; i++) {
                    var parameter = $scope.fields[i];
                    switch (parameter) {
                        case 'aksjonsDato':
                            $scope.formData.aksjonsDato = utilsService.getCurrentFormattedDate();
                            break;
                        case 'aksjonsKode':
                            $scope.formData.aksjonsKode = $scope.selectValues.aksjonsKode[0];
                            break;
                        default:
                            $scope.formData[parameter] = '';
                    }
                }
                $scope.formData.environment = $scope.environments ? $scope.environments[0] : null;
                $scope.formData.buffNr = $scope.buffNumbers ? $scope.buffNumbers[0] : null;
            }

            // ##################################

            function init() {
                setIsValidServiceRutineName();

                //better way to do this?
                if (!isValidServiceRutineName) {
                    return;
                }

                if (environmentsPromise) {
                    $scope.environments = utilsService.sortEnvironments(serviceRutineFactory.getEnvironments());
                    apiError = false;
                } else {
                    apiError = true;
                    showAlertApiError();
                    return;
                }

                if (!serviceRutineFactory.isSetServiceRutines()) {
                    apiError = true;
                    return;
                }

                getServiceRutineInputFieldName();
                getServiceRutineRequiredParametersNames();

                setSelectValues();
                formatSelectValues();

                getNonUniqueProperties();
                initRequestForm();
                overwriteTabFocusBehaviour();
            }

            init();
        }]);
