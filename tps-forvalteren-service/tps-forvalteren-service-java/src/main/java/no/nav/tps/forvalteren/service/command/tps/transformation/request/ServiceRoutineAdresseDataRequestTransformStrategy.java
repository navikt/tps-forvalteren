package no.nav.tps.forvalteren.service.command.tps.transformation.request;

import org.springframework.stereotype.Component;

import no.nav.tps.forvalteren.domain.service.tps.Request;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.transformers.Transformer;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.transformers.request.ServiceRoutineAdresseDataRequestTransform;

@Component
public class ServiceRoutineAdresseDataRequestTransformStrategy implements RequestTransformStrategy { //ny impl
    
    private static final String XML_PROPERTIES_PREFIX  = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\" standalone=\"yes\"?><tpsAdresseData xmlns=\"http://www.rtv.no/NamespaceTPS\">";
    private static final String XML_PROPERTIES_POSTFIX = "</tpsAdresseData>";
    
    @Override
    public void execute(Request request, Transformer transformer) {
        request.setXml(XML_PROPERTIES_PREFIX + request.getXml() + XML_PROPERTIES_POSTFIX);
    }
    
    @Override
    public boolean isSupported(Object o) {return o instanceof ServiceRoutineAdresseDataRequestTransform;    }
    
}
