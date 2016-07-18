/**
 * @author Frederik de Lichtenberg (Visma Consulting AS).
 */
angular.module('tps-vedlikehold.servicerutine', ['ngMessages', 'hljs'])
    .controller('servicerutineCtrl', ['$scope', '$stateParams', 'utilsService', 'servicerutineFactory',
        function($scope, $stateParams, utilsService, servicerutineFactory) {

            var tpsReturnedObject = {};

            $scope.servicerutineCode = $stateParams.servicerutineCode;
            $scope.responseReceived = true;
            $scope.required = {};
            var requiredAttributes = {};
            
            $scope.formData = {
                fnr:'',
                aksjonskode: 'B0'
            };
            
            $scope.environment = ''; // 

            // var servicerutineCode = $stateParams.servicerutineCode;
            
            $scope.formData.aksjonsDato = new Date();

            $scope.submit = function() {
                console.log("Send til tps pressed med fnr:\n" +
                    $scope.formData.fnr + ' ' + $scope.formData.aksjonskode);

                // requestFactory.getResponse(servicerutineCode, $scope.formData).then(function(res){
                //     //something
                //     $scope.xmlForm = res.data.xml;
                //     $scope.resultForm = res.data.object;
                // }, function(error){
                //     //something else
                // });
                var res = {};
                res.data = {
                    "xml": "<?xml version=\"1.0\"?>\n<tpsPersonData xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n    <tpsServiceRutine>\n        <serviceRutinenavn>FS03-FDNUMMER-PERSDATA-O</serviceRutinenavn>\n        <fnr>29089346337</fnr>\n        <aksjonsDato>2001-03-04</aksjonsDato>\n        <aksjonsKode>A</aksjonsKode>\n        <aksjonsKode2>0</aksjonsKode2>\n    </tpsServiceRutine> <tpsSvar><svarStatus><returStatus>00</returStatus><returMelding> </returMelding><utfyllendeMelding> </utfyllendeMelding></svarStatus><personDataS004><fnr>29089346337</fnr><kortnavn>GRIMNES ØYVIND KJELDSTAD</kortnavn><fornavn>ØYVIND</fornavn><mellomnavn>KJELDSTAD</mellomnavn><etternavn>GRIMNES</etternavn><spesregType> </spesregType><boAdresse1>LILLEVANNSVEIEN 53 E</boAdresse1><boAdresse2> </boAdresse2><postnr>0788</postnr><boPoststed>OSLO</boPoststed><bolignr> </bolignr><postAdresse1> </postAdresse1><postAdresse2> </postAdresse2><postAdresse3> </postAdresse3><kommunenr>0301</kommunenr><tknr>0333</tknr><tidligereKommunenr> </tidligereKommunenr><datoFlyttet> </datoFlyttet><personStatus> </personStatus><statsborger>NORGE</statsborger><datoStatsborger> </datoStatsborger><sivilstand>Ugift</sivilstand><datoSivilstand>1993-08-29</datoSivilstand><datoDo> </datoDo><datoUmyndiggjort> </datoUmyndiggjort><innvandretFra> </innvandretFra><datoInnvandret> </datoInnvandret><utvandretTil> </utvandretTil><datoUtvandret> </datoUtvandret><giroInfo><giroNummer> </giroNummer><giroTidspunktReg> </giroTidspunktReg><giroSystem> </giroSystem><giroSaksbehandler> </giroSaksbehandler></giroInfo><tlfPrivat><tlfNummer> </tlfNummer><tlfTidspunktReg> </tlfTidspunktReg><tlfSystem> </tlfSystem><tlfSaksbehandler> </tlfSaksbehandler></tlfPrivat><tlfJobb><tlfNummer> </tlfNummer><tlfTidspunktReg> </tlfTidspunktReg><tlfSystem> </tlfSystem><tlfSaksbehandler> </tlfSaksbehandler></tlfJobb><tlfMobil><tlfNummer> </tlfNummer><tlfTidspunktReg> </tlfTidspunktReg><tlfSystem> </tlfSystem><tlfSaksbehandler> </tlfSaksbehandler></tlfMobil><epost><epostAdresse> </epostAdresse><epostTidspunktReg> </epostTidspunktReg><epostSystem> </epostSystem><epostSaksbehandler> </epostSaksbehandler></epost></personDataS004></tpsSvar></tpsPersonData> ",
                    "object": "{\"tpsPersonData\":{\"xmlns\":\"http://www.rtv.no/NamespaceTPS\",\"tpsServiceRutine\":{\"aksjonsKode2\":0,\"aksjonsDato\":\"\",\"fnr\":12345678901,\"serviceRutinenavn\":\"FS03-FDNUMMER-PERSDATA-O\",\"aksjonsKode\":\"A\"},\"tpsSvar\":{\"svarStatus\":{\"returMelding\":\"\",\"returStatus\":\"00\",\"utfyllendeMelding\":\"\"},\"personDataS004\":{\"personStatus\":\"Bosatt\",\"kortnavn\":\"NORDMANN KARI\",\"datoInnvandret\":\"\",\"kommunenr\":\"0403\",\"epost\":{\"epostTidspunktReg\":\"\",\"epostSystem\":\"\",\"epostAdresse\":\"\",\"epostSaksbehandler\":\"\"},\"tlfPrivat\":{\"tlfSystem\":\"\",\"tlfTidspunktReg\":\"\",\"tlfNummer\":\"\",\"tlfSaksbehandler\":\"\"},\"tidligereKommunenr\":\"0412\",\"tknr\":\"0403\",\"tlfJobb\":{\"tlfSystem\":\"\",\"tlfTidspunktReg\":\"\",\"tlfNummer\":\"\",\"tlfSaksbehandler\":\"\"},\"bolignr\":\"\",\"datoDo\":\"\",\"datoUtvandret\":\"\",\"etternavn\":\"NORDMANN\",\"postnr\":2322,\"datoUmyndiggjort\":\"\",\"utvandretTil\":\"\",\"boAdresse2\":\"\",\"mellomnavn\":\"\",\"innvandretFra\":\"\",\"giroInfo\":{\"giroNummer\":12345678901,\"giroTidspunktReg\":\"\",\"giroSystem\":\"\",\"giroSaksbehandler\":\"\"},\"datoFlyttet\":\"2002-08-30\",\"postAdresse2\":\"\",\"postAdresse1\":\"\",\"datoSivilstand\":\"\",\"fnr\":12345678901,\"fornavn\":\"KARI\",\"sivilstand\":\"Ugift\",\"postAdresse3\":\"\",\"datoStatsborger\":\"\",\"spesregType\":\"\",\"boAdresse1\":\"STEINRØYSA 10 C\",\"tlfMobil\":{\"tlfSystem\":\"\",\"tlfTidspunktReg\":\"\",\"tlfNummer\":\"\",\"tlfSaksbehandler\":\"\"},\"statsborger\":\"NORGE\"}},\"xsi:schemaLocation\":\"<http://www.rtv.no/NamespaceTPS >W:\\\\PSDAT~2.XSD\",\"xmlns:xsi\":\"http://www.w3.org/2001/XMLSchema-instance\"}}"
                };
                //#####################
                //sjekk for statuskode på svar
                $scope.responseReceived = true;

                $scope.xmlForm = utilsService.formatXml(res.data.xml);

                tpsReturnedObject = angular.fromJson(res.data.object);
                $scope.svarStatus = tpsReturnedObject.tpsPersonData.tpsSvar.svarStatus;
                $scope.personData = tpsReturnedObject.tpsPersonData.tpsSvar.personDataS004;

                // $scope.resultForm = res.data.object;
                // console.log($scope.personData);
            };

            $scope.isRequired = function(type) {
                return (type in requiredAttributes);
            };

            function init() {
                $scope.fields = servicerutineFactory.getServicerutineAttributes($stateParams.servicerutineCode);
                $scope.fields.push('aksjonskode');
                requiredAttributes = servicerutineFactory.getServicerutineRequiredAttributes($stateParams.servicerutineCode);
                // console.log(requiredAttributes);
                // setRequiresAttributes();
                $scope.aksjonskoder = servicerutineFactory.getServicerutineAksjonskoder($stateParams.servicerutineCode).sort();
                // $scope.formData.aksjonskode = $scope.aksjonskoder[0];
                $scope.environments = servicerutineFactory.getEnvironments().sort();
            }
            init();

            // {"tpsPersonData":
            //     {"xmlns":"http://www.rtv.no/NamespaceTPS",
            //     "tpsServiceRutine":
            //         {"aksjonsKode2":0,"aksjonsDato":"","fnr":12345678901,"serviceRutinenavn":"FS03-FDNUMMER-PERSDATA-O","aksjonsKode":"A"},
            //     "tpsSvar":
            //         {
            //         "svarStatus":
            //             {"returMelding":"","returStatus":"00","utfyllendeMelding":""},
            //         "personDataS004":
            //             {
            //              "personStatus":"Bosatt",
            //              "kortnavn":"NORDMANN KARI",
            //              "datoInnvandret":"",
            //              "kommunenr":"0403",
            //              "epost":
            //                  {"epostTidspunktReg":"","epostSystem":"","epostAdresse":"","epostSaksbehandler":""},
            //              "tlfPrivat":
            //                  {"tlfSystem":"","tlfTidspunktReg":"","tlfNummer":"","tlfSaksbehandler":""},
            //              "tidligereKommunenr":"0412",
            //              "tknr":"0403",
            //              "tlfJobb":
            //                  {"tlfSystem":"","tlfTidspunktReg":"","tlfNummer":"","tlfSaksbehandler":""},
            //              "bolignr":"",
            //              "datoDo":"",
            //              "datoUtvandret":"",
            //              "etternavn":"NORDMANN",
            //              "postnr":2322,
            //              "datoUmyndiggjort":"",
            //              "utvandretTil":"",
            //              "boAdresse2":"",
            //              "mellomnavn":"",
            //              "innvandretFra":"",
            //              "giroInfo":
            //                  {"giroNummer":12345678901,"giroTidspunktReg":"","giroSystem":"","giroSaksbehandler":""},
            //              "datoFlyttet":"2002-08-30",
            //              "postAdresse2":"",
            //              "postAdresse1":"",
            //              "datoSivilstand":"",
            //              "fnr":12345678901,
            //              "fornavn":"KARI",
            //              "sivilstand":"Ugift",
            //              "postAdresse3":"",
            //              "datoStatsborger":"",
            //              "spesregType":"",
            //              "boAdresse1":"STEINRØYSA 10 C",
            //              "tlfMobil":
            //                  {"tlfSystem":"","tlfTidspunktReg":"","tlfNummer":"","tlfSaksbehandler":""},
            //              "statsborger":"NORGE"
            //             }
            //         },
            //     "xsi:schemaLocation":"<http://www.rtv.no/NamespaceTPS >W:\\PSDAT~2.XSD",
            //     "xmlns:xsi":"http://www.w3.org/2001/XMLSchema-instance"
            //     }
            // }
        }]);
