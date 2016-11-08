//package no.nav.tps.vedlikehold.service.command.tps.utils;
//
//import com.fasterxml.jackson.xml.XmlMapper;
//import no.nav.tps.vedlikehold.domain.service.command.tps.TpsRequest;
//import no.nav.tps.vedlikehold.domain.service.command.tps.TpsSystemInfo;
//import no.nav.tps.vedlikehold.domain.service.command.tps.ajourforing.requests.TpsRequestEndringsmelding;
//import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.requests.TpsRequestServiceRoutine;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//
///**
// * Created by f148888 on 27.10.2016.
// */
//
//@Component
//public class TpsRequestXmlCreator {
//
//    @Autowired
//    private XmlMapper xmlMapper;
//
//    private static final String XML_PROPERTIES_PREFIX  = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><tpsPersonData>";
//    private static final String XML_PROPERTIES_POSTFIX = "</tpsPersonData>";
//    private static final String XML_PROPERTIES_PREFIX_ENDRE_FAKE = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><sfePersonData> <sfeAjourforing> <systemInfo><kilde>FS22</kilde><brukerID>Z990485</brukerID></systemInfo>";
//    private static final String XML_PROPERTIES_PREFIX_ENDRE  = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><sfePersonData> <sfeAjourforing>";
//    private static final String XML_PROPERTIES_POSTFIX_ENDRE = "</sfeAjourforing> </sfePersonData>";
//
//    public String writeTpsRequestToXml(TpsRequest tpsRequest) throws IOException {
//        return xmlMapper.writeValueAsString(tpsRequest);
//    }
//
//    public String createXmlTpsRequestServiceRutine(TpsRequestServiceRoutine tpsRequest) throws IOException{
//        return XML_PROPERTIES_PREFIX + xmlMapper.writeValueAsString(tpsRequest) + XML_PROPERTIES_POSTFIX;     //TODO: This class shouldnt be responsible for message construction
//    }
//
//    public String createXmlTpsRequestEndringsmelding(TpsRequestEndringsmelding tpsRequest, TpsSystemInfo tpsSystemInfo) throws IOException{
//        return XML_PROPERTIES_PREFIX_ENDRE + xmlMapper.writeValueAsString(tpsSystemInfo)
//                + xmlMapper.writeValueAsString(tpsRequest) + XML_PROPERTIES_POSTFIX_ENDRE;     //TODO: This class shouldnt be responsible for message construction
//    }
//}
