package no.nav.tps.forvalteren.consumer.ws.tpsws.diskresjonskode;

import no.nav.tjeneste.pip.diskresjonskode.meldinger.HentDiskresjonskodeResponse;
import no.nav.tps.forvalteren.consumer.ws.tpsws.AuthorisationStrategyConsumer;


public interface DiskresjonskodeConsumer extends AuthorisationStrategyConsumer {
    HentDiskresjonskodeResponse getDiskresjonskodeResponse(String fNr);
}
