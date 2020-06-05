package no.nav.tps.forvalteren.consumer.rs.kodeverk;

import static java.lang.String.format;
import static no.nav.tps.forvalteren.common.java.message.MessageConstants.KODEVERK_NOT_FOUND_KEY;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.net.URI;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import no.nav.tps.forvalteren.common.java.message.MessageProvider;
import no.nav.tps.forvalteren.domain.rs.kodeverk.Kodeverk;

@Slf4j
@Service
@RequiredArgsConstructor
public class KodeverkConsumer {

    @Value("${provider.kodeverk.v1.url}")
    private String kodeverkUrl;

    private final MapperFacade mapperFacade;
    private final RestTemplate restTemplate;
    private final MessageProvider messageProvider;

    private static final String NAV_CALL_ID= "Nav-Call-Id";
    private static final String NAV_CONSUMER_ID = "Nav-Consumer-Id";
    private static final String CONSUMER = "tps-forvalteren";
    private static final String KODEVERK_BETYDNINGER_URL = "%s/api/v1/kodeverk/%s/koder/betydninger?spraak=norsk";

    public Kodeverk hentKodeverk(String kodeverkNavn) {

        try {
            ResponseEntity<KodeverkResponse> response = restTemplate.exchange(
                    RequestEntity.get(URI.create(format(KODEVERK_BETYDNINGER_URL, kodeverkUrl, kodeverkNavn)))
                    .header(NAV_CALL_ID, getUuid())
                    .header(NAV_CONSUMER_ID, CONSUMER)
                    .build(), KodeverkResponse.class);

            if (response.hasBody()) {
                return mapperFacade.map(response.getBody(), Kodeverk.class);
            } else {
                throw new HttpClientErrorException(NOT_FOUND, format("Kodeverk %s ikke funnet", kodeverkNavn));
            }

        } catch (RuntimeException exception) {
            log.error(messageProvider.get(KODEVERK_NOT_FOUND_KEY, kodeverkNavn), exception);
            return null;
        }
    }

    private static String getUuid() {
        return format("%s %s", CONSUMER, UUID.randomUUID());
    }
}
