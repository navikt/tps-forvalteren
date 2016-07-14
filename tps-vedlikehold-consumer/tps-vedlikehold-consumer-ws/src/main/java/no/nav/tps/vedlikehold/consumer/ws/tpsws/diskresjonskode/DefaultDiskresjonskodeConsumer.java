package no.nav.tps.vedlikehold.consumer.ws.tpsws.diskresjonskode;

import no.nav.tjeneste.pip.diskresjonskode.DiskresjonskodePortType;
import no.nav.tjeneste.pip.diskresjonskode.meldinger.HentDiskresjonskodeBolkRequest;
import no.nav.tjeneste.pip.diskresjonskode.meldinger.HentDiskresjonskodeBolkResponse;
import no.nav.tjeneste.pip.diskresjonskode.meldinger.HentDiskresjonskodeRequest;
import no.nav.tjeneste.pip.diskresjonskode.meldinger.HentDiskresjonskodeResponse;
import no.nav.tps.vedlikehold.consumer.ws.tpsws.exceptions.FNrEmptyException;
import no.nav.tps.vedlikehold.consumer.ws.tpsws.exceptions.PersonNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.springframework.util.ObjectUtils.isEmpty;

/**
 * @author Tobias Hansen (Visma Consulting AS).
 */
public class DefaultDiskresjonskodeConsumer implements DiskresjonskodeConsumer {
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
            return diskresjonskodePortType.hentDiskresjonskode(request);
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