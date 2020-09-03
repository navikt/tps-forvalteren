package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints;

import javax.jms.JMSException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import no.nav.tps.forvalteren.common.java.logging.LogExceptions;
import no.nav.tps.forvalteren.domain.rs.RsPureXmlMessageResponse;
import no.nav.tps.forvalteren.domain.rs.RsTpsMelding;
import no.nav.tps.forvalteren.provider.rs.security.logging.BaseProvider;
import no.nav.tps.forvalteren.service.command.tps.xmlmelding.TpsXmlSender;

@RestController
@RequestMapping(value = "api/v1")
@ConditionalOnProperty(prefix = "tps.forvalteren", name = "production.mode", havingValue = "false")
public class XmlMeldingController extends BaseProvider {

    private static final String REST_SERVICE_NAME = "xmlMelding";

    @Autowired
    private TpsXmlSender tpsXmlSender;

    @LogExceptions
    @RequestMapping(value = "/xmlmelding", method = RequestMethod.POST)
    public RsPureXmlMessageResponse sendXmlMelding(@RequestBody RsTpsMelding rsTpsMelding) throws JMSException {

        RsPureXmlMessageResponse response = new RsPureXmlMessageResponse();
        response.setXml(tpsXmlSender.sendTpsMelding(rsTpsMelding));
        return response;
    }
}