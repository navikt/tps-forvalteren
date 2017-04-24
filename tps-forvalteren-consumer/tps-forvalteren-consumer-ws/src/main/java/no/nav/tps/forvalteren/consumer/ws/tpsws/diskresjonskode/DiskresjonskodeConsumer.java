package no.nav.tps.forvalteren.consumer.ws.tpsws.diskresjonskode;

import java.util.List;

import no.nav.tjeneste.pip.diskresjonskode.meldinger.HentDiskresjonskodeBolkResponse;
import no.nav.tjeneste.pip.diskresjonskode.meldinger.HentDiskresjonskodeResponse;
import no.nav.tps.forvalteren.consumer.ws.tpsws.AuthorisationStrategyConsumer;


public interface DiskresjonskodeConsumer extends AuthorisationStrategyConsumer {
    HentDiskresjonskodeResponse getDiskresjonskodeResponse(String fNr);

    HentDiskresjonskodeBolkResponse getDiskresjonskodeBolk(List<String> fNrListe);
}
