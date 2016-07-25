package no.nav.tps.vedlikehold.consumer.ws.tpsws.egenansatt;

import no.nav.modig.common.MDCOperations;
import no.nav.tjeneste.pip.pipegenansatt.v1.PipEgenAnsattPortType;
import no.nav.tjeneste.pip.pipegenansatt.v1.meldinger.ErEgenAnsattEllerIFamilieMedEgenAnsattRequest;
import no.nav.tjeneste.pip.pipegenansatt.v1.meldinger.ErEgenAnsattEllerIFamilieMedEgenAnsattResponse;
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

    public static final String PERSON_NOT_FOUND_TPSWS_ERROR = "PERSON IKKE FUNNET";
    public static final String INVALID_FNR_TPSWS_ERROR      = "FØDSELSNUMMER INNGITT ER UGYLDIG";
    public static final String EMPTY_FNR_TPSWS_ERROR        = "FNR MÅ FYLLES UT";

    // Test user
    private static final String PING_FNR = "13037999916";

    @Autowired
    private PipEgenAnsattPortType pipEgenAnsattPortType;

    @Override
    public boolean ping() throws Exception {
        try {
            isEgenAnsatt(PING_FNR);
        } catch (Exception environment) {
            LOGGER.warn("Pinging egenAnsatt failed with exception: {}", environment.toString());

            throw environment;
        }
        return true;
    }

    @Override
    public boolean isEgenAnsatt(String fnr) {
        if (fnr == null) {
            LOGGER.info("isEgenAnsatt called with Fnr == null");

            return false;
        }

        ErEgenAnsattEllerIFamilieMedEgenAnsattRequest request = createRequest(fnr);
        ErEgenAnsattEllerIFamilieMedEgenAnsattResponse response;

        MDCOperations.putToMDC(MDCOperations.MDC_CALL_ID, MDCOperations.generateCallId());

        try {
            response = pipEgenAnsattPortType.erEgenAnsattEllerIFamilieMedEgenAnsatt(request);
        } catch (SOAPFaultException environment) {
            LOGGER.info("TPSWS: isEgenAnsatt failed with exception: {}", environment.toString());

            Boolean personNotFound = environment.getMessage().contains(PERSON_NOT_FOUND_TPSWS_ERROR);
            Boolean invalidFnr     = environment.getMessage().contains(INVALID_FNR_TPSWS_ERROR);
            Boolean emptyFnr       = environment.getMessage().contains(EMPTY_FNR_TPSWS_ERROR);

            if (personNotFound || invalidFnr || emptyFnr) {
                return false;
            }

            throw environment;
        } finally {
            MDCOperations.remove(MDCOperations.MDC_CALL_ID);
        }

        return response.isEgenAnsatt();
    }

    private ErEgenAnsattEllerIFamilieMedEgenAnsattRequest createRequest(String fnr) {
        ErEgenAnsattEllerIFamilieMedEgenAnsattRequest request = new ErEgenAnsattEllerIFamilieMedEgenAnsattRequest();
        request.setIdent(fnr);

        return request;
    }
}
