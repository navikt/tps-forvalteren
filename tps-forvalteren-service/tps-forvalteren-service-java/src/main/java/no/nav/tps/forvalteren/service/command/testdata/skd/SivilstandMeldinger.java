package no.nav.tps.forvalteren.service.command.testdata.skd;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.strategies.SivilstandSkdParameterStrategy;

@Service
public class SivilstandMeldinger {

    @Autowired
    private SivilstandSkdParameterStrategy sivilstandSkdParameterStrategy;

    @Autowired
    private SkdGetHeaderForSkdMelding skdGetHeaderForSkdMelding;

    public List<SkdMelding> createMeldinger(List<Person> personer, boolean addHeader) {

        List<SkdMelding> result = new ArrayList();

        personer.forEach(person -> {

            if (!person.getSivilstander().isEmpty()) {
                List<SkdMeldingTrans1> skdMeldingTrans1List = sivilstandSkdParameterStrategy.execute(person);
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