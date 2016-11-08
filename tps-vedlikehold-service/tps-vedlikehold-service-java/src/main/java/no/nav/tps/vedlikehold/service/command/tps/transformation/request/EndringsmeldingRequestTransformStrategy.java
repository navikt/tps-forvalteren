package no.nav.tps.vedlikehold.service.command.tps.transformation.request;

import no.nav.tps.vedlikehold.domain.service.command.tps.Request;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.transformers.request.EndringsmeldingRequestTransform;
import org.springframework.stereotype.Component;

@Component
public class EndringsmeldingRequestTransformStrategy implements RequestTransformStrategy {

    private static final String XML_PROPERTIES_PREFIX  = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><sfePersonData> <sfeAjourforing>";
    private static final String XML_PROPERTIES_POSTFIX = "</sfeAjourforing> </sfePersonData>";

    @Override
    public void execute(Request request) {
        request.setXml(XML_PROPERTIES_PREFIX + request.getXml() + XML_PROPERTIES_POSTFIX);
    }

    @Override
    public boolean isSupported(Object o) {
        return o instanceof EndringsmeldingRequestTransform;
    }
}
