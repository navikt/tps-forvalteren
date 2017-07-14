package no.nav.tps.forvalteren.service.kodeverk;


import no.nav.tps.forvalteren.common.java.message.MessageProvider;
import no.nav.tps.forvalteren.consumer.ws.kodeverk.KodeverkConsumer;
import no.nav.tps.forvalteren.domain.ws.kodeverk.Kode;
import no.nav.tps.forvalteren.domain.ws.kodeverk.Kodeverk;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

import static no.nav.tps.forvalteren.service.kodeverk.KodeverkConstants.KODEVERK_KOMMUNER_NAVN;

@Component
public class KodeverkUpdater {

    @Autowired
    private KodeverkCache kodeverkCache;

    @Autowired
    private KodeverkConsumer kodeverkConsumer;

    @Autowired
    private MessageProvider messageProvider;

    public void updateTpsfKodeverkCache() {
        Kodeverk remotekoderverk = kodeverkConsumer.hentKodeverk(KODEVERK_KOMMUNER_NAVN);
        if(remotekoderverk != null){
            kodeverkCache.clearCache();

            for(Kode kode : remotekoderverk.getKoder()){

            }
        }


    }
}
