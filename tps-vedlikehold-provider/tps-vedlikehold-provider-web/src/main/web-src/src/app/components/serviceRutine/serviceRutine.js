/**
 * @author Frederik de Lichtenberg (Visma Consulting AS).
 */
angular.module('tps-vedlikehold.servicerutine', ['ngMessages', 'hljs'])
    .controller('servicerutineController', ['$scope', '$stateParams', '$mdDialog', 'utilsService', 'servicerutineFactory', 'environmentsPromise',
        function($scope, $stateParams, $mdDialog, utilsService, servicerutineFactory, environmentsPromise) {
            
            $scope.serviceRutinenavn = $stateParams.serviceRutinenavn;
            $scope.formData = {};

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
                    $scope.svarStatus = tpsReturnedObject.tpsSvar.svarStatus;

                    $scope.personData = utilsService.flattenObject(tpsReturnedObject
                        .tpsSvar[servicerutineFactory.getServicerutineReturnedDataLabel($scope.serviceRutinenavn)],
                        nonUniqueProperties);
                }, function(error) {
                    showAlertTPSError();
                });
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
                params.aksjonsDato = utilsService.formatDate(formData.aksjonsDato);
                params.aksjonsKode = formData.aksjonsKode.charAt(0);
                params.aksjonsKode2 = formData.aksjonsKode.charAt(1);
                params.environment = formData.environment;
                return params;
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
                    $scope.environments = servicerutineFactory.getEnvironments().sort();
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
