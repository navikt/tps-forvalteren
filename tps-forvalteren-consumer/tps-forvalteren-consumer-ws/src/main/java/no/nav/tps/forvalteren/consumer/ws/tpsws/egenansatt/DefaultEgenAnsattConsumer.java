package no.nav.tps.forvalteren.consumer.ws.tpsws.egenansatt;

import javax.xml.ws.soap.SOAPFaultException;

import no.nav.modig.common.MDCOperations;
import no.nav.tjeneste.pip.pipegenansatt.v1.PipEgenAnsattPortType;
import no.nav.tjeneste.pip.pipegenansatt.v1.meldinger.ErEgenAnsattEllerIFamilieMedEgenAnsattRequest;
import no.nav.tjeneste.pip.pipegenansatt.v1.meldinger.ErEgenAnsattEllerIFamilieMedEgenAnsattResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class DefaultEgenAnsattConsumer implements EgenAnsattConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultEgenAnsattConsumer.class);

    static final String PERSON_NOT_FOUND_TPSWS_ERROR = "PERSON IKKE FUNNET";
    static final String INVALID_FNR_TPSWS_ERROR      = "FØDSELSNUMMER INNGITT ER UGYLDIG";
    static final String EMPTY_FNR_TPSWS_ERROR        = "FNR MÅ FYLLES UT";

    private static final String PING_FNR = "10108000398";

    @Autowired
    private PipEgenAnsattPortType pipEgenAnsattPortType;

    @Override
    public boolean ping() {
        try {
            isEgenAnsatt(PING_FNR);
        } catch (RuntimeException exception) {
            LOGGER.warn("Pinging egenAnsatt failed with exception: {}", exception.toString());
            throw exception;
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

        MDCOperations.putToMDC(MDCOperations.MDC_CALL_ID, MDCOperations.generateCallId());

        try {
            ErEgenAnsattEllerIFamilieMedEgenAnsattResponse response = pipEgenAnsattPortType.erEgenAnsattEllerIFamilieMedEgenAnsatt(request);

            return response.isEgenAnsatt();
        } catch (SOAPFaultException exception) {
            LOGGER.info("TPSWS: isEgenAnsatt failed with exception: {}", exception.toString());

            boolean personNotFound = exception.getMessage().contains(PERSON_NOT_FOUND_TPSWS_ERROR);
            boolean invalidFnr     = exception.getMessage().contains(INVALID_FNR_TPSWS_ERROR);
            boolean emptyFnr       = exception.getMessage().contains(EMPTY_FNR_TPSWS_ERROR);

            if (personNotFound || invalidFnr || emptyFnr) {
                return false;
            }

            throw exception;
        } finally {
            MDCOperations.remove(MDCOperations.MDC_CALL_ID);
        }

    }

    private ErEgenAnsattEllerIFamilieMedEgenAnsattRequest createRequest(String fnr) {
        ErEgenAnsattEllerIFamilieMedEgenAnsattRequest request = new ErEgenAnsattEllerIFamilieMedEgenAnsattRequest();
        request.setIdent(fnr);

        return request;
    }
}
