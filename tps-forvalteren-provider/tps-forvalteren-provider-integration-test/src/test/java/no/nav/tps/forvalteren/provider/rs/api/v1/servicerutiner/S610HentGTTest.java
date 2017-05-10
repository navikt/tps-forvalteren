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

    private static final String fnrTest = "01018012345";
    private static final String serviceRutineNavn = "FS03-FDNUMMER-KERNINFO-O";

    @Override
    protected String getServiceName() {
        return serviceRutineNavn;
    }

    @Test
    @WithUserDetails(TestUserDetails.USERNAME)
    public void getsPersonopplysninger() throws Exception {

        String tpsResponseXml = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><tpsPersonData><tpsServiceRutine><serviceRutinenavn>FS03-FDNUMMER-KERNINFO-O</serviceRutinenavn><aksjonsDato/><aksjonsKode>A</aksjonsKode><aksjonsKode2>0</aksjonsKode2><fnr>"+ fnrTest+"</fnr></tpsServiceRutine> <tpsSvar><svarStatus><returStatus>00</returStatus><returMelding> </returMelding><utfyllendeMelding> </utfyllendeMelding></svarStatus><personDataS610><person><fodselsnummer>"+fnrTest+"</fodselsnummer><fodselsnummerDetalj>s</bruker></person></personDataS610></tpsSvar> </tpsPersonData> ";

        setResponseQueueMessage(tpsResponseXml);

        addRequestParam("aksjonsKode", "A0");
        addRequestParam("environment", "t9");
        addRequestParam("fnr", fnrTest);

        MvcResult result = mvc.perform(get(getUrl()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.xml", is(equalTo(tpsResponseXml))))
                .andExpect(jsonPath("$.response.data[0].fodselsnummer", is(equalTo(fnrTest))))
                .andReturn();


    }

}
