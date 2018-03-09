package no.nav.tps.forvalteren.consumer.ws.tpsws.egenansatt;

import no.nav.modig.common.MDCOperations;
import no.nav.tjeneste.pip.egenansatt.v1.binding.EgenAnsattV1;
import no.nav.tjeneste.pip.egenansatt.v1.meldinger.HentErEgenAnsattEllerIFamilieMedEgenAnsattRequest;
import no.nav.tjeneste.pip.egenansatt.v1.meldinger.HentErEgenAnsattEllerIFamilieMedEgenAnsattResponse;

import javax.xml.ws.soap.SOAPFaultException;
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
    private EgenAnsattV1 pipEgenAnsatt;

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

        HentErEgenAnsattEllerIFamilieMedEgenAnsattRequest request = createRequest(fnr);

        MDCOperations.putToMDC(MDCOperations.MDC_CALL_ID, MDCOperations.generateCallId());

        try {
            HentErEgenAnsattEllerIFamilieMedEgenAnsattResponse response = pipEgenAnsatt.hentErEgenAnsattEllerIFamilieMedEgenAnsatt(request);

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

    private HentErEgenAnsattEllerIFamilieMedEgenAnsattRequest createRequest(String fnr) {
        HentErEgenAnsattEllerIFamilieMedEgenAnsattRequest request = new HentErEgenAnsattEllerIFamilieMedEgenAnsattRequest();
        request.setIdent(fnr);

        return request;
    }
}
