/**
 * @author Frederik de Lichtenberg (Visma Consulting AS).
 */
angular.module('tps-vedlikehold.servicerutine', ['ngMessages', 'hljs'])
    .controller('servicerutineCtrl', ['$scope', '$stateParams', '$mdDialog', 'utilsService', 'servicerutineFactory',
        function($scope, $stateParams, $mdDialog, utilsService, servicerutineFactory) {

            var tpsReturnedObject = {};
            
            $scope.serviceRutinenavn = $stateParams.serviceRutinenavn;
            $scope.isValidServiceRutinenavn = false;
            $scope.formData = {};

            //objects that contain non-unique properties
            var nonUniqueProperties = [];

            var requiredAttributes = [];

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
                    // cyclic?
                    // console.log(error);
                    showAlertTPSError();
                });
            };

            $scope.isRequired = function(type) {
                return (requiredAttributes.indexOf(type) > -1);
            };

            function showAlertTPSError() {
                var confirm = $mdDialog.confirm()
                    .title('Serverfeil')
                    .textContent('Fikk ikke hentet informasjon om TPS fra server. Vil du prøve igjen?')
                    .ariaLabel('Feil ved henting av data fra TPS')
                    .ok('Prøv igjen')
                    .cancel('Avbryt');
                $mdDialog.show(confirm).then(function () {
                    $scope.submit();
                });
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
                $scope.isValidServiceRutinenavn = ($scope.serviceRutinenavn in servicerutineFactory.getServicerutiner());
            }
            
            function getServicerutineRequiredAttributesNames() {
                requiredAttributes = servicerutineFactory.getServicerutineRequiredAttributesNames($scope.serviceRutinenavn);
            }
            
            function getServicerutineAksjonsKoder() {
                $scope.aksjonsKoder = servicerutineFactory.getServicerutineAksjonsKoder($scope.serviceRutinenavn).sort();
            }
            
            function getEnvironments() {
                $scope.environments = servicerutineFactory.getEnvironments().sort();
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
                if (!$scope.isValidServiceRutinenavn) {
                    return;
                }
                
                getServicerutineAttributesNames();
                getServicerutineRequiredAttributesNames();
                getServicerutineAksjonsKoder();
                getEnvironments();
                getNonUniqueProperties();
                initRequestForm();
            }

            init();
        }]);