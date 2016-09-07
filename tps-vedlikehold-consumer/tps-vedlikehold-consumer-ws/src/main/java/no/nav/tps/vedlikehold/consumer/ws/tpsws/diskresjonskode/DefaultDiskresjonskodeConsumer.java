package no.nav.tps.vedlikehold.consumer.ws.tpsws.diskresjonskode;

import java.util.List;

import javax.xml.ws.soap.SOAPFaultException;

import no.nav.modig.common.MDCOperations;
import no.nav.tjeneste.pip.diskresjonskode.DiskresjonskodePortType;
import no.nav.tjeneste.pip.diskresjonskode.meldinger.HentDiskresjonskodeBolkRequest;
import no.nav.tjeneste.pip.diskresjonskode.meldinger.HentDiskresjonskodeBolkResponse;
import no.nav.tjeneste.pip.diskresjonskode.meldinger.HentDiskresjonskodeRequest;
import no.nav.tjeneste.pip.diskresjonskode.meldinger.HentDiskresjonskodeResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Tobias Hansen (Visma Consulting AS).
 */
@Component
public class DefaultDiskresjonskodeConsumer implements DiskresjonskodeConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultDiskresjonskodeConsumer.class);

    static final String NO_MATCHES_FOUND_TPSWS_ERROR = "Ingen forekomster funnet";
    static final String INVALID_FNR_TPSWS_ERROR      = "FØDSELSNUMMER INNGITT ER UGYLDIG";

    // Test user
    private static final String PING_FNR = "13037999916";

    @Autowired
    private DiskresjonskodePortType diskresjonskodePortType;

    @Override
    public boolean ping() {
        try {
            getDiskresjonskode(PING_FNR);
        } catch (RuntimeException exception) {
            LOGGER.warn("Pinging diskresjonskode failed with exception: {}", exception.toString());
            throw exception;
        }

        return true;
    }

    @Override
    public HentDiskresjonskodeResponse getDiskresjonskode(String fNr) {
        HentDiskresjonskodeRequest request = createRequest(fNr);

        MDCOperations.putToMDC(MDCOperations.MDC_CALL_ID, MDCOperations.generateCallId());

        try {
            return diskresjonskodePortType.hentDiskresjonskode(request);
        } catch (SOAPFaultException exception) {
            LOGGER.info("TPSWS: hentDiskresjonskode failed with exception: {}", exception.toString());

            boolean noMatchesFound = exception.getMessage().contains(NO_MATCHES_FOUND_TPSWS_ERROR);
            boolean invalidFnr     = exception.getMessage().contains(INVALID_FNR_TPSWS_ERROR);

            if (noMatchesFound || invalidFnr) {
                HentDiskresjonskodeResponse response = new HentDiskresjonskodeResponse();
                response.setDiskresjonskode("");
                return response;
            }

            throw exception;
        } finally {
            MDCOperations.remove(MDCOperations.MDC_CALL_ID);
        }
    }

    @Override
    public HentDiskresjonskodeBolkResponse getDiskresjonskodeBolk(List<String> fNrListe) {
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