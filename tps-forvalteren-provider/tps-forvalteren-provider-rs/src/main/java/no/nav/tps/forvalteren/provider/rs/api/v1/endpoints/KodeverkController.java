package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints;

import no.nav.tps.forvalteren.consumer.ws.kodeverk.KodeverkConsumer;
import no.nav.tps.forvalteren.domain.ws.kodeverk.Kodeverk;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/v1/kodeverk")
@ConditionalOnProperty(prefix = "tps.forvalteren", name = "production-mode", havingValue = "false")
public class KodeverkController {

    @Autowired
    private KodeverkConsumer kodeverkConsumer;

    @RequestMapping(value = "/{kodeverk}", method = RequestMethod.GET)
    public void getKodeverk(@PathVariable("kodeverk") String kodeverkNavn) {
        Kodeverk kodeverk = kodeverkConsumer.hentKodeverk(kodeverkNavn);
        return;
    }

}
