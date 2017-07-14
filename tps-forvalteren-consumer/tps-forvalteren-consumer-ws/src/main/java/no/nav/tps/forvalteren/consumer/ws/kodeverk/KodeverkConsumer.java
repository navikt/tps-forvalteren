package no.nav.tps.forvalteren.consumer.ws.kodeverk;

import no.nav.tps.forvalteren.domain.ws.kodeverk.Kodeverk;

import java.util.List;


public interface KodeverkConsumer {

    Kodeverk hentKodeverk(String navn);

    void ping();
}
