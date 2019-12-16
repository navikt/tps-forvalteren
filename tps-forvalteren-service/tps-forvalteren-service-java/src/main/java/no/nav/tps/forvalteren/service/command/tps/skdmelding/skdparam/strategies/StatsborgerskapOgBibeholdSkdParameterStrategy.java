package no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.strategies;

import static java.util.Collections.emptyList;
import static no.nav.tps.forvalteren.domain.service.tps.config.SkdConstants.TRANSTYPE_1;
import static no.nav.tps.forvalteren.service.command.testdata.utils.HentDatoFraIdentService.enforceValidTpsDate;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Statsborgerskap;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdMeldingTrans1;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.ConvertDateToString;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.LandkodeEncoder;

@Service
@RequiredArgsConstructor
public class StatsborgerskapOgBibeholdSkdParameterStrategy {

    private static final String AARSAK_KO_DE_FOR_STATSBORGERSKAP = "35";

    private final LandkodeEncoder landkodeEncoder;

    public List<SkdMeldingTrans1> execute(Person person) {

        if (person.getStatsborgerskap().size() > 1) {
            person.getStatsborgerskap().sort(Comparator.comparing(Statsborgerskap::getId));
            List<SkdMeldingTrans1> messages = new ArrayList(person.getStatsborgerskap().size() - 1);

            for (int i = 1; i < person.getStatsborgerskap().size(); i++) {
                messages.add(buildSkdTrans1Statsborger(person, person.getStatsborgerskap().get(i)));
            }
            return messages;

        } else {
            return emptyList();
        }
    }

    private SkdMeldingTrans1 buildSkdTrans1Statsborger(Person person, Statsborgerskap statsborgerskap) {

        String yyyyMMdd = ConvertDateToString.yyyyMMdd(enforceValidTpsDate(person.getRegdato()));
        String hhMMss = ConvertDateToString.hhMMss(person.getRegdato());

        return SkdMeldingTrans1.builder()
                .aarsakskode(AARSAK_KO_DE_FOR_STATSBORGERSKAP)
                .transtype(TRANSTYPE_1)
                .fodselsdato(person.getIdent().substring(0, 6))
                .personnummer(person.getIdent().substring(6, 11))
                .maskindato(yyyyMMdd)
                .maskintid(hhMMss)
                .regDato(ConvertDateToString.yyyyMMdd(statsborgerskap.getStatsborgerskapRegdato()))
                .statsborgerskap(landkodeEncoder.encode(statsborgerskap.getStatsborgerskap()))
                .regdatoStatsb(ConvertDateToString.yyyyMMdd(enforceValidTpsDate(statsborgerskap.getStatsborgerskapRegdato())))
                .build();
    }
}
