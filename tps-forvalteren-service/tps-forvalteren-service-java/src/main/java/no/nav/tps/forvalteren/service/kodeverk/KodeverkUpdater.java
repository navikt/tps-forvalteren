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
        updateTpsfKommuneNummerCache();
        updateTpsfPostNummerCache();
        updateTpsfLandkoderCache();
    }

    private void updateTpsfKommuneNummerCache() {
        Kodeverk remoteKodeverkKommune = kodeverkConsumer.hentKodeverk(KodeverkConstants.KODEVERK_KOMMUNER_NAVN);

        if (remoteKodeverkKommune != null) {

            kodeverkCache.clearKommuneCache();
            kodeverkCache.setKodeverkKommuneKoder(remoteKodeverkKommune.getKoder());

            for (Kode kode : remoteKodeverkKommune.getKoder()) {
                kodeverkCache.getKodeverkKommunerMap().put(kode.getNavn(), kode);
            }
        }
    }

    private void updateTpsfPostNummerCache() {
        Kodeverk remoteKodeverkPostnummer = kodeverkConsumer.hentKodeverk(KodeverkConstants.KODEVERK_POSTNUMMER_NAVN);

        if (remoteKodeverkPostnummer != null) {

            kodeverkCache.clearPostnummerCache();
            kodeverkCache.setKodeverkPostnummerKoder(remoteKodeverkPostnummer.getKoder());

            for (Kode kode : remoteKodeverkPostnummer.getKoder()) {
                kodeverkCache.getKodeverkPostnummerMap().put(kode.getNavn(), kode);
            }
        }
    }
    
    private void updateTpsfLandkoderCache() {
        Kodeverk remoteKodeverkLandkoder = kodeverkConsumer.hentKodeverk(KodeverkConstants.KODEVERK_LANDKODER_NAVN);

        if (remoteKodeverkLandkoder != null) {

            kodeverkCache.clearLandkoderCache();
            kodeverkCache.setKodeverkLandkoder(remoteKodeverkLandkoder.getKoder());

            for (Kode kode : remoteKodeverkLandkoder.getKoder()) {
                kodeverkCache.getKodeverkLandkoderMap().put(kode.getNavn(), kode);
            }
        }
    }
}
