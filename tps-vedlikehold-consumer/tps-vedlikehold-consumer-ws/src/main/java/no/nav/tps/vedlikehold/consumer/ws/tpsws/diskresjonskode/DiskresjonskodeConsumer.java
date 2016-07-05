package no.nav.tps.vedlikehold.consumer.ws.tpsws.diskresjonskode;

import no.nav.tjeneste.pip.diskresjonskode.meldinger.HentDiskresjonskodeResponse;

/**
 * @author Tobias Hansen (Visma Consulting AS).
 */
public interface DiskresjonskodeConsumer {
    HentDiskresjonskodeResponse getDiskresjonskode(final String fNr) throws Exception;

    boolean ping() throws Exception;
}
