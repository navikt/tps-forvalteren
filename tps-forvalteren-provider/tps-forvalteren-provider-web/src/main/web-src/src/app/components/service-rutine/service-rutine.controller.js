angular.module('tps-forvalteren.service-rutine', ['ngMessages', 'hljs'])
    .controller('ServiceRutineCtrl', ['$scope', '$stateParams', '$mdDialog', '$document', 'utilsService', 'serviceRutineFactory', 'environmentsPromise','locationService', 'headerService',
        function ($scope, $stateParams, $mdDialog, $document, utilsService, serviceRutineFactory, environmentsPromise, locationService, headerService, underHeaderService) {

            headerService.setHeader('Servicerutiner');

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

            var requiredParameters = [];
            var isValidServiceRutineName = false;
            var apiError = true;

            $scope.loadServiceRutineTemplate = function () {
                return isValidServiceRutineName && !apiError;
            };

            $scope.openServiceRutine = function (rutine) {
                locationService.redirectToServiceRutineState(rutine);
            };

            $scope.submit = function () {
            var params = utilsService.createParametersFromFormData($scope.formData);

                $scope.loading = true;

                serviceRutineFactory.getServiceRutineResponse($scope.serviceRutineName, params).then(function (res) {
                    $scope.loading = false;
                    $scope.clearResponseForm();

                    var response = res.data.response;
                    var xml = res.data.xml;

                    $scope.xmlForm = utilsService.formatXml(xml);

                    $scope.svarStatus = "STATUS: " + response.status.kode + " " + response.status.melding + " " + response.status.utfyllendeMelding;
                    $scope.returStatus = response.status.kode;

                    $scope.responseData = response;

                    $scope.adresseHistorikk = response.data1;

                    /* Brukes kun til å hente Tags som skal inn i servicerutinenes html fil */
                    // var jup = utilsService.flattenObject($scope.responseData);
                    // var str = JSON.stringify(jup, null, 2);
                    // console.log(str);

                    if(response.data === undefined) return;
                    //$scope.personsData = extractPersonsData(response, nonUniqueProperties);

                    var antallTreff = response.antallTotalt;
                    if(antallTreff === undefined || antallTreff == 1) $scope.toggle = true;
                }, function (error) {
                    $scope.loading = false;
                    showAlertTPSError(error);
                });

            };

            headerService.setButtons([{
                icon: 'assets/icons/keyboard_arrow_down.svg',
                text: "Velg servicerutine",
                click: function (ev) {
                    var confirm = $mdDialog.confirm({
                        controller: 'VelgServiceRutineCtrl',
                        templateUrl: 'app/components/service-rutine/velg-service-rutine/velg-service-rutine.html',
                        parent: angular.element(document.body),
                        targetEvent: ev,
                        clickOutsideToClose:true
                    });
                    $mdDialog.show(confirm);
                }
            }]);

            $scope.clearResponseForm = function () {
                $scope.personsData = {};
                $scope.toggle = false;
                $scope.svarStatus = null;
                $scope.xmlForm = null;
                $scope.adresseHistorikk = null;
                $scope.responseData = null;
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

            $scope.resolveDisplayTemplate = function(text, personData) {
                var pattern = /\$\{(.)*?}/g;
                var matches = text.match(pattern);

                angular.forEach(matches, function(val) {
                    var objKey = val.slice(2, -1);
                    text = text.replace(val, personData[objKey]);
                });
                return text;
            };

            $scope.resolveArray = function(arrayprefix, index, arrayLength, value){
                if(arrayLength === 1){
                    return arrayprefix + "." + value;
                }
                return arrayprefix + "[" + index + "]" + "." + value;
            };

            $scope.getNumber = function(antall){
                return new Array(parseInt(antall));
            };

            $scope.pageForward = function pageForward(){
               $scope.formData.buffNr = (parseInt($scope.formData.buffNr)+1).toString();
                $scope.submit();
            };

            $scope.pageBackwards = function pageBackwards(){
                $scope.formData.buffNr = (parseInt($scope.formData.buffNr)-1).toString();
                $scope.submit();
            };

            $scope.getStartIndex = function (buffer){
                var i = parseInt(buffer);
                return ((i-1)*34 + 1);
            };

            $scope.getEndIndex = function (buffer, antallTreff){
                var bufferNummer = parseInt(buffer);
                if(bufferNummer*34 > antallTreff) return antallTreff;
                return 34*bufferNummer;
            };

            $scope.openGT = function () {
                locationService.redirectToGT();
            };

            $scope.openVisTestdata = function () {
                locationService.redirectToVisTestdata();
            };

            function extractPersonsData(responseObject, nonUniqueProperties) {
                var personsData = {};
                if (responseObject.antallTotalt === undefined || responseObject.antallTotalt === 1) {
                    personsData[0] = utilsService.flattenObject(responseObject.data[0], nonUniqueProperties);
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

                var errorObj = error.status === 401 ? errorMessages[401] : errorMessages[500];
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
                $scope.fields = serviceRutineFactory.getServiceRutineParametersNames($scope.serviceRutineName);
            }

            function setIsValidServiceRutineName() {
                isValidServiceRutineName = ($scope.serviceRutineName in serviceRutineFactory.getServiceRutines());
            }

            function getServiceRutineRequiredParametersNames() {
                requiredParameters = serviceRutineFactory.getServiceRutineRequiredParametersNames($scope.serviceRutineName);
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
                        case 'infoType':
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
                    $scope.environments = serviceRutineFactory.getEnvironments();
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

                $scope.serviceRutineNameHtmlUrl =  serviceRutineFactory.getServiceRoutineHtmlUrl($scope.serviceRutineName);

                getServiceRutineInputFieldName();
                getServiceRutineRequiredParametersNames();

                setSelectValues();

                initRequestForm();
                overwriteTabFocusBehaviour();
            }

            init();
        }]);
