package no.nav.tps.vedlikehold.consumer.ws.tpsws.egenansatt;

import no.nav.modig.common.MDCOperations;
import no.nav.tjeneste.pip.pipegenansatt.v1.PipEgenAnsattPortType;
import no.nav.tjeneste.pip.pipegenansatt.v1.meldinger.ErEgenAnsattEllerIFamilieMedEgenAnsattRequest;
import no.nav.tjeneste.pip.pipegenansatt.v1.meldinger.ErEgenAnsattEllerIFamilieMedEgenAnsattResponse;
import no.nav.tps.vedlikehold.consumer.ws.tpsws.exceptions.FNrEmptyException;
import org.apache.cxf.common.i18n.Exception;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.ws.soap.SOAPFaultException;

import static org.springframework.util.ObjectUtils.isEmpty;

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
    public boolean isEgenAnsatt(String fNr) {
        if (isEmpty(fNr)) {
            throw new FNrEmptyException();
        }

        ErEgenAnsattEllerIFamilieMedEgenAnsattRequest request = createRequest(fNr);
        MDCOperations.putToMDC(MDCOperations.MDC_CALL_ID, MDCOperations.generateCallId());

        ErEgenAnsattEllerIFamilieMedEgenAnsattResponse response;

        try {
            response = pipEgenAnsattPortType.erEgenAnsattEllerIFamilieMedEgenAnsatt(request);
            MDCOperations.remove(MDCOperations.MDC_CALL_ID);
        } catch (SOAPFaultException exception) {

            if (exception.getMessage().contains("PERSON IKKE FUNNET")) {
                LOGGER.info("TPSWS: isEgenAnsatt failed with exception: Person not found");

                return false;
            } else if (exception.getMessage().contains("FØDSELSNUMMER INNGITT ER UGYLDIG")) {
                LOGGER.info("TPSWS: isEgenAnsatt failed with exception: Invalid Fnr");

                return false;
            }

            LOGGER.error("TPSWS: isEgenAnsatt failed with exception: {}", exception.toString());

            throw exception;
        }

        return response.isEgenAnsatt();
    }

    private ErEgenAnsattEllerIFamilieMedEgenAnsattRequest createRequest(String fNr) {
        ErEgenAnsattEllerIFamilieMedEgenAnsattRequest request = new ErEgenAnsattEllerIFamilieMedEgenAnsattRequest();
        request.setIdent(fNr);

        return request;
    }
}
