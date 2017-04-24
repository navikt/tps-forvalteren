package no.nav.tps.forvalteren.service.command.tps.transformation.response;

import no.nav.tps.forvalteren.domain.service.tps.Response;
import no.nav.tps.forvalteren.domain.service.tps.ResponseStatus;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.transformers.response.ResponseStatusTransformer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class ResponseStatusTransformStrategyTest {


    @InjectMocks
    private ResponseStatusTransformStrategy strategy;

    @Test
    public void isSupportedReturnsTrueForResponseStatusTransformer() {
        ResponseStatusTransformer transformer = new ResponseStatusTransformer();

        boolean isSupported = strategy.isSupported(transformer);
        assertThat(isSupported, is(true));
    }


    @Test
    public void executeUpdatesResponseWithStatus() {

        String returStatus = "returStatus";
        String returMelding = "returMelding";
        String utfyllendeMelding = "utfyllendeMelding";


        ResponseStatusTransformer transformer = new ResponseStatusTransformer();
        transformer.setXmlElement("status");

        Response response = new Response();
        response.setRawXml("<status> <returStatus>"+returStatus+"</returStatus>  <returMelding>"+returMelding+"</returMelding>  <utfyllendeMelding>"+utfyllendeMelding+"</utfyllendeMelding>  </status>");

        strategy.execute(response, transformer);

        ResponseStatus status = response.getStatus();

        assertThat(status, is(notNullValue()));
        assertThat(status.getKode(), is(equalTo(returStatus)));
        assertThat(status.getMelding(), is(equalTo(returMelding)));
        assertThat(status.getUtfyllendeMelding(), is(equalTo(utfyllendeMelding)));


    }
}