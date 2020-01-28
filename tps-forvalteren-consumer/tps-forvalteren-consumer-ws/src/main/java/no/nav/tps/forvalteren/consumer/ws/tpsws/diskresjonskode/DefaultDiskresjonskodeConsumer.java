package no.nav.tps.forvalteren.consumer.ws.tpsws.diskresjonskode;

import java.util.List;
import javax.xml.ws.soap.SOAPFaultException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import no.nav.modig.common.MDCOperations;
import no.nav.tjeneste.pip.diskresjonskode.binding.DiskresjonskodePortType;
import no.nav.tjeneste.pip.diskresjonskode.meldinger.HentDiskresjonskodeBolkRequest;
import no.nav.tjeneste.pip.diskresjonskode.meldinger.HentDiskresjonskodeBolkResponse;
import no.nav.tjeneste.pip.diskresjonskode.meldinger.HentDiskresjonskodeRequest;
import no.nav.tjeneste.pip.diskresjonskode.meldinger.HentDiskresjonskodeResponse;

public class DefaultDiskresjonskodeConsumer implements DiskresjonskodeConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultDiskresjonskodeConsumer.class);

    static final String NO_MATCHES_FOUND_TPSWS_ERROR = "Ingen forekomster funnet";
    static final String INVALID_FNR_TPSWS_ERROR      = "FØDSELSNUMMER INNGITT ER UGYLDIG";

    // Test user
    private static final String FNR_TEST_PERSON_IPROD = "10108000398";

    @Autowired
    private DiskresjonskodePortType diskresjonskodePortType;

    @Override
    public boolean ping() {
        try {
            getDiskresjonskodeResponse(FNR_TEST_PERSON_IPROD);
        } catch (RuntimeException exception) {
            LOGGER.warn("Pinging diskresjonskode failed with exception: {}", exception.toString());
            throw exception;
        }

        return true;
    }

    @Override
    public HentDiskresjonskodeResponse getDiskresjonskodeResponse(String fNr) {
        HentDiskresjonskodeRequest request = createRequest(fNr);

        MDCOperations.putToMDC(MDCOperations.MDC_CALL_ID, MDCOperations.generateCallId());

        try {
            return diskresjonskodePortType.hentDiskresjonskode(request);
        } catch (SOAPFaultException exception) {
            LOGGER.warn("TPSWS: hentDiskresjonskode failed with exception: {}", exception.toString());

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