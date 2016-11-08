package no.nav.tps.vedlikehold.consumer.ws.tpsws.diskresjonskode;

import java.util.List;

import no.nav.tjeneste.pip.diskresjonskode.meldinger.HentDiskresjonskodeBolkResponse;
import no.nav.tjeneste.pip.diskresjonskode.meldinger.HentDiskresjonskodeResponse;
import no.nav.tps.vedlikehold.consumer.ws.tpsws.AuthorisationStrategyConsumer;

/**
 * @author Tobias Hansen (Visma Consulting AS).
 */
public interface DiskresjonskodeConsumer extends AuthorisationStrategyConsumer {
    HentDiskresjonskodeResponse getDiskresjonskode(String fNr);

    HentDiskresjonskodeBolkResponse getDiskresjonskodeBolk(List<String> fNrListe);
}
