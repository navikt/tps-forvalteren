package no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.strategies;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters.BarnForeldrerelasjonSkdParametre;
import no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters.SkdParametersCreator;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdMeldingTrans1;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.SkdParametersStrategy;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.ConvertDateToString;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.ForeldreStrategy;

@Service
public class BarnForeldreRelasjonSkdParameterStrategy implements SkdParametersStrategy {

    private static final String AARSAKSKODE_FOR_FAMILIEENDRING_MELDING = "98";
    private static final String TRANSTYPE = "1";

    @Override
    public String hentTildelingskode() {
        return "0";
    }

    @Override
    public boolean isSupported(SkdParametersCreator creator) {
        return creator instanceof BarnForeldrerelasjonSkdParametre;
    }

    @Override
    public SkdMeldingTrans1 execute(Person barn) {

        SkdMeldingTrans1 skdMeldingTrans1 = new SkdMeldingTrans1();

        skdMeldingTrans1.setFodselsdato(getFoedselsdato(barn));
        skdMeldingTrans1.setPersonnummer(getPersonnummer(barn));

        String maskindato = ConvertDateToString.yyyyMMdd(LocalDateTime.now());
        String maskintid = ConvertDateToString.hhMMss(LocalDateTime.now());

        skdMeldingTrans1.setMaskindato(maskindato);
        skdMeldingTrans1.setMaskintid(maskintid);

        skdMeldingTrans1.setAarsakskode(AARSAKSKODE_FOR_FAMILIEENDRING_MELDING);
        skdMeldingTrans1.setTranstype(TRANSTYPE);

        skdMeldingTrans1.setRegDato(maskindato);

        List<Person> foreldre = ForeldreStrategy.getEntydigeForeldre(barn.getRelasjoner());
        if (!foreldre.isEmpty()) {
            setMorRelasjon(skdMeldingTrans1, foreldre.get(0));
        }
        if (foreldre.size() > 1) {
            setFarRelasjon(skdMeldingTrans1, foreldre.get(1));
        }

        return skdMeldingTrans1;
    }

    private static void setMorRelasjon(SkdMeldingTrans1 skdMeldingTrans1, Person person) {

        skdMeldingTrans1.setMorsFodselsdato(getFoedselsdato(person));
        skdMeldingTrans1.setMorsPersonnummer(getPersonnummer(person));
    }

    private static void setFarRelasjon(SkdMeldingTrans1 skdMeldingTrans1, Person person) {

        skdMeldingTrans1.setFarsFodselsdato(getFoedselsdato(person));
        skdMeldingTrans1.setFarsPersonnummer(getPersonnummer(person));
    }

    private static String getFoedselsdato(Person person) {
        return person.getIdent().substring(0, 6);
    }

    private static String getPersonnummer(Person person) {
        return person.getIdent().substring(6, 11);
    }
}
