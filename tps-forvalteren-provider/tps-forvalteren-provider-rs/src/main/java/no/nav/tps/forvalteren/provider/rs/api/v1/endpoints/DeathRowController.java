package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints;

import java.util.List;
import java.util.Set;

import ma.glasnost.orika.MapperFacade;
import no.nav.tps.forvalteren.domain.jpa.DeathRow;
import no.nav.tps.forvalteren.domain.rs.RsDeathRow;
import static no.nav.tps.forvalteren.provider.rs.config.ProviderConstants.OPERATION;
import static no.nav.tps.forvalteren.provider.rs.config.ProviderConstants.RESTSERVICE;

import javax.transaction.Transactional;

import no.nav.freg.metrics.annotations.Metrics;
import no.nav.freg.spring.boot.starters.log.exceptions.LogExceptions;
import no.nav.tps.forvalteren.service.command.dodsmeldinger.CreateDodsmelding;
import no.nav.tps.forvalteren.service.command.testdata.response.IdentMedStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Transactional
@RestController
@RequestMapping(value = "api/v1/doedsmelding")
@ConditionalOnProperty(prefix = "tps.forvalteren", name = "production-mode", havingValue = "false")
public class DeathRowController {

    private static final String REST_SERVICE_NAME = "testdata";



    @Autowired
    private CreateDodsmelding createDodsmelding;

    @LogExceptions
    @Metrics(value = "provider", tags = { @Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = "sjekkIdenter")})
    @RequestMapping(value = "/checkpersoner", method = RequestMethod.GET)
    public Set<IdentMedStatus> checkIdList(@RequestBody List<String> personIdListe) {
        return null;
    }


    @LogExceptions
    @Metrics(value = "provider", tags = { @Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = "settDodsmelding")})
    @RequestMapping(value = "/opprett", method = RequestMethod.POST)
    public void createMelding(@RequestBody RsDeathRow dMelding){
        createDodsmelding.execute(dMelding);
    }

    @LogExceptions
    @Metrics(value = "provider", tags = { @Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = "annullerDodsmelding")})
    @RequestMapping(value = "/deletemelding", method = RequestMethod.POST)
    public void deleteMelding(){

    }

    @LogExceptions
    @Metrics(value = "provider", tags = { @Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = "hentLogg")})
    @RequestMapping(value = "/meldinger", method = RequestMethod.GET)
    public List<?> getMeldingLogg(){
        return null;
    }


}
