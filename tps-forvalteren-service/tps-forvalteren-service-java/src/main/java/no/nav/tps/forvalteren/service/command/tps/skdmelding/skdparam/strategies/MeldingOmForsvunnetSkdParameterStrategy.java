package no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.strategies;

import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters.MeldingOmForsvunnetSkdParametere;
import no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters.SkdParametersCreator;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdMeldingTrans1;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.SkdParametersStrategy;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.ConvertDateToString;

@Service
public class MeldingOmForsvunnetSkdParameterStrategy implements SkdParametersStrategy {

    private static final String AARSAKS_KO_DE_FOR_FORSVINNING_MELDING = "82";
    private static final String TILDELING_KO_DE_PA_FORSVUNNET_MELDING = "0";

    @Override
    public SkdMeldingTrans1 execute(Person barn) {

        SkdMeldingTrans1 skdMeldingTrans1 = new SkdMeldingTrans1();
        skdMeldingTrans1.setTildelingskode(hentTildelingskode());

        addSkdParametersExtractedFromPerson(skdMeldingTrans1, barn);
        addDefaultParam(skdMeldingTrans1);

        return skdMeldingTrans1;
    }

    @Override
    public String hentTildelingskode() {
        return TILDELING_KO_DE_PA_FORSVUNNET_MELDING;
    }

    @Override
    public boolean isSupported(SkdParametersCreator creator) {
        return creator instanceof MeldingOmForsvunnetSkdParametere;
    }

    private void addSkdParametersExtractedFromPerson(SkdMeldingTrans1 skdMeldingTrans1, Person forsvunnetPerson) {

        skdMeldingTrans1.setFodselsdato(getDato(forsvunnetPerson));
        skdMeldingTrans1.setPersonnummer(getPersonnr(forsvunnetPerson));

        skdMeldingTrans1.setMaskindato(ConvertDateToString.yyyyMMdd(forsvunnetPerson.getRegdato()));
        skdMeldingTrans1.setMaskintid(ConvertDateToString.hhMMss(forsvunnetPerson.getRegdato()));

        skdMeldingTrans1.setRegDato(ConvertDateToString.yyyyMMdd(forsvunnetPerson.getForsvunnetDato()));
    }

    private static void addDefaultParam(SkdMeldingTrans1 skdMeldingTrans1) {
        skdMeldingTrans1.setTranstype("1");
        skdMeldingTrans1.setAarsakskode(AARSAKS_KO_DE_FOR_FORSVINNING_MELDING);
        skdMeldingTrans1.setStatuskode("4");
    }

    private static String getPersonnr(Person person) {
        return person.getIdent().substring(6, 11);
    }

    private static String getDato(Person person) {
        return person.getIdent().substring(0, 6);
    }
}
