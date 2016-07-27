/**
 * @author Frederik de Lichtenberg (Visma Consulting AS).
 */
angular.module('tps-vedlikehold.service-rutine')
    .controller('ServiceRutineCtrl', ['$scope', '$stateParams', '$mdDialog', '$document', 'utilsService', 'serviceRutineFactory', 'responseFormConfig', 'environmentsPromise',
        function($scope, $stateParams, $mdDialog, $document, utilsService, serviceRutineFactory, responseFormConfig, environmentsPromise) {

            $scope.serviceRutineName = $stateParams.serviceRutineName;
            $scope.loading = false;
            $scope.formData = {};
            $scope.fields = [];
            $scope.selectValues = {};
            $scope.responseFormConfig = responseFormConfig;
            $scope.onlyNumbers = /^\d+$/;

            var tpsReturnedObject = {};
            var nonUniqueProperties = []; //objects that contain non-unique properties
            var requiredParameters = [];
            var isValidServiceRutineName = false;
            var apiError = true;

            $scope.loadServiceRutineTemplate = function () {
                return isValidServiceRutineName && !apiError;
            };

            $scope.submit = function() {
                var params = createParams($scope.formData);
                $scope.loading = true;

                serviceRutineFactory.getResponse($scope.serviceRutineName, params).then(function(res) {
                    $scope.loading = false;
                    $scope.xmlForm = utilsService.formatXml(res.data.xml);

                    tpsReturnedObject = res.data.data;

                    var svarStatus = tpsReturnedObject.tpsSvar.svarStatus;
                    $scope.svarStatus = "STATUS: " + svarStatus.returStatus + " " +  svarStatus.returMelding + " " +  svarStatus.utfyllendeMelding;
                    $scope.returStatus = svarStatus.returStatus;

                    $scope.personData = utilsService.flattenObject(tpsReturnedObject
                        .tpsSvar[serviceRutineFactory.getServiceRutineReturnedDataLabel($scope.serviceRutineName)],
                        nonUniqueProperties);

                }, function(error) {
                    $scope.loading = false;
                    showAlertTPSError(error);
                });
            };

            $scope.clearResponseForm = function() {
                $scope.personData = {};
                $scope.svarStatus = null;
                $scope.xmlForm = null;
            };

            $scope.isRequired = function(type) {
                return (requiredParameters.indexOf(type) > -1);
            };

            $scope.repositionDatePicker = function(pika){
                var parentScrollOffset = Math.abs(parseInt($document[0].body.style.top));
                parentScrollOffset = parentScrollOffset ? parentScrollOffset : 0;
                var modalInputOffset = pika.el.offsetTop;
                pika.el.style.top = parentScrollOffset+modalInputOffset+'px';
            };

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

            // ##################################
            // These function will maybe need additions when adding new input fields
            // See confluence for info

            function formatSelectValues() {
                if ($scope.selectValues.aksjonsKode) {
                    $scope.selectValues.aksjonsKode.sort();
                }
            }
            
            function initRequestForm() {
                for (var i = 0; i < $scope.fields.length; i++) {
                    var parameter = $scope.fields[i];
                    switch(parameter) {
                        case 'fnr': 
                            $scope.formData.fnr = '';
                            break;
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
            }

            init();
        }]);
