package no.nav.tps.vedlikehold.consumer.ws.tpsws.diskresjonskode;

import no.nav.modig.common.MDCOperations;
import no.nav.tjeneste.pip.diskresjonskode.DiskresjonskodePortType;
import no.nav.tjeneste.pip.diskresjonskode.meldinger.HentDiskresjonskodeBolkRequest;
import no.nav.tjeneste.pip.diskresjonskode.meldinger.HentDiskresjonskodeBolkResponse;
import no.nav.tjeneste.pip.diskresjonskode.meldinger.HentDiskresjonskodeRequest;
import no.nav.tjeneste.pip.diskresjonskode.meldinger.HentDiskresjonskodeResponse;
import no.nav.tps.vedlikehold.consumer.ws.tpsws.exceptions.FNrEmptyException;
import no.nav.tps.vedlikehold.consumer.ws.tpsws.exceptions.PersonNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.util.ObjectUtils.isEmpty;

/**
 * @author Tobias Hansen (Visma Consulting AS).
 */
@Component
public class DefaultDiskresjonskodeConsumer implements DiskresjonskodeConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultDiskresjonskodeConsumer.class);
    public static final String DISKRESJONSKODE_NOT_FOUND_ERROR = "Ingen forekomster funnet";

    // Test user
    private static final String PING_FNR = "13037999916";

    @Autowired
    private DiskresjonskodePortType diskresjonskodePortType;

    @Override
    public boolean ping() throws Exception {
        try {
            getDiskresjonskode(PING_FNR);
        } catch (PersonNotFoundException e) {
            // At en person ikke finnes i diskresjonskode er bare en funksjonell feil,
            // ikke noe som skal logges eller håndteres som en teknisk feil.
            return true; //TODO: Bedre måte å gjøre dette på?
        } catch (Exception e) {
            LOGGER.warn("Ping mot diskresjonskode feilet med følgende exception", e);
            throw e;
        }
        return true;
    }

    @Override
    public HentDiskresjonskodeResponse getDiskresjonskode(String fNr) throws Exception {
        if (isEmpty(fNr)) {
            throw new FNrEmptyException();
        }

        HentDiskresjonskodeRequest request = createRequest(fNr);

        try {
            MDCOperations.putToMDC(MDCOperations.MDC_CALL_ID, MDCOperations.generateCallId());
            HentDiskresjonskodeResponse response = diskresjonskodePortType.hentDiskresjonskode(request);
            MDCOperations.remove(MDCOperations.MDC_CALL_ID);

            return response;
        } catch (Exception e) {
            if (DISKRESJONSKODE_NOT_FOUND_ERROR.equals(e.getMessage())) {
                throw new PersonNotFoundException(fNr, e);
            }

            throw e;
        }
    }

    @Override
    public HentDiskresjonskodeBolkResponse getDiskresjonskodeBolk(List<String> fNrListe) {
        if (isEmpty(fNrListe)) {
            throw new FNrEmptyException();
        }

        HentDiskresjonskodeBolkRequest request = createBulkRequest(fNrListe);

        return diskresjonskodePortType.hentDiskresjonskodeBolk(request);
    }

    private HentDiskresjonskodeBolkRequest createBulkRequest(List<String> fNrListe) {
        HentDiskresjonskodeBolkRequest request = new HentDiskresjonskodeBolkRequest();
        request.getIdentListe().addAll(fNrListe);

        return request;
    }

    private HentDiskresjonskodeRequest createRequest(String fNr) {
        HentDiskresjonskodeRequest request = new HentDiskresjonskodeRequest();
        request.setIdent(fNr);

        return request;
    }
}