package no.nav.tps.forvalteren.service.command.testdata.skd;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsSkdMeldingDefinition;
import no.nav.tps.forvalteren.service.command.exceptions.HttpInternalServerErrorException;
import no.nav.tps.forvalteren.service.command.exceptions.HttpUnauthorisedException;
import no.nav.tps.forvalteren.service.command.testdata.FilterPaaIdenterTilgjengeligeIMiljo;
import no.nav.tps.forvalteren.service.command.testdata.skd.utils.PersonToSkdParametersMapper;
import no.nav.tps.forvalteren.service.command.tps.SkdMeldingRequest;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.TpsRequestSender;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.GetTpsSkdmeldingService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class SkdUpdateCreatePersoner {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(TpsRequestSender.class);

    private static final String[] T_ENVIRONMENTS = {"t0","t1","t2","t3","t4","t5","t6","t7","t8","t9","t10","t11","t12"};
    private static final String[] Q_ENVIRONMENTS = {"q0","q1","q2","q3","q4","q5","q6","q8"};
    private static final String[] U_ENVIRONMENTS = {"u5", "u6"};

    @Value("${environment.class}")
    private String deployedEnvironment;

    @Autowired
    private PersonToSkdParametersMapper personToSkdParametersMapper;

    @Autowired
    private SkdOpprettSkdMeldingMedHeaderOgInnhold skdOpprettSkdMeldingMedHeaderOgInnhold;

    @Autowired
    private SkdMeldingRequest skdMeldingRequest;

    @Autowired
    private FilterPaaIdenterTilgjengeligeIMiljo filterPaaIdenterTilgjengeligeIMiljo;

    @Autowired
    private GetTpsSkdmeldingService getTpsSkdmeldingService;

    public void execute(List<Person> personer){
        List<String> identer = ekstraherIdenterFraPerson(personer);
        Set<String> identerSomIkkeFinnesiTPSiMiljoe = filterPaaIdenterTilgjengeligeIMiljo.filtrer(identer);

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
        TpsSkdMeldingDefinition skdMeldingDefinition = getTpsSkdmeldingService.execute().get(0);  // For now only 1 SkdMeldingDefinition

        for(String env : T_ENVIRONMENTS){
            sendSkdMelding(skdMelding, skdMeldingDefinition, env);
        }
        for(String env : U_ENVIRONMENTS){
            sendSkdMelding(skdMelding, skdMeldingDefinition, env);
        }

        if("q".equalsIgnoreCase(deployedEnvironment)){
            for(String env : Q_ENVIRONMENTS){
                sendSkdMelding(skdMelding, skdMeldingDefinition, env);
            }
        }
    }

    private void sendSkdMelding(String skdMelding, TpsSkdMeldingDefinition skdMeldingDefinition, String environment){
        try {
            skdMeldingRequest.execute(skdMelding, skdMeldingDefinition, environment);
        }
        catch (JMSException jmsException) {
            LOGGER.error(jmsException.getMessage(), jmsException);
            throw new HttpInternalServerErrorException(jmsException, "api/v1/testdata/saveTPS");
        } catch (HttpUnauthorisedException ex) {
            LOGGER.error(ex.getMessage(), ex);
            throw new HttpUnauthorisedException(ex, "api/v1/testdata/saveTPS" + "skdInnvandring");
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
