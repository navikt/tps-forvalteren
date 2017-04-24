package no.nav.tps.forvalteren.service.command.tps.transformation.response;

import no.nav.tps.forvalteren.domain.service.tps.Response;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.transformers.response.ResponseDataTransformer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


@RunWith(MockitoJUnitRunner.class)
public class ResponseDataTransformStrategyTest {


    @InjectMocks
    private ResponseDataTransformStrategy strategy;

    @Test
    public void isSupportedReturnsTrueforResponseDataTransformer() {
        ResponseDataTransformer transform = new ResponseDataTransformer();

        boolean isSupported = strategy.isSupported(transform);

        assertThat(isSupported, is(true));
    }

    @Test
    public void executeUpdatesResponseWithDataInXmlElement() {

        String data = "data";

        ResponseDataTransformer transformer = new ResponseDataTransformer();
        transformer.setXmlElement("tag");

        Response response = new Response();
        response.setRawXml("<tag>"+data+"</tag>");

        strategy.execute(response, transformer);

        assertThat(response.getDataXmls().get(0), is(equalTo(data)));
    }

}