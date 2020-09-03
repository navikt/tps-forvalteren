package no.nav.tps.forvalteren.consumer.ws.tpsws.diskresjonskode;

import java.security.SecureRandom;
import javax.xml.rpc.soap.SOAPFaultException;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;

import lombok.extern.slf4j.Slf4j;
import no.nav.tjeneste.pip.diskresjonskode.DiskresjonskodePortType;
import no.nav.tjeneste.pip.diskresjonskode.meldinger.HentDiskresjonskodeRequest;
import no.nav.tjeneste.pip.diskresjonskode.meldinger.HentDiskresjonskodeResponse;

@Slf4j
public class DefaultDiskresjonskodeConsumer implements DiskresjonskodeConsumer {

    private static final String NO_MATCHES_FOUND_TPSWS_ERROR = "Ingen forekomster funnet";
    private static final String INVALID_FNR_TPSWS_ERROR = "FØDSELSNUMMER INNGITT ER UGYLDIG";
    private static final String MDC_CALL_ID = "callId";

    private static final SecureRandom secureRandom = new SecureRandom();

    // Test user
    private static final String FNR_TEST_PERSON_IPROD = "10108000398";

    @Autowired
    private DiskresjonskodePortType diskresjonskodePortType;

    @Override
    public boolean ping() {
        try {
            getDiskresjonskodeResponse(FNR_TEST_PERSON_IPROD);
        } catch (RuntimeException exception) {
            log.warn("Pinging diskresjonskode failed with exception: {}", exception.toString());
            throw exception;
        }

        return true;
    }

    @Override
    public HentDiskresjonskodeResponse getDiskresjonskodeResponse(String fNr) {
        HentDiskresjonskodeRequest request = createRequest(fNr);

        MDC.put(MDC_CALL_ID, generateCallId());

        try {
            return diskresjonskodePortType.hentDiskresjonskode(request);
        } catch (SOAPFaultException exception) {
            log.warn("TPSWS: hentDiskresjonskode failed with exception: {}", exception.toString());

            boolean noMatchesFound = exception.getMessage().contains(NO_MATCHES_FOUND_TPSWS_ERROR);
            boolean invalidFnr = exception.getMessage().contains(INVALID_FNR_TPSWS_ERROR);

            if (noMatchesFound || invalidFnr) {
                HentDiskresjonskodeResponse response = new HentDiskresjonskodeResponse();
                response.setDiskresjonskode("");
                return response;
            }

            throw exception;
        } finally {
            MDC.remove(MDC_CALL_ID);
        }
    }

    private HentDiskresjonskodeRequest createRequest(String fNr) {
        HentDiskresjonskodeRequest request = new HentDiskresjonskodeRequest();
        request.setIdent(fNr);

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