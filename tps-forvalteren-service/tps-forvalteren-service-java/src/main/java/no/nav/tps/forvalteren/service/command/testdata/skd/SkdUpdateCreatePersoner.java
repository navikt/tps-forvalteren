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

    @Value("${environment.class}")
    private String deployedEnvironment;

    @Autowired
    private SendSkdMeldingTilGitteMiljoer sendSkdMeldingTilGitteMiljoer;

    @Autowired
    private PersonToSkdParametersMapper personToSkdParametersMapper;

    @Autowired
    private SkdOpprettSkdMeldingMedHeaderOgInnhold skdOpprettSkdMeldingMedHeaderOgInnhold;

    @Autowired
    private FiltrerPaaIdenterTilgjengeligeIMiljo filtrerPaaIdenterTilgjengeligeIMiljo;

    @Autowired
    private GetEnvironments getEnvironmentsCommand;

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
            sendSkdMeldingTilGitteMiljoer.execute(skdMelding, getEnvironmentsCommand.getEnvironmentsFromVera("tpsws"));
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
