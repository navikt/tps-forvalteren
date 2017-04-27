package no.nav.tps.forvalteren.service.command.tps.transformation.request;

import no.nav.tps.forvalteren.domain.service.tps.Request;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.transformers.Transformer;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.transformers.request.ServiceRoutineRequestTransform;
import org.springframework.stereotype.Component;

@Component
public class ServiceRoutineRequestTransformStrategy implements RequestTransformStrategy {

    private static final String XML_PROPERTIES_PREFIX  = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><tpsPersonData>";
    private static final String XML_PROPERTIES_POSTFIX = "</tpsPersonData>";

    @Override
    public void execute(Request request, Transformer transformer) {
        request.setXml(XML_PROPERTIES_PREFIX + request.getXml() + XML_PROPERTIES_POSTFIX);
    }

    @Override
    public boolean isSupported(Object o) {
        return o instanceof ServiceRoutineRequestTransform;
    }

}