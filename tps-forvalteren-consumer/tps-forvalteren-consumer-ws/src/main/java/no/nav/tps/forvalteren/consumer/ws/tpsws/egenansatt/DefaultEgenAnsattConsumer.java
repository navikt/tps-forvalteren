package no.nav.tps.forvalteren.consumer.ws.tpsws.egenansatt;

import java.security.SecureRandom;
import javax.xml.ws.soap.SOAPFaultException;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;

import lombok.extern.slf4j.Slf4j;
import no.nav.tjeneste.pip.egen.ansatt.v1.EgenAnsattV1;
import no.nav.tjeneste.pip.egen.ansatt.v1.WSHentErEgenAnsattEllerIFamilieMedEgenAnsattRequest;
import no.nav.tjeneste.pip.egen.ansatt.v1.WSHentErEgenAnsattEllerIFamilieMedEgenAnsattResponse;

@Slf4j
public class DefaultEgenAnsattConsumer implements EgenAnsattConsumer {

    private static final String PERSON_NOT_FOUND_TPSWS_ERROR = "PERSON IKKE FUNNET";
    private static final String INVALID_FNR_TPSWS_ERROR = "FØDSELSNUMMER INNGITT ER UGYLDIG";
    private static final String EMPTY_FNR_TPSWS_ERROR = "FNR MÅ FYLLES UT";
    private static final String MDC_CALL_ID = "callId";
    private static final String PING_FNR = "10108000398";
    private static final SecureRandom secureRandom = new SecureRandom();

    @Autowired
    private EgenAnsattV1 pipEgenAnsatt;

    @Override
    public boolean ping() {
        try {
            isEgenAnsatt(PING_FNR);
        } catch (RuntimeException exception) {
            log.warn("Pinging egenAnsatt failed with exception: {}", exception.toString());
            throw exception;
        }
        return true;
    }

    @Override
    public boolean isEgenAnsatt(String fnr) {
        if (fnr == null) {
            log.info("isEgenAnsatt called with Fnr == null");
            return false;
        }

        WSHentErEgenAnsattEllerIFamilieMedEgenAnsattRequest request = createRequest(fnr);

        MDC.put(MDC_CALL_ID, generateCallId());

        try {
            WSHentErEgenAnsattEllerIFamilieMedEgenAnsattResponse response = pipEgenAnsatt.hentErEgenAnsattEllerIFamilieMedEgenAnsatt(request);

            return response.isEgenAnsatt();
        } catch (SOAPFaultException exception) {
            log.info("TPSWS: isEgenAnsatt failed with exception: {}", exception.toString());

            boolean personNotFound = exception.getMessage().contains(PERSON_NOT_FOUND_TPSWS_ERROR);
            boolean invalidFnr = exception.getMessage().contains(INVALID_FNR_TPSWS_ERROR);
            boolean emptyFnr = exception.getMessage().contains(EMPTY_FNR_TPSWS_ERROR);

            if (personNotFound || invalidFnr || emptyFnr) {
                return false;
            }

            throw exception;
        } finally {
            MDC.remove(MDC_CALL_ID);
        }

    }

    private WSHentErEgenAnsattEllerIFamilieMedEgenAnsattRequest createRequest(String fnr) {
        WSHentErEgenAnsattEllerIFamilieMedEgenAnsattRequest request = new WSHentErEgenAnsattEllerIFamilieMedEgenAnsattRequest();
        request.setIdent(fnr);

        return request;
    }

    public static String generateCallId() {

        return new StringBuilder()
                .append("CallId_")
                .append(System.currentTimeMillis())
                .append('_')
                .append(secureRandom.nextInt(2147483647))
                .toString();
    }
}