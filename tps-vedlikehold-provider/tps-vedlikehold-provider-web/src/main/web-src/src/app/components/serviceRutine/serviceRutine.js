/**
 * @author Frederik de Lichtenberg (Visma Consulting AS).
 */
angular.module('tps-vedlikehold.servicerutine', ['ngMessages', 'hljs'])

    .controller('servicerutineController', ['$scope', '$stateParams', '$mdDialog', 'utilsService', 'servicerutineFactory', 'formConfig', 'environmentsPromise',
        function($scope, $stateParams, $mdDialog, utilsService, servicerutineFactory, formConfig, environmentsPromise) {

            $scope.serviceRutinenavn = $stateParams.serviceRutinenavn;
            //$scope.isValidServiceRutinenavn = false;

            $scope.formData = {};
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

                servicerutineFactory.getResponse($scope.serviceRutinenavn, params).then(function(res) {
                    $scope.xmlForm = utilsService.formatXml(res.data.xml);
                    tpsReturnedObject = res.data.data;

                    var svarStatus = tpsReturnedObject.tpsSvar.svarStatus;
                    var message = "Status: " + svarStatus.returStatus + " " +  svarStatus.returMelding + " " +  svarStatus.utfyllendeMelding;
                    $scope.svarStatus = message;

                    $scope.personData = utilsService.flattenObject(tpsReturnedObject
                        .tpsSvar[servicerutineFactory.getServicerutineReturnedDataLabel($scope.serviceRutinenavn)],
                        nonUniqueProperties);

                }, function(error) {
                    showAlertTPSError();
                });
            };

            $scope.hideToast = function() {
                toast.hide();
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
                params.fnr = formData.fnr;
                params.aksjonsDato = checkDate(formData.aksjonsDato);
                if (formData.aksjonsKode) {
                    params.aksjonsKode = formData.aksjonsKode.charAt(0);
                    params.aksjonsKode2 = formData.aksjonsKode.charAt(1);
                } else {
                    params.aksjonsKode = 0;
                    params.aksjonsKode2 = 0;
                }

                params.environment = formData.environment;
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
            
            function getServicerutineAttributesNames() {
                $scope.fields = servicerutineFactory.getServicerutineAttributesNames($scope.serviceRutinenavn);
                $scope.fields.push('aksjonsKode');
            }
            
            function setIsValidServiceRutinenavn() {
                isValidServiceRutinenavn = ($scope.serviceRutinenavn in servicerutineFactory.getServicerutiner());
            }
            
            function getServicerutineRequiredAttributesNames() {
                requiredAttributes = servicerutineFactory.getServicerutineRequiredAttributesNames($scope.serviceRutinenavn);
            }
            
            function getServicerutineAksjonsKoder() {
                $scope.aksjonsKoder = servicerutineFactory.getServicerutineAksjonsKoder($scope.serviceRutinenavn).sort();
            }

            function getNonUniqueProperties() {
                nonUniqueProperties = servicerutineFactory.getNonUniqueProperties($scope.serviceRutinenavn);
            }
            
            function initRequestForm() {
                $scope.formData = {
                    fnr: '',
                    aksjonsDato: new Date(),
                    aksjonsKode: $scope.aksjonsKoder[0],
                    environment: $scope.environments[0]
                };
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

                getServicerutineAttributesNames();
                getServicerutineRequiredAttributesNames();
                getServicerutineAksjonsKoder();
                getNonUniqueProperties();
                initRequestForm();
            }

            init();
        }]);
