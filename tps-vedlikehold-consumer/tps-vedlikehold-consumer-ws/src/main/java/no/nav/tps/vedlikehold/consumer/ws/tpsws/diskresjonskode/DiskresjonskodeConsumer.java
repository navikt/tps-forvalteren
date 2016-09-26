package no.nav.tps.vedlikehold.consumer.ws.tpsws.diskresjonskode;

import java.util.List;

import no.nav.tjeneste.pip.diskresjonskode.meldinger.HentDiskresjonskodeBolkResponse;
import no.nav.tjeneste.pip.diskresjonskode.meldinger.HentDiskresjonskodeResponse;

/**
 * @author Tobias Hansen (Visma Consulting AS).
 */
public interface DiskresjonskodeConsumer {
    boolean ping();

    HentDiskresjonskodeResponse getDiskresjonskode(String fNr);

    HentDiskresjonskodeBolkResponse getDiskresjonskodeBolk(List<String> fNrListe);
}
