package no.nav.tps.forvalteren.service.command.testdata.skd;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsSkdRequestMeldingDefinition;
import no.nav.tps.forvalteren.service.command.testdata.FiltrerPaaIdenterTilgjengeligeIMiljo;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.GetSkdMeldingByName;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.SkdParametersCreatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class SkdCreatePersoner {

    @Value("${environment.class}")
    private String deployedEnvironment;

    @Autowired
    private SendSkdMeldingTilGitteMiljoer sendSkdMeldingTilGitteMiljoer;

    @Autowired
    private SkdParametersCreatorService skdParametersCreatorService;

    @Autowired
    private SkdOpprettSkdMeldingMedHeaderOgInnhold skdOpprettSkdMeldingMedHeaderOgInnhold;

    @Autowired
    private FiltrerPaaIdenterTilgjengeligeIMiljo filtrerPaaIdenterTilgjengeligeIMiljo;

    @Autowired
    private GetSkdMeldingByName getSkdMeldingByName;

    public void execute(List<Person> personer, List<String> environments){
        List<String> identer = ekstraherIdenterFraPersoner(personer);
        Set<String> identerSomIkkeFinnesiTPSiMiljoe = filtrerPaaIdenterTilgjengeligeIMiljo.filtrer(identer, new HashSet<>(environments));
        TpsSkdRequestMeldingDefinition skdRequestMeldingDefinition = getSkdMeldingByName.execute("Innvandring").get();

        for(Person person : personer){

            if(identerSomIkkeFinnesiTPSiMiljoe.contains(person.getIdent())){
                Map<String,String> skdParametere = skdParametersCreatorService.execute(skdRequestMeldingDefinition, person);
                String skdMelding = skdOpprettSkdMeldingMedHeaderOgInnhold.execute(skdParametere);
                sendSkdMeldingTilGitteMiljoer.execute(skdMelding, skdRequestMeldingDefinition, new HashSet<>(environments));
            }

        }
    }

    private List<String> ekstraherIdenterFraPersoner(List<Person> personer) {
        List<String> identer = new ArrayList<>();
        for(Person person : personer){
            identer.add(person.getIdent());
        }
        return identer;
    }
}
