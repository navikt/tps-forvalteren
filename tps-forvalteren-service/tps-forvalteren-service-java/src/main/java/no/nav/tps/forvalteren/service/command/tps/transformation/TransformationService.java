package no.nav.tps.forvalteren.service.command.tps.transformation;

import static java.util.Objects.nonNull;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.service.tps.Request;
import no.nav.tps.forvalteren.domain.service.tps.Response;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsServiceRoutineDefinitionRequest;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.transformers.Transformer;
import no.nav.tps.forvalteren.service.command.tps.transformation.request.RequestTransformStrategy;
import no.nav.tps.forvalteren.service.command.tps.transformation.response.ResponseTransformStrategy;

@Service
public class TransformationService {

    @Autowired
    private List<RequestTransformStrategy> requestStrategies;

    @Autowired
    private List<ResponseTransformStrategy> responseStrategies;

    public void transform(Request request, TpsServiceRoutineDefinitionRequest serviceRoutine) {
        if (nonNull(serviceRoutine.getTransformers())) {
            for (Transformer transformer : serviceRoutine.getTransformers()) {
                for (RequestTransformStrategy strategy : requestStrategies) {
                    if (strategy.isSupported(transformer)) {
                        strategy.execute(request, transformer);
                    }
                }
            }
        }
    }

    public void transform(Response response, TpsServiceRoutineDefinitionRequest serviceRoutine) {
        if (nonNull(serviceRoutine.getTransformers())) {
            for (Transformer transformer : serviceRoutine.getTransformers()) {
                for (ResponseTransformStrategy strategy : responseStrategies) {
                    if (strategy.isSupported(transformer)) {
                        strategy.execute(response, transformer);
                    }
                }
            }
        }
    }

}
