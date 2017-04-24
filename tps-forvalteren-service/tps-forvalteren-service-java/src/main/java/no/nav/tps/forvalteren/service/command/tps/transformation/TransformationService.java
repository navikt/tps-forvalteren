package no.nav.tps.forvalteren.service.command.tps.transformation;

import no.nav.tps.forvalteren.domain.service.tps.Request;
import no.nav.tps.forvalteren.domain.service.tps.Response;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsServiceRoutineDefinition;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.transformers.Transformer;
import no.nav.tps.forvalteren.service.command.tps.transformation.request.RequestTransformStrategy;
import no.nav.tps.forvalteren.service.command.tps.transformation.response.ResponseTransformStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransformationService {

    @Autowired
    private List<RequestTransformStrategy> requestStrategies;

    @Autowired
    private List<ResponseTransformStrategy> responseStrategies;


    public void transform(Request request, TpsServiceRoutineDefinition serviceRoutine) {
        for (Transformer transformer : serviceRoutine.getTransformers()) {
            for (RequestTransformStrategy strategy : requestStrategies) {
                if (strategy.isSupported(transformer)){
                    strategy.execute(request, transformer);
                }
            }
        }
    }

    public void transform(Response response, TpsServiceRoutineDefinition serviceRoutine) {
        for (Transformer transformer : serviceRoutine.getTransformers()) {
            for (ResponseTransformStrategy strategy : responseStrategies) {
                if (strategy.isSupported(transformer)){
                    strategy.execute(response, transformer);
                }
            }
        }
    }

}
