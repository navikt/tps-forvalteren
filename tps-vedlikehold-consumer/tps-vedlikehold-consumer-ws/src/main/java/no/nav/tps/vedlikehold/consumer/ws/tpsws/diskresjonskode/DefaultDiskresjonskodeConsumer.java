package no.nav.tps.vedlikehold.consumer.ws.tpsws.diskresjonskode;

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

import javax.xml.ws.soap.SOAPFaultException;
import java.util.List;

/**
 * @author Tobias Hansen (Visma Consulting AS).
 */
@Component
public class DefaultDiskresjonskodeConsumer implements DiskresjonskodeConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultDiskresjonskodeConsumer.class);

    public static final String NO_MATCHES_FOUND_TPSWS_ERROR = "Ingen forekomster funnet";
    public static final String INVALID_FNR_TPSWS_ERROR      = "FØDSELSNUMMER INNGITT ER UGYLDIG";

    // Test user
    private static final String PING_FNR = "13037999916";

    @Autowired
    private DiskresjonskodePortType diskresjonskodePortType;

    @Override
    public boolean ping() throws Exception {
        try {
            getDiskresjonskode(PING_FNR);
        } catch (Exception environment) {
            LOGGER.warn("Pinging diskresjonskode failed with exception: {}", environment.toString());

            throw environment;
        }

        return true;
    }

    @Override
    public HentDiskresjonskodeResponse getDiskresjonskode(String fNr) throws Exception {
        HentDiskresjonskodeRequest request = createRequest(fNr);
        HentDiskresjonskodeResponse response;

        MDCOperations.putToMDC(MDCOperations.MDC_CALL_ID, MDCOperations.generateCallId());

        try {
            response = diskresjonskodePortType.hentDiskresjonskode(request);

            return response;
        } catch (SOAPFaultException environment) {
            LOGGER.info("TPSWS: hentDiskresjonskode failed with exception: {}", environment.toString());

            Boolean noMatchesFound = environment.getMessage().contains(NO_MATCHES_FOUND_TPSWS_ERROR);
            Boolean invalidFnr = environment.getMessage().contains(INVALID_FNR_TPSWS_ERROR);

            if (noMatchesFound || invalidFnr) {
                response = new HentDiskresjonskodeResponse();
                response.setDiskresjonskode("");

                return response;
            }

            throw environment;
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