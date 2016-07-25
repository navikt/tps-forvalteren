/**
 * @author Frederik de Lichtenberg (Visma Consulting AS).
 */
angular.module('tps-vedlikehold.servicerutine', ['ngMessages', 'hljs'])

    .controller('servicerutineController', ['$scope', '$stateParams', '$mdDialog', 'utilsService', 'servicerutineFactory', 'formConfig', 'environmentsPromise',
        function($scope, $stateParams, $mdDialog, utilsService, servicerutineFactory, formConfig, environmentsPromise) {

            $scope.serviceRutinenavn = $stateParams.serviceRutinenavn;
            $scope.loading = false;
            $scope.formData = {};
            $scope.fields = [];
            $scope.formConfig = formConfig;
            $scope.onlyNumbers = /^\d+$/;

            var tpsReturnedObject = {};
            var nonUniqueProperties = []; //objects that contain non-unique properties
            var requiredAttributes = [];
            var isValidServiceRutinenavn = false;
            var apiError = true;

            $scope.loadServicerutineTemplate = function () {
                return isValidServiceRutinenavn && !apiError;
            };

            $scope.submit = function() {
                var params = createParams($scope.formData);
                $scope.loading = true;
                servicerutineFactory.getResponse($scope.serviceRutinenavn, params).then(function(res) {
                    $scope.loading = false;
                    $scope.xmlForm = utilsService.formatXml(res.data.xml);
                    tpsReturnedObject = res.data.data;

                    var svarStatus = tpsReturnedObject.tpsSvar.svarStatus;
                    var message = "STATUS: " + svarStatus.returStatus + " " +  svarStatus.returMelding + " " +  svarStatus.utfyllendeMelding;
                    $scope.svarStatus = message;
                    $scope.returStatus = svarStatus.returStatus;

                    $scope.personData = utilsService.flattenObject(tpsReturnedObject
                        .tpsSvar[servicerutineFactory.getServicerutineReturnedDataLabel($scope.serviceRutinenavn)],
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
                return (requiredAttributes.indexOf(type) > -1);
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
            
            function getServicerutineInputFieldNames() {
                $scope.fields = servicerutineFactory.getServicerutineAttributesNames($scope.serviceRutinenavn);

                if (servicerutineFactory.hasAksjonsKodes($scope.serviceRutinenavn)) {
                    $scope.fields.push('aksjonsKode');
                }
            }
            
            function setIsValidServiceRutinenavn() {
                isValidServiceRutinenavn = ($scope.serviceRutinenavn in servicerutineFactory.getServicerutiner());
            }
            
            function getServicerutineRequiredAttributesNames() {
                requiredAttributes = servicerutineFactory.getServicerutineRequiredAttributesNames($scope.serviceRutinenavn);
            }
            
            function getServicerutineAksjonsKodes() {
                if (servicerutineFactory.hasAksjonsKodes($scope.serviceRutinenavn)) {
                    $scope.aksjonsKodes = servicerutineFactory.getServicerutineAksjonsKodes($scope.serviceRutinenavn).sort();
                }
            }

            function getNonUniqueProperties() {
                nonUniqueProperties = servicerutineFactory.getNonUniqueProperties($scope.serviceRutinenavn);
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
                setIsValidServiceRutinenavn();

                //better way to do this?
                if (!isValidServiceRutinenavn) {
                    return;
                }

                if (environmentsPromise) {
                    $scope.environments = utilsService.sortEnvironments(servicerutineFactory.getEnvironments());
                    apiError = false;
                } else {
                    apiError = true;
                    showAlertApiError();
                    return;
                }

                if (!servicerutineFactory.isSetServicerutiner()) {
                    apiError = true;
                    return;
                }

                getServicerutineInputFieldNames();
                getServicerutineRequiredAttributesNames();
                getServicerutineAksjonsKodes();
                getNonUniqueProperties();
                initRequestForm();
            }

            init();
        }]);
