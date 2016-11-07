package no.nav.tps.vedlikehold.service.command.tps.transformation;

import no.nav.tps.vedlikehold.domain.service.command.tps.Request;
import no.nav.tps.vedlikehold.domain.service.command.tps.Response;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.definition.TpsServiceRoutine;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.transformers.XmlTransformer;
import org.springframework.stereotype.Component;

@Component
public class TransformationService {

    public void transform(Request request, TpsServiceRoutine serviceRoutine) {
        serviceRoutine.getTransformers()
                .stream()
                .filter(XmlTransformer::isPreSend)
                .forEach(transformer -> {
                    String transformedXml = transformer.getStrategy().execute(request.getXml());
                    request.setXml(transformedXml);
                });
    }

    public void transform(Response response, TpsServiceRoutine serviceRoutine) {
        serviceRoutine.getTransformers()
                .stream()
                .filter(XmlTransformer::isPostSend)
                .forEach(transformer -> {
                    String transformedXml = transformer.getStrategy().execute(response.getXml());
                    response.setXml(transformedXml);
                });
    }

}
