package no.nav.tps.vedlikehold.service.command.tps.transformation.request;

import com.fasterxml.jackson.xml.XmlMapper;
import no.nav.tps.vedlikehold.domain.service.command.tps.Request;
import no.nav.tps.vedlikehold.domain.service.command.tps.TpsSystemInfo;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.transformers.request.EndringsmeldingRequestTransform;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class EndringsmeldingRequestTransformStrategy implements RequestTransformStrategy {

    private static final String XML_PROPERTIES_PREFIX  = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><sfePersonData> <sfeAjourforing>";
    private static final String XML_PROPERTIES_POSTFIX = "</sfeAjourforing> </sfePersonData>";

    @Autowired
    private XmlMapper xmlMapper;


    @Override
    public void execute(Request request) {
        request.setXml(XML_PROPERTIES_PREFIX + resolveTpsSysInfoAsXml(request) + request.getXml() + XML_PROPERTIES_POSTFIX);
    }


    private String resolveTpsSysInfoAsXml(Request request) {
        String xml = "";
        TpsSystemInfo systemInfo = new TpsSystemInfo(request.getRoutineRequest().getParamValue("kilde"), request.getUser().getUsername());
        try {
            xml = xmlMapper.writeValueAsString(systemInfo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return xml;
    }


    @Override
    public boolean isSupported(Object o) {
        return o instanceof EndringsmeldingRequestTransform;
    }
}
