/**
 * @author Frederik de Lichtenberg (Visma Consulting AS).
 */
angular.module('tps-vedlikehold.servicerutine', ['ngMessages', 'hljs'])
    .controller('servicerutineCtrl', ['$scope', '$stateParams', 'utilsService', 'servicerutineFactory',
        function($scope, $stateParams, utilsService, servicerutineFactory) {

            var tpsReturnedObject = {};

            $scope.serviceRutinenavn = $stateParams.serviceRutinenavn;
            
            //
            $scope.responseReceived = true;//
            //
            
            $scope.isValidServiceRutinenavn = false;
            $scope.formData = {};

            //objects that contain non-unique properties
            var nonUniqueProperties = [];

            var requiredAttributes = [];

            $scope.submit = function() {
                var params = createParams($scope.formData);

                servicerutineFactory.getResponse($scope.serviceRutinenavn, params).then(function(res) {
                    $scope.responseReceived = true;
                    $scope.xmlForm = utilsService.formatXml(res.data.xml);
                    tpsReturnedObject = res.data.data;
                    $scope.svarStatus = tpsReturnedObject.tpsSvar.svarStatus;

                    $scope.personData = utilsService.flattenObject(tpsReturnedObject
                        .tpsSvar[servicerutineFactory.getServicerutineReturnedDataLabel($scope.serviceRutinenavn)],
                        nonUniqueProperties);
                }, function(error) {
                    //TODO
                    $scope.responseReceived = false;
                });

                //######################################################################
                //FOR TESTING
                // var res = {};
                // res.data= {
                //     "xml": "<?xml version=\"1.0\"?>\n<tpsPersonData xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n    <tpsServiceRutine>\n        <serviceRutinenavn>FS03-FDNUMMER-PERSDATA-O</serviceRutinenavn>\n        <fnr>29089346337</fnr>\n        <aksjonsDato>2001-03-04</aksjonsDato>\n        <aksjonsKode>A</aksjonsKode>\n        <aksjonsKode2>0</aksjonsKode2>\n    </tpsServiceRutine> <tpsSvar><svarStatus><returStatus>00</returStatus><returMelding> </returMelding><utfyllendeMelding> </utfyllendeMelding></svarStatus><personDataS004><fnr>29089346337</fnr><kortnavn>GRIMNES ØYVIND KJELDSTAD</kortnavn><fornavn>ØYVIND</fornavn><mellomnavn>KJELDSTAD</mellomnavn><etternavn>GRIMNES</etternavn><spesregType> </spesregType><boAdresse1>LILLEVANNSVEIEN 53 E</boAdresse1><boAdresse2> </boAdresse2><postnr>0788</postnr><boPoststed>OSLO</boPoststed><bolignr> </bolignr><postAdresse1> </postAdresse1><postAdresse2> </postAdresse2><postAdresse3> </postAdresse3><kommunenr>0301</kommunenr><tknr>0333</tknr><tidligereKommunenr> </tidligereKommunenr><datoFlyttet> </datoFlyttet><personStatus> </personStatus><statsborger>NORGE</statsborger><datoStatsborger> </datoStatsborger><sivilstand>Ugift</sivilstand><datoSivilstand>1993-08-29</datoSivilstand><datoDo> </datoDo><datoUmyndiggjort> </datoUmyndiggjort><innvandretFra> </innvandretFra><datoInnvandret> </datoInnvandret><utvandretTil> </utvandretTil><datoUtvandret> </datoUtvandret><giroInfo><giroNummer> </giroNummer><giroTidspunktReg> </giroTidspunktReg><giroSystem> </giroSystem><giroSaksbehandler> </giroSaksbehandler></giroInfo><tlfPrivat><tlfNummer> </tlfNummer><tlfTidspunktReg> </tlfTidspunktReg><tlfSystem> </tlfSystem><tlfSaksbehandler> </tlfSaksbehandler></tlfPrivat><tlfJobb><tlfNummer> </tlfNummer><tlfTidspunktReg> </tlfTidspunktReg><tlfSystem> </tlfSystem><tlfSaksbehandler> </tlfSaksbehandler></tlfJobb><tlfMobil><tlfNummer> </tlfNummer><tlfTidspunktReg> </tlfTidspunktReg><tlfSystem> </tlfSystem><tlfSaksbehandler> </tlfSaksbehandler></tlfMobil><epost><epostAdresse> </epostAdresse><epostTidspunktReg> </epostTidspunktReg><epostSystem> </epostSystem><epostSaksbehandler> </epostSaksbehandler></epost></personDataS004></tpsSvar></tpsPersonData> ",
                //     "data": {
                //         "tpsServiceRutine": {
                //             "serviceRutinenavn": "FS03-FDNUMMER-PERSDATA-O",
                //             "fnr": "29089346337",
                //             "aksjonsDato": "2001-03-04",
                //             "aksjonsKode": "A",
                //             "aksjonsKode2": "0"
                //         },
                //         "tpsSvar": {
                //             "svarStatus": {
                //                 "returStatus": "00",
                //                 "returMelding": " ",
                //                 "utfyllendeMelding": " "
                //             },
                //             "personDataS004": {
                //                 "fnr": "29089346337",
                //                 "kortnavn": "GRIMNES ØYVIND KJELDSTAD",
                //                 // "kortnavn": "MMMMMMMMMMMMMMMMMMMMMMMMM",
                //                 "fornavn": "ØYVIND",
                //                 "mellomnavn": "KJELDSTAD",
                //                 "etternavn": "GRIMNES",
                //                 "spesregType": " ",
                //                 "boAdresse1": "LILLEVANNSVEIEN 53 E",
                //                 "boAdresse2": " ",
                //                 "postnr": "0788",
                //                 "boPoststed": "OSLO",
                //                 "bolignr": " ",
                //                 "postAdresse1": " ",
                //                 "postAdresse2": " ",
                //                 "postAdresse3": " ",
                //                 "kommunenr": "0301",
                //                 "tknr": "0333",
                //                 "tidligereKommunenr": " ",
                //                 "datoFlyttet": " ",
                //                 "personStatus": " ",
                //                 "statsborger": "NORGE",
                //                 "datoStatsborger": " ",
                //                 "sivilstand": "Ugift",
                //                 "datoSivilstand": "1993-08-29",
                //                 "datoDo": " ",
                //                 "datoUmyndiggjort": " ",
                //                 "innvandretFra": " ",
                //                 "datoInnvandret": " ",
                //                 "utvandretTil": " ",
                //                 "datoUtvandret": " ",
                //                 "giroInfo": {
                //                     "giroNummer": " ",
                //                     "giroTidspunktReg": " ",
                //                     "giroSystem": " ",
                //                     "giroSaksbehandler": " "
                //                 },
                //                 "tlfPrivat": {
                //                     "tlfNummer": " ",
                //                     "tlfTidspunktReg": " ",
                //                     "tlfSystem": " ",
                //                     "tlfSaksbehandler": " "
                //                 },
                //                 "tlfJobb": {
                //                     "tlfNummer": " ",
                //                     "tlfTidspunktReg": " ",
                //                     "tlfSystem": " ",
                //                     "tlfSaksbehandler": " "
                //                 },
                //                 "tlfMobil": {
                //                     "tlfNummer": " ",
                //                     "tlfTidspunktReg": " ",
                //                     "tlfSystem": " ",
                //                     "tlfSaksbehandler": " "
                //                 },
                //                 "epost": {
                //                     "epostAdresse": " ",
                //                     "epostTidspunktReg": " ",
                //                     "epostSystem": " ",
                //                     "epostSaksbehandler": " "
                //                 }
                //             }
                //         }
                //     }
                // };
                //
                // //#####################
                // //sjekk for statuskode på svar
                // $scope.responseReceived = true;
                //
                // $scope.xmlForm = utilsService.formatXml(res.data.xml);
                //
                // // tpsReturnedObject = angular.fromJson(res.data.object);
                // tpsReturnedObject = res.data.data;
                // $scope.svarStatus = tpsReturnedObject.tpsSvar.svarStatus;
                // $scope.personData = utilsService.flattenObject(tpsReturnedObject.tpsSvar.personDataS004, nonUniqueProperties);
            };

            $scope.isRequired = function(type) {
                return (requiredAttributes.indexOf(type) > -1);
            };

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
            
            function setIsValidRutineserviceCode() {
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
                setIsValidRutineserviceCode();
                
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
