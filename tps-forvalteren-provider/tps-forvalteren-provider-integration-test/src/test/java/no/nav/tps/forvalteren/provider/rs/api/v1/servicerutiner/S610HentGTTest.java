package no.nav.tps.forvalteren.provider.rs.api.v1.servicerutiner;


import no.nav.tps.forvalteren.provider.rs.api.v1.AbstractServiceControllerIntegrationTest;
import no.nav.tps.forvalteren.provider.rs.api.v1.config.TestUserDetails;
import org.junit.Test;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class S610HentGTTest extends AbstractServiceControllerIntegrationTest{

    private static final String fnrTest = "27057047173";
    private static final String serviceRutineNavn = "FS03-FDNUMMER-KERNINFO-O";

    @Override
    protected String getServiceName() {
        return serviceRutineNavn;
    }

    @Test
    @WithUserDetails(TestUserDetails.USERNAME)
    public void getsPersonopplysninger() throws Exception {

        String tpsResponseXml = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><tpsPersonData><tpsServiceRutine><serviceRutinenavn>"+serviceRutineNavn+"</serviceRutinenavn><aksjonsDato/><aksjonsKode>B</aksjonsKode><aksjonsKode2>0</aksjonsKode2><fnr>"+fnrTest+"</fnr></tpsServiceRutine> <tpsSvar><svarStatus><returStatus>00</returStatus><returMelding> </returMelding><utfyllendeMelding> </utfyllendeMelding></svarStatus><personDataS610><person><fodselsnummer>27057047173</fodselsnummer><identType>FNR</identType><fodselsnummerDetalj><fnrTidspunkt> </fnrTidspunkt><fnrSystem> </fnrSystem><fnrSaksbehandler> </fnrSaksbehandler></fodselsnummerDetalj><fodselsdato>1970-05-27</fodselsdato><fodested> </fodested><fodestedDetalj><fodestedTidspunkt> </fodestedTidspunkt><fodestedSystem> </fodestedSystem><fodestedSaksbehandler> </fodestedSaksbehandler></fodestedDetalj><kjonn>M</kjonn><personnavn><gjeldendePersonnavn>STØKKEN HANS-ERLEND</gjeldendePersonnavn><allePersonnavn><kortnavn>STØKKEN HANS-ERLEND</kortnavn><fornavn>HANS-ERLEND</fornavn><mellomnavn> </mellomnavn><etternavn>STØKKEN</etternavn><navnTidspunkt>2002-05-22</navnTidspunkt><navnSystem>SKD</navnSystem><navnSaksbehandler> </navnSaksbehandler></allePersonnavn></personnavn><personstatusDetalj><kodePersonstatus>UTVA</kodePersonstatus><kodePersonstatusBeskr>Utvandret</kodePersonstatusBeskr><datoPersonstatus>2015-01-06</datoPersonstatus><psTidspunkt>2015-01-06</psTidspunkt><psSystem>SKD</psSystem><psSaksbehandler> </psSaksbehandler></personstatusDetalj><datoDo> </datoDo><datoDoDetalj><doTidspunkt> </doTidspunkt><doSystem> </doSystem><doSaksbehandler> </doSaksbehandler></datoDoDetalj><statsborgerskap>NOR</statsborgerskap><statsborgerskapDetalj><kodeStatsborgerskap>NOR</kodeStatsborgerskap><kodeStatsborgerskapBeskr>NORGE</kodeStatsborgerskapBeskr><datoStatsborgerskap> </datoStatsborgerskap><sbTidspunkt>2002-05-22</sbTidspunkt><sbSystem>SKD</sbSystem><sbSaksbehandler> </sbSaksbehandler></statsborgerskapDetalj><bankkontoNorge><kontoNummer>16541061359</kontoNummer><banknavn> </banknavn><regTidspunkt>2010-04-28</regTidspunkt><regSystem>SKD</regSystem><regSaksbehandler>AJOURHD</regSaksbehandler></bankkontoNorge><bostedsAdresse><boAdresse1> </boAdresse1><boAdresse2> </boAdresse2><boPostnr> </boPostnr><boPoststed> </boPoststed><fullBostedsAdresse><datoFom> </datoFom><datoTom> </datoTom><adresse1> </adresse1><adresse2> </adresse2><tilleggsAdresseSKD> </tilleggsAdresseSKD><kommunenr> </kommunenr><kommuneNavn> </kommuneNavn><tknr> </tknr><tkNavn> </tkNavn><bolignr> </bolignr><postnr> </postnr><poststed> </poststed><landKode> </landKode><land> </land><adresseType> </adresseType><beskrAdrType> </beskrAdrType><offAdresse><gateNavn> </gateNavn><husnr> </husnr><bokstav> </bokstav></offAdresse><matrAdresse><mellomAdresse> </mellomAdresse><gardsnr> </gardsnr><bruksnr> </bruksnr><festenr> </festenr><undernr> </undernr></matrAdresse><adrTidspunktReg> </adrTidspunktReg><adrSystem> </adrSystem><adrSaksbehandler> </adrSaksbehandler></fullBostedsAdresse></bostedsAdresse><postAdresse><postAdresse1>AVENIDA BOULEVARD NUMERO 19</postAdresse1><postAdresse2>29649 LA CALA DE MIJAS</postAdresse2><postAdresse3>SPANIA</postAdresse3><postPostnr> </postPostnr><postPoststed> </postPoststed><postLandKode>ESP</postLandKode><postLand>SPANIA</postLand><fullPostAdresse><datoFom>2014-12-30</datoFom><datoTom> </datoTom><landKode>ESP</landKode><land>SPANIA</land><adresseType>PUTL</adresseType><beskrAdrType>Postadresse utland</beskrAdrType><adresse1>AVENIDA BOULEVARD NUMERO 19</adresse1><adresse2>29649 LA CALA DE MIJAS</adresse2><adresse3>SPANIA</adresse3><postnr> </postnr><poststed> </poststed><adrTidspunktReg>2015-01-06</adrTidspunktReg><adrSystem>SKD</adrSystem><adrSaksbehandler>AJOURHD</adrSaksbehandler></fullPostAdresse></postAdresse><sivilstand>UGIF</sivilstand><sivilstandDetalj><kodeSivilstand>UGIF</kodeSivilstand><kodeSivilstandBeskr>Ugift</kodeSivilstandBeskr><datoSivilstand> </datoSivilstand><sivilstTidspunkt>2002-05-22</sivilstTidspunkt><sivilstSystem>SKD</sivilstSystem><sivilstSaksbehandler> </sivilstSaksbehandler></sivilstandDetalj><bruker><datoUmyndiggjort> </datoUmyndiggjort><umyndigDetalj><umyndigTidspunkt> </umyndigTidspunkt><umyndigSystem> </umyndigSystem><umyndigSaksbehandler> </umyndigSaksbehandler></umyndigDetalj><diskresjonDetalj><kodeDiskresjon> </kodeDiskresjon><kodeDiskresjonBeskr> </kodeDiskresjonBeskr><datoDiskresjon> </datoDiskresjon><diskresjonTidspunkt> </diskresjonTidspunkt><diskresjonSystem> </diskresjonSystem><diskresjonSaksbehandler> </diskresjonSaksbehandler></diskresjonDetalj><sikkerhetsTiltak><typeSikkerhetsTiltak> </typeSikkerhetsTiltak><beskrSikkerhetsTiltak> </beskrSikkerhetsTiltak><sikrFom> </sikrFom><sikrTom> </sikrTom><opprettetTidspunkt> </opprettetTidspunkt><opprettetSystem> </opprettetSystem><opprettetSaksbehandler> </opprettetSaksbehandler></sikkerhetsTiltak><NAVenhet>0214</NAVenhet><NAVenhetDetalj><kodeNAVenhet>0214</kodeNAVenhet><kodeNAVenhetBeskr>Ås</kodeNAVenhetBeskr><datoNAVenhet> </datoNAVenhet><NAVenhetTidspunkt>2002-05-22</NAVenhetTidspunkt><NAVenhetSystem>SKD</NAVenhetSystem><NAVenhetSaksbehandler> </NAVenhetSaksbehandler></NAVenhetDetalj><bankkontoUtland><giroNrUtland> </giroNrUtland><iban> </iban><swiftKodeUtland> </swiftKodeUtland><bankKodeUtland> </bankKodeUtland><bankNavnUtland> </bankNavnUtland><bankAdresse1> </bankAdresse1><bankAdresse2> </bankAdresse2><bankAdresse3> </bankAdresse3><bankLandKode> </bankLandKode><bankLand> </bankLand><bankValuta> </bankValuta><beskrBankValuta> </beskrBankValuta><regTidspunkt> </regTidspunkt><regSystem> </regSystem><regSaksbehandler> </regSaksbehandler></bankkontoUtland><postadresseNorgeNAV><datoFom> </datoFom><datoTom> </datoTom><typeAdresseNavNorge> </typeAdresseNavNorge><beskrTypeAdresseNavNorge> </beskrTypeAdresseNavNorge><typeTilleggsLinje> </typeTilleggsLinje><beskrTypeTilleggsLinje> </beskrTypeTilleggsLinje><tilleggsLinje> </tilleggsLinje><kommunenr> </kommunenr><kommuneNavn> </kommuneNavn><gatekode> </gatekode><gatenavn> </gatenavn><husnr> </husnr><husbokstav> </husbokstav><eiendomsnavn> </eiendomsnavn><bolignr> </bolignr><postboksnr> </postboksnr><postboksAnlegg> </postboksAnlegg><postnr> </postnr><poststed> </poststed><adrTidspunktReg> </adrTidspunktReg><adrSystem> </adrSystem><adrSaksbehandler> </adrSaksbehandler></postadresseNorgeNAV><postadresseUtlandNAV><datoFom> </datoFom><datoTom> </datoTom><landKode> </landKode><land> </land><adresseType> </adresseType><beskrAdrType> </beskrAdrType><adresse1> </adresse1><adresse2> </adresse2><adresse3> </adresse3><adrTidspunktReg> </adrTidspunktReg><adrSystem> </adrSystem><adrSaksbehandler> </adrSaksbehandler></postadresseUtlandNAV><relasjoner></relasjoner><geografiskTilknytning><landKode>ESP</landKode><kommunenr> </kommunenr><bydel> </bydel></geografiskTilknytning><regelForGeografiskTilknytning>C</regelForGeografiskTilknytning><behov></behov><preferanser><sprak> </sprak></preferanser><telefoner></telefoner><harVerge>0</harVerge></bruker></person></personDataS610></tpsSvar> </tpsPersonData> ";

        setResponseQueueMessage(tpsResponseXml);

        addRequestParam("aksjonsKode", "B0");
        addRequestParam("environment", "t9");
        addRequestParam("fnr", fnrTest);

        MvcResult result = mvc.perform(get(getUrl()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.xml", is(equalTo(tpsResponseXml))))
                .andExpect(jsonPath("$.response.data1.identType", is(equalTo("FNR"))))
                .andExpect(jsonPath("$.response.data1.fodselsnummer", is(equalTo(Long.parseLong(fnrTest)))))
                .andReturn();


        String content = result.getResponse().getContentAsString();
    }

}
