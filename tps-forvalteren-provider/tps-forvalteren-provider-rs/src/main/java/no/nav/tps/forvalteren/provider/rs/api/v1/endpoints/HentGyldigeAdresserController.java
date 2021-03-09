package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints;

import static no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.hent.attributter.finngyldigeadresser.JaEllerNei.J;
import static no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.hent.attributter.finngyldigeadresser.JaEllerNei.N;
import static no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.hent.attributter.finngyldigeadresser.Typesok.F;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import no.nav.tps.forvalteren.common.logging.LogExceptions;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.hent.TpsFinnGyldigeAdresserRequest;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.hent.TpsFinnGyldigeAdresserResponse;
import no.nav.tps.forvalteren.provider.rs.api.v1.endpoints.mapping.MapFinnGyldigeAdresserToTpsServiceRutine;
import no.nav.tps.forvalteren.provider.rs.api.v1.endpoints.request.param.FinnGyldigeAdresserRequestParam;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.HentGyldigeAdresserService;

/**
 * Dette api-et tilbyr gyldige adresser som er hentet fra TPS MQ-tjenesten S051.
 */
@RestController
@RequestMapping(value = "api/v1/gyldigadresse")
public class HentGyldigeAdresserController {

    @Autowired
    private HentGyldigeAdresserService hentGyldigeAdresserService;

    @Autowired
    private MapFinnGyldigeAdresserToTpsServiceRutine finnGyldigeAdresserMapper;

    @LogExceptions
    @Operation(method = "Få tilfeldige, gyldige adresser", description = "Denne tjenesten returnerer en tilfeldig, gyldig adresse. Default returnerer den én adresse.\n"
            + "     * Dersom man ønsker flere, så må man oppgi antall i query.\n\r"
            + "     * Man kan også legge ved kriterier/spesifikasjoner i spørringen, på hva adressen må inneholde:\n\r"
            + "     * kommunenummer, gatenavn (minst 3 tegn), postnummer.")
    @GetMapping("/tilfeldig")
    public TpsFinnGyldigeAdresserResponse hentTilfeldigAdresse(@RequestParam(required = false, defaultValue = "1") Integer maxAntall,
            @RequestParam(required = false) String kommuneNr,
            @RequestParam(required = false) String postNr) {
        return hentGyldigeAdresserService.hentTilfeldigAdresse(maxAntall, kommuneNr, postNr);
    }

    @LogExceptions
    @Operation(method = "Søk etter gyldige adresser i TPS", description = "De query parametrene som ikke spesifiseres, følger default verdier. \n\r"
            + "Default verdier: typesok(F), visPostnr(J),alleSkrivevarianter(N),maxRetur(5),sortering(N). "
            + "Gyldige adresser er default sortert på adressenavn(N). Typesøket er Fonetisk(F).\n\r \n\r"
            + " Hvis alleSkrivevarianter=J og visPostNr=J, så returneres KUN adresser som har postnr.")
    @GetMapping("/autocomplete")
    public TpsFinnGyldigeAdresserResponse finnGyldigAdresse(
            @ModelAttribute FinnGyldigeAdresserRequestParam finnGyldigeAdresserRequestParam) {
        TpsFinnGyldigeAdresserRequest tpsServiceRoutineRequest = TpsFinnGyldigeAdresserRequest.builder().typesok(F).visPostnr(J).alleSkrivevarianter(N).alltidRetur(J).maxRetur(5).build();
        tpsServiceRoutineRequest = finnGyldigeAdresserMapper.map(finnGyldigeAdresserRequestParam, tpsServiceRoutineRequest);

        return hentGyldigeAdresserService.finnGyldigAdresse(tpsServiceRoutineRequest);
    }
}
