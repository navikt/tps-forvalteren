package no.nav.tps.forvalteren.service.command.testdata.skd;

import static no.nav.tps.forvalteren.domain.jpa.InnvandretUtvandret.InnUtvandret.UTVANDRET;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.strategies.UtvandringOgInnvandringSkdParameterStrategy;

@Service
@RequiredArgsConstructor
public class UtvandringOgInnvandring {

    private final SkdGetHeaderForSkdMelding skdGetHeaderForSkdMelding;
    private final UtvandringOgInnvandringSkdParameterStrategy utvandringOgInnvandringSkdParameterStrategy;

    public List<SkdMeldingTrans1> createMeldinger(List<Person> personerSomAlleredeEksistererITps, boolean addHeader) {
        List<SkdMeldingTrans1> skdMeldinger = new ArrayList<>();

        personerSomAlleredeEksistererITps.forEach(person -> {
            if (person.getInnvandretUtvandret().stream()
                    .filter(innvandretUtvandret -> UTVANDRET == innvandretUtvandret.getInnutvandret())
                    .findFirst().isPresent()) {
                List<SkdMeldingTrans1> skdMeldingTrans1List = utvandringOgInnvandringSkdParameterStrategy.execute(person);
                skdMeldingTrans1List.forEach(melding -> {
                    if (addHeader) {
                        melding.setHeader(skdGetHeaderForSkdMelding.execute(melding));
                    }
                });
                skdMeldinger.addAll(skdMeldingTrans1List);
            }
        });

        return skdMeldinger;
    }
}
