package no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.strategies;

import java.time.LocalDateTime;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters.SkdParametersCreator;
import no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters.UtvandringSkdParametere;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdMeldingTrans1;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.SkdParametersStrategy;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.ConvertDateToString;
import org.springframework.stereotype.Service;

@Service
public class UtvandringsSkdParameterStrategy implements SkdParametersStrategy {

    private static final String AARSAKSKODE_FOR_UTVANDRING = "32";
    private static final String TRANSTYPE_FOR_UTVANDRING = "1";
    private static final String STATUSKODE_FOR_UTVANDRING = "3";

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

        addSkdParametersExtractedFromPerson(skdMeldingTrans1, person);
        addDefaultParams(skdMeldingTrans1);
        return skdMeldingTrans1;
    }

    private void addSkdParametersExtractedFromPerson(SkdMeldingTrans1 skdMeldingTrans1, Person person) {
        skdMeldingTrans1.setFodselsdato(person.getIdent().substring(0, 6));
        skdMeldingTrans1.setPersonnummer(person.getIdent().substring(6, 11));

        String yyyyMMdd = ConvertDateToString.yyyyMMdd(person.getRegdato());
        String hhMMss = ConvertDateToString.hhMMss(person.getRegdato());
        LocalDateTime meldingRegDato = person.getRegistertUtvandringsdato() != null ? person.getRegistertUtvandringsdato() : person.getRegdato();
        LocalDateTime flytteRegDato = person.getFlyttetTilLandDato() != null ? person.getFlyttetTilLandDato() : person.getRegdato();

        skdMeldingTrans1.setMaskintid(hhMMss);
        skdMeldingTrans1.setMaskindato(yyyyMMdd);
        skdMeldingTrans1.setRegDato(ConvertDateToString.yyyyMMdd(meldingRegDato));

        String registrertUtvandretDato = ConvertDateToString.yyyyMMdd(meldingRegDato);
        String flytteUtvandretDato = ConvertDateToString.yyyyMMdd(flytteRegDato);

        skdMeldingTrans1.setUtvandretTilLand(person.getUtvandretTilLand());
        skdMeldingTrans1.setTilLandRegdato(registrertUtvandretDato);
        skdMeldingTrans1.setTilLandFlyttedato(flytteUtvandretDato);
    }

    private void addDefaultParams(SkdMeldingTrans1 skdMeldingTrans1) {
        skdMeldingTrans1.setAarsakskode(AARSAKSKODE_FOR_UTVANDRING);
        skdMeldingTrans1.setTranstype(TRANSTYPE_FOR_UTVANDRING);
        skdMeldingTrans1.setStatuskode(STATUSKODE_FOR_UTVANDRING);
    }
}
