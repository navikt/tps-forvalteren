package no.nav.tps.forvalteren.service.command.tps.servicerutiner;

import static no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.servicerutiner.S051FinnGyldigeAdresser.FINN_GYLDIGE_ADRESSER_SERVICERUTINE_NAVN;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.TpsRequestContext;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.hent.TpsFinnGyldigeAdresserRequest;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.hent.TpsFinnGyldigeAdresserResponse;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.hent.attributter.finngyldigeadresser.JaEllerNei;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.hent.attributter.finngyldigeadresser.Typesok;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.response.TpsServiceRoutineResponse;
import no.nav.tps.forvalteren.service.command.exceptions.TpsTimeoutException;
import no.nav.tps.forvalteren.service.user.UserContextHolder;

@Slf4j
@Service
public class HentGyldigeAdresserService {
    @Value("${tpsf.environment.name}")
    private String env;

    @Autowired
    private UserContextHolder userContextHolder;

    @Autowired
    private TpsRequestSender tpsRequestSender;

    @Autowired
    private ObjectMapper objectMapper;

    public TpsFinnGyldigeAdresserResponse hentTilfeldigAdresse(Integer maxantall, String kommuneNr, String postNr) {
        // TODO fix for object mapper problem
        if (maxantall < 2) {
            maxantall = 2;
        }
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

        TpsServiceRoutineResponse response = sendTpsRequest(tpsServiceRoutineRequest);

        return objectMapper.convertValue(response, TpsFinnGyldigeAdresserResponse.class);
    }

    public TpsFinnGyldigeAdresserResponse finnGyldigAdresse(@ModelAttribute TpsFinnGyldigeAdresserRequest tpsServiceRoutineRequest) {

        setServiceRoutineMeta(tpsServiceRoutineRequest);
        TpsServiceRoutineResponse response = sendTpsRequest(tpsServiceRoutineRequest);

        return objectMapper.convertValue(response, TpsFinnGyldigeAdresserResponse.class);

    }

    private TpsServiceRoutineResponse sendTpsRequest(TpsFinnGyldigeAdresserRequest tpsServiceRoutineRequest) {
        TpsServiceRoutineResponse tpsServiceRoutineResponse = tpsRequestSender.sendTpsRequest(tpsServiceRoutineRequest, createContext(), 2_000);
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
        if (env.toLowerCase().contains("u")) {
            context.setEnvironment("t0");
        } else {
            context.setEnvironment(env);
        }
        return context;
    }
}
