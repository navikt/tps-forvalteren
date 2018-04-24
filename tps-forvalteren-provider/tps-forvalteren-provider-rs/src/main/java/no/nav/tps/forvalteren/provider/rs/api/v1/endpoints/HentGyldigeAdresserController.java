package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints;

import static no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.servicerutiner.S051FinnGyldigeAdresser.FINN_GYLDIGE_ADRESSER_SERVICERUTINE_NAVN;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.TpsRequestContext;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.hent.TpsFinnGyldigeAdresserRequest;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.hent.attributter.finngyldigeadresser.JaEllerNei;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.hent.attributter.finngyldigeadresser.Typesok;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.response.TpsServiceRoutineResponse;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.TpsRequestSender;
import no.nav.tps.forvalteren.service.user.UserContextHolder;

/**
 * Dette apiet tilbyr gyldige adresser som er hentet fra TPS MQ-tjenesten S051.
 */
@RestController
@RequestMapping(value = "api/v1/gyldigadresse")
public class HentGyldigeAdresserController {
    @Autowired
    private UserContextHolder userContextHolder;
    
    @Autowired
    private TpsRequestSender tpsRequestSender;
    
    /**
     * Denne tjenesten returnerer en tilfeldig adresse. Default returnerer den én adresse.
     * Dersom man ønsker flere, så må man oppgi antall i query.
     * Man kan også legge ved kriterier/spesifikasjoner i spørringen, på hva adressen må inneholde:
     * kommunenummer, gatenavn (minst 3 tegn), postnummer.
     */
    @GetMapping("/tilfeldig")
    public TpsServiceRoutineResponse hentTilfeldigAdresse(@RequestParam(required = false, defaultValue = "1") Integer antall, @RequestParam(required = false) String kommuneNr,
            @RequestParam(required = false) String postNr) {
        TpsFinnGyldigeAdresserRequest tpsServiceRoutineRequest = TpsFinnGyldigeAdresserRequest.builder()
                .typesok(Typesok.T)
                .maxRetur(antall)
                .kommuneNrsok(kommuneNr)
                .postNrsok(postNr)
                .alltidRetur(JaEllerNei.J)
                .alleSkrivevarianter(JaEllerNei.N)
                .visPostnr(JaEllerNei.J)
                .build();
        setServiceRoutineMeta(tpsServiceRoutineRequest);
        
        return tpsRequestSender.sendTpsRequest(tpsServiceRoutineRequest, createContext());
    }
    
    private TpsRequestContext createContext() {
        TpsRequestContext context = new TpsRequestContext();
        context.setUser(userContextHolder.getUser());
        context.setEnvironment("u6");
        return context;
    }
    
    @GetMapping("/autocomplete")
    public TpsServiceRoutineResponse finnGyldigAdresse(@ModelAttribute TpsFinnGyldigeAdresserRequest tpsServiceRoutineRequest) { //TODO requestparam klasse, tpservice-mapper og tps-servicerequest. Test denne rest-klassen (med typesok T)
        //        tpsServiceRoutineRequest = TpsFinnGyldigeAdresserRequest.builder()
        //                .adresseNavnsok("BOLIG")
        //                .typesok(Typesok.F)
        //                .alltidRetur(JaEllerNei.J)
        //                .alleSkrivevarianter(JaEllerNei.J)
        //                .visPostnr(JaEllerNei.J)
        //                .sortering("N")
        //                .build();
        setServiceRoutineMeta(tpsServiceRoutineRequest);
        return tpsRequestSender.sendTpsRequest(tpsServiceRoutineRequest, createContext());
        
    }
    
    private void setServiceRoutineMeta(TpsFinnGyldigeAdresserRequest tpsServiceRoutineRequest) {
        tpsServiceRoutineRequest.setServiceRutinenavn(FINN_GYLDIGE_ADRESSER_SERVICERUTINE_NAVN);
        tpsServiceRoutineRequest.setAksjonsKode("A");
        tpsServiceRoutineRequest.setAksjonsKode2("0");
        
    }
    
}
