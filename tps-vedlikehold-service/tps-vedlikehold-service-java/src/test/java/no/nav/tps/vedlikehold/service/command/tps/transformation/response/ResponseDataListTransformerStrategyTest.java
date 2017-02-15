package no.nav.tps.vedlikehold.service.command.tps.transformation.response;

import no.nav.tps.vedlikehold.domain.service.tps.Response;
import no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.transformers.response.ResponseDataListTransformer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class ResponseDataListTransformerStrategyTest {


    @InjectMocks
    private ResponseDataListTransformerStrategy strategy;


    @Test
    public void isSupportedReturnsTrueForResponseDataListTransformer() {
        ResponseDataListTransformer transformer = new ResponseDataListTransformer();

        boolean isSupported = strategy.isSupported(transformer);
        assertThat(isSupported, is(true));
    }


    @Test
    public void executeUpdatesResponseWithDataInXmlElement() {
        String data1 = "data1";
        String data2 = "data2";
        String totalHits = "2";

        ResponseDataListTransformer transformer = new ResponseDataListTransformer();
        transformer.setXmlElement("tag");
        transformer.setXmlElementSingleResource("data");
        transformer.setXmlElementTotalHits("totalHits");

        Response response = new Response();
        response.setRawXml("<tag> <totalHits>"+totalHits+"</totalHits> <data>"+data1+"</data> <data>"+data2+"</data>   </tag>");


        strategy.execute(response, transformer);

        assertThat(response.getTotalHits(), is(2));
        assertThat(response.getDataXmls().get(0), is(data1));
        assertThat(response.getDataXmls().get(1), is(data2));

    }

}