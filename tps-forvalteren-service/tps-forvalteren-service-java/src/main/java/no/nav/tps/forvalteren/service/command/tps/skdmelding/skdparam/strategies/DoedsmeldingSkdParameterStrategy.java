package no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.strategies;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.rs.skd.IdentType;
import no.nav.tps.forvalteren.domain.service.DiskresjonskoderType;
import no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters.DoedsmeldingSkdParametere;
import no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters.SkdParametersCreator;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdMeldingTrans1;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.SkdParametersStrategy;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.ConvertDateToString;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.SetAdresseService;

@Service
@RequiredArgsConstructor
public class DoedsmeldingSkdParameterStrategy implements SkdParametersStrategy {

    private static final String AARSAKSKODE_FOR_DOEDSMELDING = "43";
    private static final String TRANSTYPE_FOR_DOEDSMELDING = "1";
    private static final String STATUSKODE_FNR = "5";
    private static final String STATUSKODE_DNR = "2";
    private static final String TILDELINGSKODE_DOEDSMELDING = "0";

    private final SetAdresseService setAdresseService;

    @Override
    public String hentTildelingskode() {
        return TILDELINGSKODE_DOEDSMELDING;
    }

    @Override
    public boolean isSupported(SkdParametersCreator creator) {
        return creator instanceof DoedsmeldingSkdParametere;
    }

    @Override
    public SkdMeldingTrans1 execute(Person person) {
        SkdMeldingTrans1 skdMeldingTrans1 = new SkdMeldingTrans1();
        skdMeldingTrans1.setTildelingskode(hentTildelingskode());

        addSkdParametersExtractedFromPerson(skdMeldingTrans1, person);
        setAdresseService.execute(skdMeldingTrans1, person);

        return skdMeldingTrans1;
    }

    private void addSkdParametersExtractedFromPerson(SkdMeldingTrans1 skdMeldingTrans1, Person person) {
        skdMeldingTrans1.setFodselsdato(person.getIdent().substring(0, 6));
        skdMeldingTrans1.setPersonnummer(person.getIdent().substring(6, 11));

        skdMeldingTrans1.setSpesRegType(person.getSpesreg() != null ? DiskresjonskoderType.valueOf(person.getSpesreg()).getKodeverdi() : null);

        skdMeldingTrans1.setMaskintid(ConvertDateToString.hhMMss(person.getRegdato()));
        skdMeldingTrans1.setMaskindato(ConvertDateToString.yyyyMMdd(person.getRegdato()));

        String doedsdatoStringVersion = ConvertDateToString.yyyyMMdd(person.getDoedsdato());

        // The specification for doedsmelding says reg-dato should be doedsdato
        skdMeldingTrans1.setRegDato(doedsdatoStringVersion);
        skdMeldingTrans1.setDatoDoed(doedsdatoStringVersion);

        skdMeldingTrans1.setAarsakskode(AARSAKSKODE_FOR_DOEDSMELDING);
        skdMeldingTrans1.setStatuskode(IdentType.FNR.name().equals(person.getIdenttype()) ? STATUSKODE_FNR : STATUSKODE_DNR);
        skdMeldingTrans1.setTranstype(TRANSTYPE_FOR_DOEDSMELDING);
    }
}
