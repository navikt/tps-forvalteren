package no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.strategies;

import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters.DoedsmeldingAnnulleringSkdParamtere;
import no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters.SkdParametersCreator;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdMeldingTrans1;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.SkdParametersStrategy;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.ConvertDateToString;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.SetAdresseService;

@Service
public class DoedsmeldingAnnulleringSkdParamterStrategy implements SkdParametersStrategy {

    private static final String AARSAKS_KO_DE_FOR_DOEDSMELDING = "45";
    private static final String TRANSTYPE_FOR_DOEDSMELDING = "1";
    private static final String STATUS_KO_DE_FOR_DOEDSMELDING = "1";
    private static final String TILDELINGS_KO_DE_DOEDSMELDING_ANNULERING = "0";

    @Autowired
    private SetAdresseService setAdresse;

    @Override public String hentTildelingskode() {
        return TILDELINGS_KO_DE_DOEDSMELDING_ANNULERING;
    }

    @Override
    public boolean isSupported(SkdParametersCreator creator) {
        return creator instanceof DoedsmeldingAnnulleringSkdParamtere;
    }

    @Override
    public SkdMeldingTrans1 execute(Person person) {

        SkdMeldingTrans1 skdMeldingTrans1 = new SkdMeldingTrans1();
        skdMeldingTrans1.setTildelingskode(hentTildelingskode());

        addSkdParametersExtractedFromPerson(skdMeldingTrans1, person);
        setAdresse.execute(skdMeldingTrans1, person);
        addDefaultParam(skdMeldingTrans1);

        return skdMeldingTrans1;
    }

    private void addSkdParametersExtractedFromPerson(SkdMeldingTrans1 skdMeldingTrans1, Person person) {

        skdMeldingTrans1.setFodselsdato(person.getIdent().substring(0, 6));
        skdMeldingTrans1.setPersonnummer(person.getIdent().substring(6, 11));

        String yyyyMMdd = ConvertDateToString.yyyyMMdd(person.getRegdato());
        String hhMMss = ConvertDateToString.hhMMss(person.getRegdato());

        skdMeldingTrans1.setMaskintid(hhMMss);
        skdMeldingTrans1.setMaskindato(yyyyMMdd);

        String doedsdatoStringVersion = ConvertDateToString.yyyyMMdd(LocalDateTime.now());

        skdMeldingTrans1.setRegDato(doedsdatoStringVersion);
    }

    private void addDefaultParam(SkdMeldingTrans1 skdMeldingTrans1) {

        skdMeldingTrans1.setAarsakskode(AARSAKS_KO_DE_FOR_DOEDSMELDING);
        skdMeldingTrans1.setTranstype(TRANSTYPE_FOR_DOEDSMELDING);
        skdMeldingTrans1.setStatuskode(STATUS_KO_DE_FOR_DOEDSMELDING);
    }
}
