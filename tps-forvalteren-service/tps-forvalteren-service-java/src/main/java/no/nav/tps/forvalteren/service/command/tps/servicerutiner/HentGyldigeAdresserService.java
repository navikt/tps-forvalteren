package no.nav.tps.forvalteren.service.command.tps.servicerutiner;

import static no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.servicerutiner.S051FinnGyldigeAdresser.FINN_GYLDIGE_ADRESSER_SERVICERUTINE_NAVN;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.TpsRequestContext;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.hent.TpsFinnGyldigeAdresserRequest;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.hent.attributter.finngyldigeadresser.JaEllerNei;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.hent.attributter.finngyldigeadresser.Typesok;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.response.TpsServiceRoutineResponse;
import no.nav.tps.forvalteren.service.user.UserContextHolder;

@Service
public class HentGyldigeAdresserService {
    
    @Autowired
    private UserContextHolder userContextHolder;
    
    @Autowired
    private TpsRequestSender tpsRequestSender;
    
    public TpsServiceRoutineResponse hentTilfeldigAdresse(Integer antall, String kommuneNr, String postNr) {
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
    
    public TpsServiceRoutineResponse finnGyldigAdresse(
            @ModelAttribute TpsFinnGyldigeAdresserRequest tpsServiceRoutineRequest) { //TODO requestparam klasse, tpservice-mapper og tps-servicerequest. Test denne rest-klassen (med typesok T)
        setServiceRoutineMeta(tpsServiceRoutineRequest);
        return tpsRequestSender.sendTpsRequest(tpsServiceRoutineRequest, createContext());
        
    }
    
    private void setServiceRoutineMeta(TpsFinnGyldigeAdresserRequest tpsServiceRoutineRequest) {
        tpsServiceRoutineRequest.setServiceRutinenavn(FINN_GYLDIGE_ADRESSER_SERVICERUTINE_NAVN);
        tpsServiceRoutineRequest.setAksjonsKode("A");
        tpsServiceRoutineRequest.setAksjonsKode2("0");
    }
    
    private TpsRequestContext createContext() {
        TpsRequestContext context = new TpsRequestContext();
        context.setUser(userContextHolder.getUser());
        context.setEnvironment("u6");
        return context;
    }
}
