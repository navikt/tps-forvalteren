/**
 * @author Frederik de Lichtenberg (Visma Consulting AS).
 */
angular.module('tps-vedlikehold.servicerutine', ['ngMessages'])
    .controller('servicerutineCtrl', ['$scope', '$stateParams', 'servicerutineFactory', 'utilsService',
        function($scope, $stateParams, servicerutineFactory, utilsService) {

            var tpsReturnedObject = {};

            $scope.servicerutineCode = $stateParams.servicerutineCode;
            
            //
            $scope.responseReceived = true;//
            //
            
            $scope.required = {};
            $scope.isValidServicerutineCode = false;
            var requiredAttributes = {};
            
            $scope.formData = {};
            $scope.environment = ''; //

            //objects that contain non-unique properties
            var nonUniqueProperties = [];
            

            $scope.submit = function() {
                console.log("Send til tps pressed med data:\n" +
                    $scope.formData.fnr + '\n' + $scope.formData.aksjonsDato + '\n' + $scope.formData.aksjonskode + '\n' + $scope.environment);

                // requestFactory.getResponse(servicerutineCode, $scope.formData).then(function(res){
                //     //something
                //     $scope.xmlForm = res.data.xml;
                //     $scope.resultForm = res.data.object;
                //     $scope.responseReceived = true;
                // }, function(error){
                //     //something else
                //     $scope.responseReceived = false;
                // });
                var res = {};
                res.data= {
                    "xml": "<?xml version=\"1.0\"?>\n<tpsPersonData xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n    <tpsServiceRutine>\n        <serviceRutinenavn>FS03-FDNUMMER-PERSDATA-O</serviceRutinenavn>\n        <fnr>29089346337</fnr>\n        <aksjonsDato>2001-03-04</aksjonsDato>\n        <aksjonsKode>A</aksjonsKode>\n        <aksjonsKode2>0</aksjonsKode2>\n    </tpsServiceRutine> <tpsSvar><svarStatus><returStatus>00</returStatus><returMelding> </returMelding><utfyllendeMelding> </utfyllendeMelding></svarStatus><personDataS004><fnr>29089346337</fnr><kortnavn>GRIMNES ØYVIND KJELDSTAD</kortnavn><fornavn>ØYVIND</fornavn><mellomnavn>KJELDSTAD</mellomnavn><etternavn>GRIMNES</etternavn><spesregType> </spesregType><boAdresse1>LILLEVANNSVEIEN 53 E</boAdresse1><boAdresse2> </boAdresse2><postnr>0788</postnr><boPoststed>OSLO</boPoststed><bolignr> </bolignr><postAdresse1> </postAdresse1><postAdresse2> </postAdresse2><postAdresse3> </postAdresse3><kommunenr>0301</kommunenr><tknr>0333</tknr><tidligereKommunenr> </tidligereKommunenr><datoFlyttet> </datoFlyttet><personStatus> </personStatus><statsborger>NORGE</statsborger><datoStatsborger> </datoStatsborger><sivilstand>Ugift</sivilstand><datoSivilstand>1993-08-29</datoSivilstand><datoDo> </datoDo><datoUmyndiggjort> </datoUmyndiggjort><innvandretFra> </innvandretFra><datoInnvandret> </datoInnvandret><utvandretTil> </utvandretTil><datoUtvandret> </datoUtvandret><giroInfo><giroNummer> </giroNummer><giroTidspunktReg> </giroTidspunktReg><giroSystem> </giroSystem><giroSaksbehandler> </giroSaksbehandler></giroInfo><tlfPrivat><tlfNummer> </tlfNummer><tlfTidspunktReg> </tlfTidspunktReg><tlfSystem> </tlfSystem><tlfSaksbehandler> </tlfSaksbehandler></tlfPrivat><tlfJobb><tlfNummer> </tlfNummer><tlfTidspunktReg> </tlfTidspunktReg><tlfSystem> </tlfSystem><tlfSaksbehandler> </tlfSaksbehandler></tlfJobb><tlfMobil><tlfNummer> </tlfNummer><tlfTidspunktReg> </tlfTidspunktReg><tlfSystem> </tlfSystem><tlfSaksbehandler> </tlfSaksbehandler></tlfMobil><epost><epostAdresse> </epostAdresse><epostTidspunktReg> </epostTidspunktReg><epostSystem> </epostSystem><epostSaksbehandler> </epostSaksbehandler></epost></personDataS004></tpsSvar></tpsPersonData> ",
                    "data": {
                        "tpsServiceRutine": {
                            "serviceRutinenavn": "FS03-FDNUMMER-PERSDATA-O",
                            "fnr": "29089346337",
                            "aksjonsDato": "2001-03-04",
                            "aksjonsKode": "A",
                            "aksjonsKode2": "0"
                        },
                        "tpsSvar": {
                            "svarStatus": {
                                "returStatus": "00",
                                "returMelding": " ",
                                "utfyllendeMelding": " "
                            },
                            "personDataS004": {
                                "fnr": "29089346337",
                                "kortnavn": "GRIMNES ØYVIND KJELDSTAD",
                                // "kortnavn": "MMMMMMMMMMMMMMMMMMMMMMMMM",
                                "fornavn": "ØYVIND",
                                "mellomnavn": "KJELDSTAD",
                                "etternavn": "GRIMNES",
                                "spesregType": " ",
                                "boAdresse1": "LILLEVANNSVEIEN 53 E",
                                "boAdresse2": " ",
                                "postnr": "0788",
                                "boPoststed": "OSLO",
                                "bolignr": " ",
                                "postAdresse1": " ",
                                "postAdresse2": " ",
                                "postAdresse3": " ",
                                "kommunenr": "0301",
                                "tknr": "0333",
                                "tidligereKommunenr": " ",
                                "datoFlyttet": " ",
                                "personStatus": " ",
                                "statsborger": "NORGE",
                                "datoStatsborger": " ",
                                "sivilstand": "Ugift",
                                "datoSivilstand": "1993-08-29",
                                "datoDo": " ",
                                "datoUmyndiggjort": " ",
                                "innvandretFra": " ",
                                "datoInnvandret": " ",
                                "utvandretTil": " ",
                                "datoUtvandret": " ",
                                "giroInfo": {
                                    "giroNummer": " ",
                                    "giroTidspunktReg": " ",
                                    "giroSystem": " ",
                                    "giroSaksbehandler": " "
                                },
                                "tlfPrivat": {
                                    "tlfNummer": " ",
                                    "tlfTidspunktReg": " ",
                                    "tlfSystem": " ",
                                    "tlfSaksbehandler": " "
                                },
                                "tlfJobb": {
                                    "tlfNummer": " ",
                                    "tlfTidspunktReg": " ",
                                    "tlfSystem": " ",
                                    "tlfSaksbehandler": " "
                                },
                                "tlfMobil": {
                                    "tlfNummer": " ",
                                    "tlfTidspunktReg": " ",
                                    "tlfSystem": " ",
                                    "tlfSaksbehandler": " "
                                },
                                "epost": {
                                    "epostAdresse": " ",
                                    "epostTidspunktReg": " ",
                                    "epostSystem": " ",
                                    "epostSaksbehandler": " "
                                }
                            }
                        }
                    }
                };

                //#####################
                //sjekk for statuskode på svar
                $scope.responseReceived = true;
                
                $scope.xmlForm = res.data.xml;

                // tpsReturnedObject = angular.fromJson(res.data.object);
                tpsReturnedObject = res.data.data;
                $scope.svarStatus = tpsReturnedObject.tpsSvar.svarStatus;
                $scope.personData = utilsService.flattenObject(tpsReturnedObject.tpsSvar.personDataS004, nonUniqueProperties);
            };

            $scope.isRequired = function(type) {
                return (type in requiredAttributes);
            };
            
            function getServicerutineAttributesNames() {
                $scope.fields = servicerutineFactory.getServicerutineAttributesNames($stateParams.servicerutineCode);
                $scope.fields.push('aksjonskode');
            }
            
            function setIsValidRutineserviceCode() {
                $scope.isValidServicerutineCode = ($stateParams.servicerutineCode in servicerutineFactory.getServicerutiner());
            }
            
            function getServicerutineRequiredAttributesNames() {
                requiredAttributes = servicerutineFactory.getServicerutineRequiredAttributesNames($stateParams.servicerutineCode);
            }
            
            function getServicerutineAksjonskoder() {
                $scope.aksjonskoder = servicerutineFactory.getServicerutineAksjonskoder($stateParams.servicerutineCode).sort();
            }
            
            function getEnvironments() {
                $scope.environments = servicerutineFactory.getEnvironments().sort();
            }

            function getNonUniqueProperties() {
                nonUniqueProperties = servicerutineFactory.getNonUniqueProperties($stateParams.servicerutineCode);
            }
            
            function initFormData() {
                $scope.formData = {
                    fnr:'',
                    aksjonskode: 'A0',
                    aksjonsDato: new Date()
                };
            }

            function init() {
                setIsValidRutineserviceCode();
                
                //better way to do this?
                if (!$scope.isValidServicerutineCode) {
                    return;
                }
                
                getServicerutineAttributesNames();
                getServicerutineRequiredAttributesNames();
                getServicerutineAksjonskoder();
                getEnvironments();
                getNonUniqueProperties();
                initFormData();
            }

            init();
        }]);
