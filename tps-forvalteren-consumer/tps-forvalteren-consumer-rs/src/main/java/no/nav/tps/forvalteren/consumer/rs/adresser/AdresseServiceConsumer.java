package no.nav.tps.forvalteren.consumer.rs.adresser;

import static java.lang.System.currentTimeMillis;

import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.extern.slf4j.Slf4j;
import no.nav.tps.forvalteren.consumer.rs.adresser.command.AdresseServiceCommand;
import no.nav.tps.forvalteren.consumer.rs.config.credentials.AdresseServiceProperties;
import no.nav.tps.forvalteren.consumer.rs.token.AccessTokenService;
import no.nav.tps.forvalteren.domain.rs.VegadresseDTO;

@Slf4j
@Service
public class AdresseServiceConsumer {

    private final WebClient webClient;
    private final AdresseServiceProperties serviceProperties;
    private final AccessTokenService accessTokenService;

    public AdresseServiceConsumer(
            AdresseServiceProperties serviceProperties,
            AccessTokenService accessTokenService) {

        this.serviceProperties = serviceProperties;
        this.webClient = WebClient.builder()
                .baseUrl(serviceProperties.getUrl())
                .build();
        this.accessTokenService = accessTokenService;
    }

    private static VegadresseDTO getDefaultAdresse() {

        return VegadresseDTO.builder()
                .matrikkelId("285693617")
                .adressenavn("FYRSTIKKALLÉEN")
                .postnummer("0661")
                .husnummer(2)
                .kommunenummer("0301")
                .build();
    }

    public List<VegadresseDTO> getAdresser(String query, Integer antall) {

        long startTime = currentTimeMillis();

        try {
            var accessToken = accessTokenService.generateToken(serviceProperties);
            var adresseResponse =
                    new AdresseServiceCommand(webClient, query, antall, accessToken.getTokenValue()).call();

            log.info("Adresseoppslag tok {} ms", currentTimeMillis() - startTime);
            return Arrays.asList(adresseResponse);

        } catch (RuntimeException e) {

            log.error("Henting av adresse feilet etter {} ms", currentTimeMillis() - startTime, e);
            return List.of(getDefaultAdresse());
        }
    }
}
