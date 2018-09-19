package no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.strategies;

import java.time.LocalDateTime;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters.SkdParametersCreator;
import no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters.UtvandringSkdParametere;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdMeldingTrans1;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.SkdParametersStrategy;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.ConvertDateToString;

@Service
public class UtvandringsSkdParameterStrategy implements SkdParametersStrategy {

    private static final String AARSAK_KO_DE_FOR_UTVANDRING = "32";
    private static final String TRANSTYPE_FOR_UTVANDRING = "1";
    private static final String STATUS_KO_DE_FOR_UTVANDRING = "3";

    @Override
    public String hentTildelingskode() {
        return "0";
    }

    @Override
    public boolean isSupported(SkdParametersCreator creator) {
        return creator instanceof UtvandringSkdParametere;
    }

    @Override
    public SkdMeldingTrans1 execute(Person person) {
        SkdMeldingTrans1 skdMeldingTrans1 = new SkdMeldingTrans1();
        skdMeldingTrans1.setTildelingskode(hentTildelingskode());
        addSkdParametersExtractedFromPerson(skdMeldingTrans1, person);
        addDefaultParams(skdMeldingTrans1);
        return skdMeldingTrans1;
    }

    private void addSkdParametersExtractedFromPerson(SkdMeldingTrans1 skdMeldingTrans1, Person person) {
        skdMeldingTrans1.setFodselsdato(person.getIdent().substring(0, 6));
        skdMeldingTrans1.setPersonnummer(person.getIdent().substring(6, 11));

        skdMeldingTrans1.setMaskintid(ConvertDateToString.hhMMss(person.getRegdato()));
        skdMeldingTrans1.setMaskindato(ConvertDateToString.yyyyMMdd(person.getRegdato()));

        LocalDateTime utvandretRegdato = person.getUtvandretTilLandRegdato() != null ? person.getUtvandretTilLandRegdato() : person.getRegdato();
        LocalDateTime utvandretFlyttedato = person.getUtvandretTilLandFlyttedato() != null ? person.getUtvandretTilLandFlyttedato() : person.getRegdato();

        skdMeldingTrans1.setRegDato(ConvertDateToString.yyyyMMdd(utvandretRegdato));

        skdMeldingTrans1.setUtvandretTilLand(person.getUtvandretTilLand());
        skdMeldingTrans1.setTilLandRegdato(ConvertDateToString.yyyyMMdd(utvandretRegdato));
        skdMeldingTrans1.setTilLandFlyttedato(ConvertDateToString.yyyyMMdd(utvandretFlyttedato));
    }

    private void addDefaultParams(SkdMeldingTrans1 skdMeldingTrans1) {
        skdMeldingTrans1.setAarsakskode(AARSAK_KO_DE_FOR_UTVANDRING);
        skdMeldingTrans1.setTranstype(TRANSTYPE_FOR_UTVANDRING);
        skdMeldingTrans1.setStatuskode(STATUS_KO_DE_FOR_UTVANDRING);
    }
}
