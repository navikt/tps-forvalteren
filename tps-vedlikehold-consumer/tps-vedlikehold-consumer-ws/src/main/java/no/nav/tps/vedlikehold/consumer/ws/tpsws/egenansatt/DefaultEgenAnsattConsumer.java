package no.nav.tps.vedlikehold.consumer.ws.tpsws.egenansatt;

import no.nav.modig.common.MDCOperations;
import no.nav.tjeneste.pip.pipegenansatt.v1.PipEgenAnsattPortType;
import no.nav.tjeneste.pip.pipegenansatt.v1.meldinger.ErEgenAnsattEllerIFamilieMedEgenAnsattRequest;
import no.nav.tjeneste.pip.pipegenansatt.v1.meldinger.ErEgenAnsattEllerIFamilieMedEgenAnsattResponse;
import org.apache.cxf.common.i18n.Exception;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.ws.soap.SOAPFaultException;

/**
 * @author Tobias Hansen (Visma Consulting AS).
 */
@Component
public class DefaultEgenAnsattConsumer implements EgenAnsattConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultEgenAnsattConsumer.class);

    // Test user
    private static final String PING_FNR = "13037999916";

    @Autowired
    private PipEgenAnsattPortType pipEgenAnsattPortType;

    @Override
    public boolean ping() throws Exception {
        isEgenAnsatt(PING_FNR);

        return true;
    }

    @Override
    public boolean isEgenAnsatt(String fnr) {
        if (fnr == null) {
            LOGGER.info("isEgenAnsatt called with Fnr == null");

            return false;
        }

        ErEgenAnsattEllerIFamilieMedEgenAnsattRequest request = createRequest(fnr);
        MDCOperations.putToMDC(MDCOperations.MDC_CALL_ID, MDCOperations.generateCallId());

        ErEgenAnsattEllerIFamilieMedEgenAnsattResponse response;

        try {
            response = pipEgenAnsattPortType.erEgenAnsattEllerIFamilieMedEgenAnsatt(request);
            MDCOperations.remove(MDCOperations.MDC_CALL_ID);
        } catch (SOAPFaultException exception) {
            LOGGER.info("TPSWS: isEgenAnsatt failed with exception: {}", exception.toString());

            Boolean personNotFound = exception.getMessage().contains("PERSON IKKE FUNNET");
            Boolean invalidFnr     = exception.getMessage().contains("FØDSELSNUMMER INNGITT ER UGYLDIG");
            Boolean emptyFnr       = exception.getMessage().contains("FNR MÅ FYLLES UT");

            if (personNotFound || invalidFnr || emptyFnr) {
                return false;
            }

            throw exception;
        }

        return response.isEgenAnsatt();
    }

    private ErEgenAnsattEllerIFamilieMedEgenAnsattRequest createRequest(String fnr) {
        ErEgenAnsattEllerIFamilieMedEgenAnsattRequest request = new ErEgenAnsattEllerIFamilieMedEgenAnsattRequest();
        request.setIdent(fnr);

        return request;
    }
}
