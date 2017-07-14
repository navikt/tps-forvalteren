package no.nav.tps.forvalteren.consumer.ws.kodeverk;

import ma.glasnost.orika.MapperFacade;
import no.nav.tjeneste.virksomhet.kodeverk.v2.HentKodeverkHentKodeverkKodeverkIkkeFunnet;
import no.nav.tjeneste.virksomhet.kodeverk.v2.KodeverkPortType;
import no.nav.tjeneste.virksomhet.kodeverk.v2.meldinger.HentKodeverkRequest;
import no.nav.tjeneste.virksomhet.kodeverk.v2.meldinger.HentKodeverkResponse;
import no.nav.tps.forvalteren.common.java.message.MessageProvider;
import no.nav.tps.forvalteren.domain.ws.kodeverk.Kodeverk;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static no.nav.tps.forvalteren.common.java.message.MessageConstants.KODEVERK_NOT_FOUND_KEY;


@Component
public class DefaultKodeverkConsumer implements KodeverkConsumer {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultKodeverkConsumer.class);

    @Autowired
    private KodeverkPortType kodeverkWs;

    @Autowired
    private MapperFacade mapper;

    @Autowired
    private MessageProvider messageProvider;

    @Override
    public Kodeverk hentKodeverk(String navn) {
        HentKodeverkRequest request = new HentKodeverkRequest();
        request.setNavn(navn);

        try {
            HentKodeverkResponse response = kodeverkWs.hentKodeverk(request);
            return mapper.map(response.getKodeverk(), Kodeverk.class);
        } catch (HentKodeverkHentKodeverkKodeverkIkkeFunnet exception) {
            LOG.error(messageProvider.get(KODEVERK_NOT_FOUND_KEY, navn), exception);
            return null;
        }
    }

    @Override
    public void ping() {
        kodeverkWs.ping();
    }
}
