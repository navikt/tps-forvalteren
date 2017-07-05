package no.nav.tps.forvalteren.service.command.testdata.skd;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsSkdRequestMeldingDefinition;
import no.nav.tps.forvalteren.service.command.FilterEnvironmentsOnDeployedEnvironment;
import no.nav.tps.forvalteren.service.command.exceptions.HttpForbiddenException;
import no.nav.tps.forvalteren.service.command.exceptions.HttpInternalServerErrorException;
import no.nav.tps.forvalteren.service.command.testdata.FiltrerPaaIdenterTilgjengeligeIMiljo;
import no.nav.tps.forvalteren.service.command.testdata.skd.utils.PersonToSkdParametersMapper;
import no.nav.tps.forvalteren.service.command.tps.SkdMeldingRequest;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.GetTpsSkdmeldingService;
import no.nav.tps.forvalteren.service.command.vera.GetEnvironments;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class SkdUpdateCreatePersoner {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(SkdUpdateCreatePersoner.class);

    @Value("${environment.class}")
    private String deployedEnvironment;

    @Autowired
    private PersonToSkdParametersMapper personToSkdParametersMapper;

    @Autowired
    private SkdOpprettSkdMeldingMedHeaderOgInnhold skdOpprettSkdMeldingMedHeaderOgInnhold;

    @Autowired
    private SkdMeldingRequest skdMeldingRequest;

    @Autowired
    private FiltrerPaaIdenterTilgjengeligeIMiljo filtrerPaaIdenterTilgjengeligeIMiljo;

    @Autowired
    private GetTpsSkdmeldingService getTpsSkdmeldingService;

    @Autowired
    private GetEnvironments getEnvironmentsCommand;

    @Autowired
    private FilterEnvironmentsOnDeployedEnvironment filterEnvironmentsOnDeployedEnvironment;

    @PreAuthorize("hasRole('ROLE_TPSF_SKRIV')")
    public void execute(List<Person> personer){
        List<String> identer = ekstraherIdenterFraPerson(personer);
        Set<String> identerSomIkkeFinnesiTPSiMiljoe = filtrerPaaIdenterTilgjengeligeIMiljo.filtrer(identer);

        for(Person person : personer){
            Map<String,String> skdParametere;

            if(identerSomIkkeFinnesiTPSiMiljoe.contains(person.getIdent())){
                skdParametere = personToSkdParametersMapper.create(person);
            } else {
                skdParametere = personToSkdParametersMapper.update(person);
            }

            String skdMelding = skdOpprettSkdMeldingMedHeaderOgInnhold.execute(skdParametere);
            sendSkdMeldingerTilAlleMiljoer(skdMelding);
        }
    }

    private void sendSkdMeldingerTilAlleMiljoer(String skdMelding){
        TpsSkdRequestMeldingDefinition skdMeldingDefinition = getTpsSkdmeldingService.execute().get(0);  // For now only 1 SkdMeldingDefinition

        Set<String> environments = getEnvironmentsCommand.getEnvironmentsFromVera("tpsws");
        Set<String> envToCheck = filterEnvironmentsOnDeployedEnvironment.execute(environments);

        for(String env : envToCheck){
            sendSkdMelding(skdMelding, skdMeldingDefinition, env);
        }
    }

    private void sendSkdMelding(String skdMelding, TpsSkdRequestMeldingDefinition skdMeldingDefinition, String environment){
        try {
            skdMeldingRequest.execute(skdMelding, skdMeldingDefinition, environment);
        } catch (JMSException jmsException) {
            LOGGER.error(jmsException.getMessage(), jmsException);
            throw new HttpInternalServerErrorException(jmsException, "api/v1/testdata/saveTPS");
        } catch (HttpForbiddenException ex) {
            LOGGER.error(ex.getMessage(), ex);
            throw new HttpForbiddenException(ex, "api/v1/testdata/saveTPS" + "skdInnvandring");
        }
    }

    private List<String> ekstraherIdenterFraPerson(List<Person> personer) {
        List<String> identer = new ArrayList<>();
        for(Person person : personer){
            identer.add(person.getIdent());
        }
        return identer;
    }
}
