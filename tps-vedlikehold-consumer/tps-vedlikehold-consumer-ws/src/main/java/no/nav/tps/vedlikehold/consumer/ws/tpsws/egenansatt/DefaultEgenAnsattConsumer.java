package no.nav.tps.vedlikehold.consumer.ws.tpsws.egenansatt;

import no.nav.tjeneste.pip.pipegenansatt.v1.PipEgenAnsattPortType;
import no.nav.tjeneste.pip.pipegenansatt.v1.meldinger.ErEgenAnsattEllerIFamilieMedEgenAnsattRequest;
import no.nav.tjeneste.pip.pipegenansatt.v1.meldinger.ErEgenAnsattEllerIFamilieMedEgenAnsattResponse;
import no.nav.tps.vedlikehold.consumer.ws.tpsws.exceptions.FNrEmptyException;
import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.util.ObjectUtils.isEmpty;

/**
 * @author Tobias Hansen (Visma Consulting AS).
 */
public class DefaultEgenAnsattConsumer implements EgenAnsattConsumer {

    // Test user
    private static final String PING_FNR = "13037999916";

    @Autowired
    private PipEgenAnsattPortType pipEgenAnsattPortType;

    @Override
    public boolean ping() throws Exception {
        try {
            isEgenAnsatt(PING_FNR);
        } catch (Exception e) {
            throw e;
        }
        return true;
    }

    @Override
    public boolean isEgenAnsatt(String fNr) {
        if (isEmpty(fNr)) {
            throw new FNrEmptyException();
        }

        ErEgenAnsattEllerIFamilieMedEgenAnsattRequest request = createRequest(fNr);

        ErEgenAnsattEllerIFamilieMedEgenAnsattResponse response = pipEgenAnsattPortType.erEgenAnsattEllerIFamilieMedEgenAnsatt(request);

        return response.isEgenAnsatt();
    }

    private ErEgenAnsattEllerIFamilieMedEgenAnsattRequest createRequest(String fNr) {
        ErEgenAnsattEllerIFamilieMedEgenAnsattRequest request = new ErEgenAnsattEllerIFamilieMedEgenAnsattRequest();
        request.setIdent(fNr);

        return request;
    }
}
