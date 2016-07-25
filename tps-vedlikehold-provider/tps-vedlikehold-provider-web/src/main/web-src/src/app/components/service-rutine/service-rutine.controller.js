/**
 * @author Frederik de Lichtenberg (Visma Consulting AS).
 */
angular.module('tps-vedlikehold.service-rutine')
    .controller('ServiceRutineCtrl', ['$scope', '$stateParams', '$mdDialog', 'utilsService', 'serviceRutineFactory', 'formConfig', 'environmentsPromise',
        function($scope, $stateParams, $mdDialog, utilsService, serviceRutineFactory, formConfig, environmentsPromise) {

            $scope.serviceRutineName = $stateParams.serviceRutineName;
            $scope.formData = {};
            $scope.fields = [];
            $scope.formConfig = formConfig;
            $scope.onlyNumbers = /^\d+$/;

            var tpsReturnedObject = {};
            var nonUniqueProperties = []; //objects that contain non-unique properties
            var requiredAttributes = [];
            var isValidServiceRutineName = false;
            var apiError = true;

            $scope.loadServiceRutineTemplate = function () {
                return isValidServiceRutineName && !apiError;
            };

            $scope.submit = function() {
                var params = createParams($scope.formData);

                serviceRutineFactory.getResponse($scope.serviceRutineName, params).then(function(res) {
                    $scope.xmlForm = utilsService.formatXml(res.data.xml);
                    tpsReturnedObject = res.data.data;

                    var svarStatus = tpsReturnedObject.tpsSvar.svarStatus;
                    var message = "STATUS: " + svarStatus.returStatus + " " +  svarStatus.returMelding + " " +  svarStatus.utfyllendeMelding;
                    $scope.svarStatus = message;
                    $scope.returStatus = svarStatus.returStatus;

                    $scope.personData = utilsService.flattenObject(tpsReturnedObject
                        .tpsSvar[serviceRutineFactory.getServiceRutineReturnedDataLabel($scope.serviceRutineName)],
                        nonUniqueProperties);

                }, function(error) {
                    showAlertTPSError();
                });
            };

            $scope.clearResponseForm = function() {
                $scope.personData = {};
                $scope.svarStatus = null;
            };

            $scope.isRequired = function(type) {
                return (requiredAttributes.indexOf(type) > -1);
            };

            function showAlertTPSError() {
                $mdDialog.show(
                    $mdDialog.alert()
                        .title('Serverfeil')
                        .textContent('Fikk ikke hentet informasjon om TPS fra server.')
                        .ariaLabel('Feil ved henting av data fra TPS')
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

            function createParams(formData) {
                var params = {};
                for (var key in formData) {
                    if (formData.hasOwnProperty(key)) {
                        switch(key) {
                            case 'aksjonsDato':
                                params.aksjonsDato = checkDate(formData.aksjonsDato);
                                break;
                            case 'aksjonsKode':
                                params.aksjonsKode = formData.aksjonsKode.charAt(0);
                                params.aksjonsKode2 = formData.aksjonsKode.charAt(1);
                                break;
                            default:
                                params[key] = formData[key];
                        }
                    }
                }
                return params;
            }

            function checkDate(aksjonsDato) {
                var dato = aksjonsDato;
                var inFuture = utilsService.isInFuture(aksjonsDato);

                if (!aksjonsDato || inFuture) {
                    var today = new Date();
                    $scope.formData.aksjonsDato = today;
                    dato = !aksjonsDato ? null : today;
                }
                return utilsService.formatDate(dato);
            }
            
            function getServiceRutineAttributesNames() {
                $scope.fields = serviceRutineFactory.getServiceRutineAttributesNames($scope.serviceRutineName);
                if (serviceRutineFactory.hasAksjonsKodes($scope.serviceRutineName)) {
                    $scope.fields.push('aksjonsKode');
                }
            }
            
            function setIsValidServiceRutineName() {
                isValidServiceRutineName = ($scope.serviceRutineName in serviceRutineFactory.getServiceRutines());
            }
            
            function getServiceRutineRequiredAttributesNames() {
                requiredAttributes = serviceRutineFactory.getServiceRutineRequiredAttributesNames($scope.serviceRutineName);
            }
            
            function getServiceRutineAksjonsKodes() {
                if (serviceRutineFactory.hasAksjonsKodes($scope.serviceRutineName)) {
                    $scope.aksjonsKodes = serviceRutineFactory.getServiceRutineAksjonsKodes($scope.serviceRutineName).sort();
                }
            }

            function getNonUniqueProperties() {
                nonUniqueProperties = serviceRutineFactory.getNonUniqueProperties($scope.serviceRutineName);
            }
            
            function initRequestForm() {
                for (var i = 0; i < $scope.fields.length; i++) {
                    var attribute = $scope.fields[i];
                    switch(attribute) {
                        case 'fnr': 
                            $scope.formData.fnr = '';
                            break;
                        case 'aksjonsDato':
                            $scope.formData.aksjonsDato = new Date();
                            break;
                        case 'aksjonsKode':
                            $scope.formData.aksjonsKode = $scope.aksjonsKodes[0];
                            break;
                        default:
                            $scope.formData[attribute] = ''; //
                    }
                }
                $scope.formData.environment = $scope.environments ? $scope.environments[0] : null;
            }

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

                getServiceRutineAttributesNames();
                getServiceRutineRequiredAttributesNames();
                getServiceRutineAksjonsKodes();
                getNonUniqueProperties();
                initRequestForm();
            }

            init();
        }]);
