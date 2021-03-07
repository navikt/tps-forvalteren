package no.nav.tps.forvalteren.service.command.tps.skdmelding;

import static java.util.stream.Collectors.toList;
import static no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.MeldingOmAnnenAvgang.MELDING_OM_ANNEN_AVGANG;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.MeldingOmAnnenAvgang;
import no.nav.tps.forvalteren.service.command.testdata.skd.SendSkdMeldingTilGitteMiljoer;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdMeldingTrans1;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdMessageCreatorTrans1;
import no.nav.tps.forvalteren.service.command.tpsconfig.GetEnvironments;

@Slf4j
@Service
public class TpsPersonService {

    private static final String OK_MSG = "^00.*";

    @Autowired
    private SkdMessageCreatorTrans1 skdMessageCreatorTrans1;

    @Autowired
    private SendSkdMeldingTilGitteMiljoer sendSkdMeldingTilGitteMiljoer;

    @Autowired
    private MeldingOmAnnenAvgang meldingOmAnnenAvgang;

    @Autowired
    private GetEnvironments getEnvironments;

    public void sendDeletePersonMeldinger(List<String> miljoer, Set<String> identerSomSkalSlettesFraTPS) {

        Set<String> environments;
        if (miljoer.isEmpty()) {
            environments = getEnvironments.getEnvironments();
        } else {
            environments = new HashSet<>(miljoer);
        }

        List<SkdMeldingTrans1> annenAvgangMeldinger = skdMessageCreatorTrans1.execute(MELDING_OM_ANNEN_AVGANG,
                identerSomSkalSlettesFraTPS.stream().map(ident -> Person.builder().ident(ident).build()).collect(toList()), true);

        Map<String, String> status = new HashMap();
        annenAvgangMeldinger.forEach(skdMelding ->
                status.putAll(sendSkdMeldingTilGitteMiljoer.execute(skdMelding.toString(), meldingOmAnnenAvgang.resolve(), environments))
        );

        status.forEach((env, msg) -> {
            if (!msg.matches(OK_MSG) && !msg.contains("Person ikke funnet i TPS")) {
                log.error("Feilet å slette personer i TPS-miljø {} feilmelding: {}", env, msg);
            }
        });
    }
}
