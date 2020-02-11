package no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.strategies;

import java.time.LocalDateTime;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters.AnnenAvgangSkdParametere;
import no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters.SkdParametersCreator;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdMeldingTrans1;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.SkdParametersStrategy;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.ConvertDateToString;

@Service
public class AnnenAvgangSkdParameterStrategy implements SkdParametersStrategy {

    private static final String AARSAKKODE_FOR_ANNENAVGANG = "54";
    private static final String TRANSTYPE_FOR_ANNENAVGANG = "1";
    private static final String STATUSKODE_FOR_ANNENAVGANG = "6";

    @Override
    public String hentTildelingskode() {
        return "0";
    }

    @Override
    public boolean isSupported(SkdParametersCreator creator) {
        return creator instanceof AnnenAvgangSkdParametere;
    }

    @Override
    public SkdMeldingTrans1 execute(Person person) {
        SkdMeldingTrans1 skdMeldingTrans1 = SkdMeldingTrans1.builder().tildelingskode(hentTildelingskode()).build();
        addDefaultParams(skdMeldingTrans1);
        addSkdParametersExtractedFromPerson(skdMeldingTrans1, person);
        return skdMeldingTrans1;
    }

    private void addSkdParametersExtractedFromPerson(SkdMeldingTrans1 skdMeldingTrans1, Person person) {
        skdMeldingTrans1.setFodselsdato(person.getIdent().substring(0, 6));
        skdMeldingTrans1.setPersonnummer(person.getIdent().substring(6, 11));

        skdMeldingTrans1.setMaskintid(ConvertDateToString.hhMMss(LocalDateTime.now()));
        skdMeldingTrans1.setMaskindato(ConvertDateToString.yyyyMMdd(LocalDateTime.now()));

        skdMeldingTrans1.setRegDato(ConvertDateToString.yyyyMMdd(LocalDateTime.now()));
    }

    private void addDefaultParams(SkdMeldingTrans1 skdMeldingTrans1) {
        skdMeldingTrans1.setAarsakskode(AARSAKKODE_FOR_ANNENAVGANG);
        skdMeldingTrans1.setTranstype(TRANSTYPE_FOR_ANNENAVGANG);
        skdMeldingTrans1.setStatuskode(STATUSKODE_FOR_ANNENAVGANG);
    }
}
