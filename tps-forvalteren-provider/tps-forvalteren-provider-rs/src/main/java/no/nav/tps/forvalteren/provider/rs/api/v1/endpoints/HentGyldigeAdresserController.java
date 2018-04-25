package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.hent.TpsFinnGyldigeAdresserRequest;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.response.TpsServiceRoutineResponse;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.HentGyldigeAdresserService;

/**
 * Dette apiet tilbyr gyldige adresser som er hentet fra TPS MQ-tjenesten S051.
 */
@RestController
@RequestMapping(value = "api/v1/gyldigadresse")
public class HentGyldigeAdresserController {
    @Autowired
    HentGyldigeAdresserService hentGyldigeAdresserService;
    
    /**
     * Denne tjenesten returnerer en tilfeldig adresse. Default returnerer den én adresse.
     * Dersom man ønsker flere, så må man oppgi antall i query.
     * Man kan også legge ved kriterier/spesifikasjoner i spørringen, på hva adressen må inneholde:
     * kommunenummer, gatenavn (minst 3 tegn), postnummer.
     */
    @GetMapping("/tilfeldig")
    public TpsServiceRoutineResponse hentTilfeldigAdresse(@RequestParam(required = false, defaultValue = "1") Integer antall,
            @RequestParam(required = false) String kommuneNr,
            @RequestParam(required = false) String postNr) {
        return hentGyldigeAdresserService.hentTilfeldigAdresse(antall, kommuneNr, postNr);
    }
    
    @GetMapping("/autocomplete")
    public TpsServiceRoutineResponse finnGyldigAdresse(
            @ModelAttribute TpsFinnGyldigeAdresserRequest tpsServiceRoutineRequest) { //TODO requestparam klasse, tpservice-mapper og tps-servicerequest. Test denne rest-klassen (med typesok T)
        return hentGyldigeAdresserService.finnGyldigAdresse(tpsServiceRoutineRequest);
    }
    
}
