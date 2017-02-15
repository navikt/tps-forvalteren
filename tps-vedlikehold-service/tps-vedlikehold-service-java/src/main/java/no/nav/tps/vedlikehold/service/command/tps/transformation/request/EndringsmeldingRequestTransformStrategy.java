package no.nav.tps.vedlikehold.service.command.tps.transformation.request;

import com.fasterxml.jackson.xml.XmlMapper;
import no.nav.tps.vedlikehold.domain.service.tps.Request;
import no.nav.tps.vedlikehold.domain.service.tps.TpsSystemInfo;
import no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.requests.TpsServiceRoutineEndringRequest;
import no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.transformers.Transformer;
import no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.transformers.request.EndringsmeldingRequestTransform;
import no.nav.tps.vedlikehold.service.command.exceptions.XmlWriteException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class EndringsmeldingRequestTransformStrategy implements RequestTransformStrategy {

    private static final String XML_PROPERTIES_PREFIX  = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><sfePersonData><sfeAjourforing>";
    private static final String XML_PROPERTIES_POSTFIX = "</sfeAjourforing></sfePersonData>";

    private static final Logger LOGGER = LoggerFactory.getLogger(EndringsmeldingRequestTransformStrategy.class);

    @Autowired
    private XmlMapper xmlMapper;


    @Override
    public void execute(Request request, Transformer transformer) {
        request.setXml(XML_PROPERTIES_PREFIX + resolveTpsSysInfoAsXml(request) + request.getXml() + XML_PROPERTIES_POSTFIX);
    }


    private String resolveTpsSysInfoAsXml(Request request) {
        String xml;
        TpsServiceRoutineEndringRequest endringRequest = (TpsServiceRoutineEndringRequest) request.getRoutineRequest();
        TpsSystemInfo systemInfo = new TpsSystemInfo(endringRequest.getKilde(), request.getContext().getUser().getUsername());
        try {
            xml = xmlMapper.writeValueAsString(systemInfo);
        } catch (IOException e) {
            LOGGER.error("Could not write xml as string");
            throw new XmlWriteException(e);
        }
        return xml;
    }


    @Override
    public boolean isSupported(Object o) {
        return o instanceof EndringsmeldingRequestTransform;
    }
}
