package no.nav.tps.vedlikehold.service.command.tps.servicerutiner.utils;

import no.nav.tps.vedlikehold.domain.service.jpa.TPSKTestPerson;
import no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.response.TpsTestdatapersonerResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * Created by Peter Fløgstad on 23.02.2017.
 */

@Component
public class TpsTestdatapersonerResponseMappingUtils {

    public TpsTestdatapersonerResponse convertToTpsServiceRutineResponse(List<TPSKTestPerson> personer) throws IOException {
        TpsTestdatapersonerResponse response = new TpsTestdatapersonerResponse();
        response.setResponse(personer);
        return response;
    }
}
