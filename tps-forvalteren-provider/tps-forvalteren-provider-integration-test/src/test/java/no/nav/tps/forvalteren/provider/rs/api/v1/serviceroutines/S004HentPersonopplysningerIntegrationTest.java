package no.nav.tps.forvalteren.provider.rs.api.v1.serviceroutines;

import no.nav.tps.forvalteren.provider.rs.api.v1.AbstractServiceControllerIntegrationTest;
import no.nav.tps.forvalteren.provider.rs.api.v1.config.TestUserDetails;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Ignore
public class S004HentPersonopplysningerIntegrationTest extends AbstractServiceControllerIntegrationTest {

    @Override
    protected String getServiceName() {
        return "FS03-FDNUMMER-PERSDATA-O";
    }

    @Test
    @WithUserDetails(TestUserDetails.USERNAME)
    public void getsPersonopplysninger() throws Exception {

        String tpsResponseXml = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><tpsPersonData><tpsServiceRutine><serviceRutinenavn>FS03-FDNUMMER-PERSDATA-O</serviceRutinenavn><aksjonsDato>2017-04-11</aksjonsDato><aksjonsKode>A</aksjonsKode><aksjonsKode2>0</aksjonsKode2><fnr>01018012345</fnr></tpsServiceRutine><tpsSvar><svarStatus><returStatus>00</returStatus><returMelding></returMelding><utfyllendeMelding></utfyllendeMelding></svarStatus><personDataS004><fnr>01018012345</fnr><kortnavn>TESTERSEN TEST</kortnavn><fornavn>TEST</fornavn><mellomnavn></mellomnavn><etternavn>TESTERSEN</etternavn><spesregType></spesregType><boAdresse1>TESTGATA 10 B</boAdresse1><boAdresse2></boAdresse2><postnr>0400</postnr><boPoststed>OSLO</boPoststed><bolignr>H0999</bolignr><postAdresse1></postAdresse1><postAdresse2></postAdresse2><postAdresse3></postAdresse3><kommunenr>0301</kommunenr><tknr>0314</tknr><tidligereKommunenr>0220</tidligereKommunenr><datoFlyttet>2014-10-12</datoFlyttet><personStatus>Bosatt                          Fnr</personStatus><statsborger>NORGE</statsborger><datoStatsborger></datoStatsborger><sivilstand>Ugift</sivilstand><datoSivilstand>1980-01-01</datoSivilstand><datoDo></datoDo><datoUmyndiggjort></datoUmyndiggjort><innvandretFra></innvandretFra><datoInnvandret></datoInnvandret><utvandretTil></utvandretTil><datoUtvandret></datoUtvandret><giroInfo><giroNummer>11112233333</giroNummer><giroTidspunktReg>2010-04-28</giroTidspunktReg><giroSystem>SKD</giroSystem><giroSaksbehandler>AJOURHD</giroSaksbehandler></giroInfo><tlfPrivat><tlfNummer></tlfNummer><tlfTidspunktReg></tlfTidspunktReg><tlfSystem></tlfSystem><tlfSaksbehandler></tlfSaksbehandler></tlfPrivat><tlfJobb><tlfNummer></tlfNummer><tlfTidspunktReg></tlfTidspunktReg><tlfSystem></tlfSystem><tlfSaksbehandler></tlfSaksbehandler></tlfJobb><tlfMobil><tlfNummer></tlfNummer><tlfTidspunktReg></tlfTidspunktReg><tlfSystem></tlfSystem><tlfSaksbehandler></tlfSaksbehandler></tlfMobil><epost><epostAdresse></epostAdresse><epostTidspunktReg></epostTidspunktReg><epostSystem></epostSystem><epostSaksbehandler></epostSaksbehandler></epost></personDataS004></tpsSvar></tpsPersonData>";

        setResponseQueueMessage(tpsResponseXml);

        addRequestParam("aksjonsDato", "2017-04-10");
        addRequestParam("aksjonsKode", "A0");
        addRequestParam("environment", "t0");
        addRequestParam("fnr", "01018012345");

        MvcResult result = mvc.perform(get(getUrl()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.xml", is(equalTo(tpsResponseXml))))
                .andExpect(jsonPath("$.response.data[0].fnr", is(equalTo("01018012345"))))
                .andReturn();
    }

}
