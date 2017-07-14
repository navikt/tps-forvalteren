package no.nav.tps.forvalteren.service.kodeverk;

import no.nav.tps.forvalteren.consumer.ws.kodeverk.KodeverkConsumer;
import no.nav.tps.forvalteren.domain.ws.kodeverk.Kode;
import no.nav.tps.forvalteren.domain.ws.kodeverk.Kodeverk;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class KodeverkUpdater {

    @Autowired
    private KodeverkCache kodeverkCache;

    @Autowired
    private KodeverkConsumer kodeverkConsumer;

    public void updateTpsfKodeverkCache() {
        Kodeverk remotekoderverk = kodeverkConsumer.hentKodeverk(KodeverkConstants.KODEVERK_KOMMUNER_NAVN);

        if(remotekoderverk != null) {

            kodeverkCache.clearCache();
            kodeverkCache.setKodeverkKommuneKoder(remotekoderverk.getKoder());

            for (Kode kode : remotekoderverk.getKoder()) {
                kodeverkCache.getKodeverkKommunerMap().put(kode.getNavn(), kode);
            }
        }

    }
}
