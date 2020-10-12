package no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.strategies;

import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters.SkdParametersCreator;
import no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters.VergemaalSkdParametere;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdMeldingTrans1;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.SkdParametersStrategy;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.ConvertDateToString;

@Service
public class VergemaalSkdParameterStrategy implements SkdParametersStrategy {

    private static final String AARSAKS_KO_DE_FOR_VERGEMAAL = "37";
    private static final String TRANSTYPE_FOR_VERGEMAAL = "1";
    private static final String TILDELINGS_KO_DE_VERGEMAAL = "0";
    private static final String ALMINNELIG = "ALM";

    @Override public String hentTildelingskode() {
        return TILDELINGS_KO_DE_VERGEMAAL;
    }

    @Override public boolean isSupported(SkdParametersCreator creator) {
        return creator instanceof VergemaalSkdParametere;
    }

    public SkdMeldingTrans1 execute(Person person) {

        SkdMeldingTrans1 skdMeldingTrans1 = new SkdMeldingTrans1();
        skdMeldingTrans1.setTildelingskode(hentTildelingskode());

        skdMeldingTrans1.setRegDato(ConvertDateToString.yyyyMMdd(person.getRegdato()));

        addSkdParameterExtractedFromPerson(skdMeldingTrans1, person);
        addSkdParameterExtractedFromVergemaal(skdMeldingTrans1, person);
        addDefaultParam(skdMeldingTrans1);

        return skdMeldingTrans1;
    }

    private void addSkdParameterExtractedFromPerson(SkdMeldingTrans1 skdMeldingTrans1, Person person) {

        skdMeldingTrans1.setFodselsdato(person.getIdent().substring(0, 6));
        skdMeldingTrans1.setPersonnummer(person.getIdent().substring(6, 11));

        skdMeldingTrans1.setMaskindato(ConvertDateToString.yyyyMMdd(person.getRegdato()));
        skdMeldingTrans1.setMaskintid(ConvertDateToString.hhMMss(person.getRegdato()));
    }

    private void addSkdParameterExtractedFromVergemaal(SkdMeldingTrans1 skdMeldingTrans1, Person person) {

        skdMeldingTrans1.setSaksid(person.getIdent().substring(4));
        skdMeldingTrans1.setEmbete(person.getVergemaal().get(0).getEmbete());
        skdMeldingTrans1.setSakstype(person.getVergemaal().get(0).getSakType());
        skdMeldingTrans1.setVedtaksdato(ConvertDateToString.yyyyMMdd(person.getVergemaal().get(0).getVedtakDato()));
        skdMeldingTrans1.setInternVergeid(person.getVergemaal().get(0).getVerge().getIdent().substring(4));
        skdMeldingTrans1.setVergeFnrDnr(person.getVergemaal().get(0).getVerge().getIdent());
        skdMeldingTrans1.setVergetype(ALMINNELIG);
        skdMeldingTrans1.setMandattype(person.getVergemaal().get(0).getMandatType());
    }

    private void addDefaultParam(SkdMeldingTrans1 skdMeldingTrans1) {
        skdMeldingTrans1.setAarsakskode(AARSAKS_KO_DE_FOR_VERGEMAAL);
        skdMeldingTrans1.setTranstype(TRANSTYPE_FOR_VERGEMAAL);
    }
}
