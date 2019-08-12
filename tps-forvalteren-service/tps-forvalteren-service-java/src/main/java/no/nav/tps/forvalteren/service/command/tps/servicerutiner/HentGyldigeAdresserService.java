package no.nav.tps.forvalteren.service.command.tps.servicerutiner;

import static no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.servicerutiner.S051FinnGyldigeAdresser.FINN_GYLDIGE_ADRESSER_SERVICERUTINE_NAVN;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;

import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.TpsRequestContext;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.hent.TpsFinnGyldigeAdresserRequest;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.hent.attributter.finngyldigeadresser.JaEllerNei;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.hent.attributter.finngyldigeadresser.Typesok;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.response.TpsServiceRoutineResponse;
import no.nav.tps.forvalteren.service.command.exceptions.TpsTimeoutException;
import no.nav.tps.forvalteren.service.user.UserContextHolder;

@Service
public class HentGyldigeAdresserService {
    @Value("${fasit.environment.name}")
    private String env;
    
    @Autowired
    private UserContextHolder userContextHolder;
    
    @Autowired
    private TpsRequestSender tpsRequestSender;
    
    public TpsServiceRoutineResponse hentTilfeldigAdresse(Integer maxantall, String kommuneNr, String postNr) {
        TpsFinnGyldigeAdresserRequest tpsServiceRoutineRequest = TpsFinnGyldigeAdresserRequest.builder()
                .typesok(Typesok.T)
                .maxRetur(maxantall)
                .kommuneNrsok(kommuneNr)
                .postNrsok(postNr)
                .alltidRetur(JaEllerNei.J)
                .alleSkrivevarianter(JaEllerNei.J)
                .visPostnr(JaEllerNei.J)
                .build();
        setServiceRoutineMeta(tpsServiceRoutineRequest);
        
        return sendTpsRequest(tpsServiceRoutineRequest);
    }
    
    public TpsServiceRoutineResponse finnGyldigAdresse(@ModelAttribute TpsFinnGyldigeAdresserRequest tpsServiceRoutineRequest) {
        
        setServiceRoutineMeta(tpsServiceRoutineRequest);
        return sendTpsRequest(tpsServiceRoutineRequest);
    }
    
    private TpsServiceRoutineResponse sendTpsRequest(TpsFinnGyldigeAdresserRequest tpsServiceRoutineRequest) {
        TpsServiceRoutineResponse tpsServiceRoutineResponse = tpsRequestSender.sendTpsRequest(tpsServiceRoutineRequest, createContext(), 40_000);
        if (tpsServiceRoutineResponse.getXml().isEmpty()) {
            throw new TpsTimeoutException("Time out: Responsmeldingen fra TPS S051 var tom. Søket i TPS tok for lang tid.");
        }
        return tpsServiceRoutineResponse;
    }
    
    private void setServiceRoutineMeta(TpsFinnGyldigeAdresserRequest tpsServiceRoutineRequest) {
        tpsServiceRoutineRequest.setServiceRutinenavn(FINN_GYLDIGE_ADRESSER_SERVICERUTINE_NAVN);
        tpsServiceRoutineRequest.setAksjonsKode("A");
        tpsServiceRoutineRequest.setAksjonsKode2("0");
    }
    
    private TpsRequestContext createContext() {
        TpsRequestContext context = new TpsRequestContext();
        context.setUser(userContextHolder.getUser());
        if(env.toLowerCase().contains("u")){
            context.setEnvironment("t0");
        } else {
            context.setEnvironment(env);
        }
        return context;
    }
}
