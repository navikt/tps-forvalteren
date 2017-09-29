package no.nav.tps.forvalteren.service.command.testdata.skd.impl;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Relasjon;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsSkdRequestMeldingDefinition;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.Familieendring;
import no.nav.tps.forvalteren.service.command.testdata.FinnBarnTilForeldreFraRelasjoner;
import no.nav.tps.forvalteren.service.command.testdata.skd.SendSkdMeldingTilGitteMiljoer;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdCreateFamilierelasjoner;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdFelterContainerTrans2;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdOpprettSkdMeldingMedHeaderOgInnhold;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.strategies.BarnetranseSkdParameterStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Service
public class DefaultSkdCreateFamilierelasjoner implements SkdCreateFamilierelasjoner {

    @Value("${environment.class}")
    private String deployedEnvironment;

    @Autowired
    private SendSkdMeldingTilGitteMiljoer sendSkdMeldingTilGitteMiljoer;

    @Autowired
    private BarnetranseSkdParameterStrategy barnetranseSkdParameterStrategy;

    @Autowired
    private SkdOpprettSkdMeldingMedHeaderOgInnhold skdOpprettSkdMeldingMedHeaderOgInnhold;

    @Autowired
    private SkdFelterContainerTrans2 skdFelterContainerTrans2;

    @Autowired
    private Familieendring familieendring;

    @Autowired
    private FinnBarnTilForeldreFraRelasjoner finnBarnTilForeldreFraRelasjoner;

    @Override
    public void execute(Person foreldre, List<Relasjon> foreldreBarnRelasjoner, List<String> environments) {
        List<Person> barn = finnBarnTilForeldreFraRelasjoner.execute(foreldreBarnRelasjoner);

        TpsSkdRequestMeldingDefinition skdRequestMeldingDefinition = familieendring.resolve();

        Map<String, String> skdParametere = barnetranseSkdParameterStrategy.execute(foreldre, barn);

        String skdMelding = skdOpprettSkdMeldingMedHeaderOgInnhold.execute(skdParametere, skdFelterContainerTrans2);

        sendSkdMeldingTilGitteMiljoer.execute(skdMelding, skdRequestMeldingDefinition, new HashSet<>(environments));
    }

}
