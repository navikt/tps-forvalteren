
angular.module('tps-vedlikehold.service-rutine')
    .controller('ServiceRutineCtrl', ['$scope', '$stateParams', '$mdDialog', '$document', 'utilsService', 'serviceRutineFactory', 'environmentsPromise',
        function ($scope, $stateParams, $mdDialog, $document, utilsService, serviceRutineFactory, environmentsPromise) {

            $scope.serviceRutineName = $stateParams.serviceRutineName;
            $scope.loading = false;
            $scope.formData = {};
            $scope.fields = [];
            $scope.selectValues = {};
            $scope.onlyNumbers = /^\d+$/;
            $scope.onlyLetters = /^[a-zA-Z0-9\s]*$/;
            $scope.personsData = {};
            $scope.toggle = false;
            $scope.isArray = angular.isArray;

            var nonUniqueProperties = [];
            var requiredParameters = [];
            var isValidServiceRutineName = false;
            var apiError = true;

            $scope.isArrays = function(arr){
            };

            $scope.loadServiceRutineTemplate = function () {
                return isValidServiceRutineName && !apiError;
            };

            //TODO Må nullstille "buffer" når man tar et nytt søk og ikke bare pager i buffer.
            $scope.submit = function () {
            var params = createParams($scope.formData);
            // var params = {fnr:"110884", aksjonsDato:"2017-04-04", aksjonsKode:"C0", environment:"t0"};

                $scope.loading = true;

                serviceRutineFactory.getServiceRutineResponse($scope.serviceRutineName, params).then(function (res) {

                    $scope.loading = false;
                    $scope.clearResponseForm();

                    var response = res.data.response;
                    var xml = res.data.xml;

                    $scope.xmlForm = utilsService.formatXml(xml);

                    $scope.svarStatus = "STATUS: " + response.status.kode + " " + response.status.melding + " " + response.status.utfyllendeMelding;
                    $scope.returStatus = response.status.kode;
                    if(response.data === undefined) return;
                    $scope.personsData = extractPersonsData(response, nonUniqueProperties);

                    var str = JSON.stringify($scope.personsData, null, 2);    
                //    console.log(str);

                    // capitalizeFirstLetterInPersonsData(response);   //TODO Må kanskje fjernes. Spørs skal se ut
                    var antallTreff = response.antallTotalt;
                    if(antallTreff === undefined || antallTreff == 1) $scope.toggle = true;
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

            //TODO Denne får nullpointer ganske ofte fordi "text" er undefined ofte.
            $scope.resolveDisplayTemplate = function(text, personData) {
                var pattern = /\$\{(.)*?}/g;
                // console.log("Text: "+ text);
                var matches = text.match(pattern);
                // console.log("Matches: " + matches);

                angular.forEach(matches, function(val) {
                    var objKey = val.slice(2, -1);
                    text = text.replace(val, personData[objKey]);
                });
                return text;
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

            function extractPersonsData(responseObject, nonUniqueProperties) {
                var personsData = {};
                if (responseObject.antallTotalt === undefined || responseObject.antallTotalt == 1) {
                    personsData[0] = utilsService.flattenObject(responseObject.data[0], nonUniqueProperties);
                    utilsService.lagArray(personsData[0]);
                } else {
                    var i = 0;
                    while (i < responseObject.antallTotalt) {
                        personsData[i] = utilsService.flattenObject(responseObject.data[i], nonUniqueProperties);
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
                var selectValues = serviceRutineFactory.getSelectValuesServiceRutine($scope.serviceRutineName);

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
                        case 'datoNyttNavn':
                        case 'aksjonsDato':
                        case 'datoTom':
                        case 'datogiroNrNorsk':
                        case 'datoGiroNr':
                            $scope.formData[parameter] = utilsService.getCurrentFormattedDate();
                            break;
                        case 'aksjonsKode':
                        case 'adresseTypeS103':
                        case 'kilde':
                        case 'buffNr':
                            $scope.formData[parameter] = $scope.selectValues[parameter][0];
                            break;
                        default:
                            $scope.formData[parameter] = '';
                    }
                }
                $scope.formData.environment = $scope.environments ? $scope.environments[0] : null;
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

                serviceRutineFactory.getServiceRoutineConfig($scope.serviceRutineName).then(function (res){
                    $scope.responseFormConfig = res.data;
                    nonUniqueProperties = res.data[$scope.serviceRutineName].nonUniqueProperties;

                });

                getServiceRutineInputFieldName();
                getServiceRutineRequiredParametersNames();

                setSelectValues();
                //formatSelectValues();

                initRequestForm();
                overwriteTabFocusBehaviour();
            }

            init();
        }]);
