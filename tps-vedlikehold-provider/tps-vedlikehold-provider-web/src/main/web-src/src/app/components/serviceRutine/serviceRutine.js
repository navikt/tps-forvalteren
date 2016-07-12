/**
 * @author Frederik de Lichtenberg (Visma Consulting AS).
 */
angular.module('tps-vedlikehold.servicerutine', ['ngMessages'])
    .controller('servicerutineCtrl', ['$scope', '$stateParams', 'servicerutineFactory',
        function($scope, $stateParams, servicerutineFactory) {

            $scope.responseReceived = true;
            
            $scope.formData = {
                fnr:''
            };
            
            $scope.environment = ''; // 

            // var servicerutineCode = $stateParams.servicerutineCode;
            
            $scope.formData.aksjonsDato = new Date();

            $scope.submit = function() {
                console.log("Send til tps pressed med fnr:\n" +
                    $scope.formData.fnr);

                // requestFactory.getResponse(servicerutineCode, $scope.formData).then(function(res){
                //     //something
                // }, function(error){
                //     //something else
                // });
            };

            function init() {
                $scope.fields = servicerutineFactory.getServicerutineAttributes($stateParams.servicerutineCode);
                $scope.fields.push('aksjonskode');
                $scope.aksjonskoder = servicerutineFactory.getServicerutineAksjonskoder($stateParams.servicerutineCode);
                $scope.environments = servicerutineFactory.getEnvironments();
            }
            init();

            // {
            //     "xml": "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><tpsPersonData xmlns=\"http://www.rtv.no/NamespaceTPS\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.rtv.no/NamespaceTPS W:\\PSDAT~2.XSD\"> <tpsServiceRutine><serviceRutinenavn>FS03-FDNUMMER-PERSDATA-O</serviceRutinenavn><fnr>12345678901</fnr><aksjonsDato/><aksjonsKode>A</aksjonsKode><aksjonsKode2>0</aksjonsKode2></tpsServiceRutine><tpsSvar><svarStatus><returStatus>00</returStatus><returMelding/><utfyllendeMelding/></svarStatus><personDataS004><fnr>12345678901</fnr><kortnavn>NORDMANN KARI</kortnavn><fornavn>KARI</fornavn><mellomnavn/><etternavn>NORDMANN</etternavn><spesregType/><boAdresse1>STEINRØYSA 10 C</boAdresse1><boAdresse2/><bolignr/><postAdresse1/><postAdresse2/><postAdresse3/><postnr>2322</postnr><kommunenr>0403</kommunenr><tknr>0403</tknr><tlfPrivat><tlfNummer/><tlfTidspunktReg/><tlfSystem/><tlfSaksbehandler/></tlfPrivat><tlfJobb><tlfNummer/><tlfTidspunktReg/><tlfSystem/><tlfSaksbehandler/></tlfJobb><tlfMobil><tlfNummer/><tlfTidspunktReg/><tlfSystem/><tlfSaksbehandler/></tlfMobil><epost><epostAdresse/><epostTidspunktReg/><epostSystem/><epostSaksbehandler/></epost><tidligereKommunenr>0412</tidligereKommunenr><datoFlyttet>2002-08-30</datoFlyttet><personStatus>Bosatt</personStatus><statsborger>NORGE</statsborger><datoStatsborger/><sivilstand>Ugift</sivilstand><datoSivilstand/><datoDo/><datoUmyndiggjort/><innvandretFra/><datoInnvandret/><utvandretTil/><datoUtvandret/><giroInfo><giroNummer>12345678901</giroNummer><giroTidspunktReg/><giroSystem/><giroSaksbehandler/></giroInfo></personDataS004></tpsSvar></tpsPersonData>",
            //     "object": "{\"tpsPersonData\":{\"xmlns\":\"http://www.rtv.no/NamespaceTPS\",\"tpsServiceRutine\":{\"aksjonsKode2\":0,\"aksjonsDato\":\"\",\"fnr\":12345678901,\"serviceRutinenavn\":\"FS03-FDNUMMER-PERSDATA-O\",\"aksjonsKode\":\"A\"},\"tpsSvar\":{\"svarStatus\":{\"returMelding\":\"\",\"returStatus\":\"00\",\"utfyllendeMelding\":\"\"},\"personDataS004\":{\"personStatus\":\"Bosatt\",\"kortnavn\":\"NORDMANN KARI\",\"datoInnvandret\":\"\",\"kommunenr\":\"0403\",\"epost\":{\"epostTidspunktReg\":\"\",\"epostSystem\":\"\",\"epostAdresse\":\"\",\"epostSaksbehandler\":\"\"},\"tlfPrivat\":{\"tlfSystem\":\"\",\"tlfTidspunktReg\":\"\",\"tlfNummer\":\"\",\"tlfSaksbehandler\":\"\"},\"tidligereKommunenr\":\"0412\",\"tknr\":\"0403\",\"tlfJobb\":{\"tlfSystem\":\"\",\"tlfTidspunktReg\":\"\",\"tlfNummer\":\"\",\"tlfSaksbehandler\":\"\"},\"bolignr\":\"\",\"datoDo\":\"\",\"datoUtvandret\":\"\",\"etternavn\":\"NORDMANN\",\"postnr\":2322,\"datoUmyndiggjort\":\"\",\"utvandretTil\":\"\",\"boAdresse2\":\"\",\"mellomnavn\":\"\",\"innvandretFra\":\"\",\"giroInfo\":{\"giroNummer\":12345678901,\"giroTidspunktReg\":\"\",\"giroSystem\":\"\",\"giroSaksbehandler\":\"\"},\"datoFlyttet\":\"2002-08-30\",\"postAdresse2\":\"\",\"postAdresse1\":\"\",\"datoSivilstand\":\"\",\"fnr\":12345678901,\"fornavn\":\"KARI\",\"sivilstand\":\"Ugift\",\"postAdresse3\":\"\",\"datoStatsborger\":\"\",\"spesregType\":\"\",\"boAdresse1\":\"STEINRØYSA 10 C\",\"tlfMobil\":{\"tlfSystem\":\"\",\"tlfTidspunktReg\":\"\",\"tlfNummer\":\"\",\"tlfSaksbehandler\":\"\"},\"statsborger\":\"NORGE\"}},\"xsi:schemaLocation\":\"<http://www.rtv.no/NamespaceTPS >W:\\\\PSDAT~2.XSD\",\"xmlns:xsi\":\"http://www.w3.org/2001/XMLSchema-instance\"}}"
            // }
        }]);
