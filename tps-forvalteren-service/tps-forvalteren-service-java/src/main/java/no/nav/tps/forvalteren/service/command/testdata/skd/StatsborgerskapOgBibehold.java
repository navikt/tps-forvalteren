package no.nav.tps.forvalteren.service.command.testdata.skd;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.strategies.StatsborgerskapOgBibeholdSkdParameterStrategy;

@Service
@RequiredArgsConstructor
public class StatsborgerskapOgBibehold {

    private final StatsborgerskapOgBibeholdSkdParameterStrategy statsborgerskapOgBibeholdSkdParameterStrategy;
    private final SkdGetHeaderForSkdMelding skdGetHeaderForSkdMelding;

    public List<SkdMelding> createMeldinger(List<Person> personerSomAlleredeEksistererITps, boolean addHeader) {

        List<SkdMelding> result = new ArrayList();

        personerSomAlleredeEksistererITps.forEach(person -> {

            if (person.getStatsborgerskap().size() > 1) {
                List<SkdMeldingTrans1> skdMeldingTrans1List = statsborgerskapOgBibeholdSkdParameterStrategy.execute(person);
                skdMeldingTrans1List.forEach(melding -> {
                    if (addHeader) {
                        melding.setHeader(skdGetHeaderForSkdMelding.execute(melding));
                    }
                });
                result.addAll(skdMeldingTrans1List);
            }
        });

        return result;
    }
}
