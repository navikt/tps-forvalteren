package no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.strategies;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Vergemaal;
import no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters.SkdParametersCreator;
import no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters.VergemaalSkdParametere;
import no.nav.tps.forvalteren.repository.jpa.VergemaalRepository;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdMeldingTrans1;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.SkdParametersStrategy;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.ConvertDateToString;
import org.springframework.beans.factory.annotation.Autowired;

public class VergemaalSkdParameterStrategy implements SkdParametersStrategy {

    private static final String AARSAKSKODE_FOR_VERGEMAAL = "37";
    private static final String TRANSTYPE_FOR_VERGEMAAL = "1";
    private static final String TILDELINGSKODE_VERGEMAAL = "0";
    Vergemaal vergemaal = null;

    @Autowired
    private VergemaalRepository vergemaalRepository;

    @Override
    public String hentTildelingskode() {
        return TILDELINGSKODE_VERGEMAAL;
    }

    @Override
    public boolean isSupported(SkdParametersCreator creator) {
        return creator instanceof VergemaalSkdParametere;
    }

    @Override
    public SkdMeldingTrans1 execute(Person person) {

        SkdMeldingTrans1 skdMeldingTrans1 = new SkdMeldingTrans1();
        skdMeldingTrans1.setTildelingskode(hentTildelingskode());

        addSkdParameterExtractedFromPerson(skdMeldingTrans1, person);

        Vergemaal vergemaal = vergemaalRepository.findById(person.getId());
        addSkdParameterExtractedFromVergemaal(skdMeldingTrans1, vergemaal);

        addDefaultParam(skdMeldingTrans1);

        return skdMeldingTrans1;

    }

    private void addSkdParameterExtractedFromPerson(SkdMeldingTrans1 skdMeldingTrans1, Person person) {
        skdMeldingTrans1.setFodselsdato(person.getIdent().substring(0, 6));
        skdMeldingTrans1.setPersonnummer(person.getIdent().substring(6, 11));

        String yyyyMMdd = ConvertDateToString.yyyyMMdd(person.getRegdato());
        String hhMMss = ConvertDateToString.hhMMss(person.getRegdato());

        skdMeldingTrans1.setMaskintid(hhMMss);
        skdMeldingTrans1.setMaskindato(yyyyMMdd);

    }

    private void addSkdParameterExtractedFromVergemaal(SkdMeldingTrans1 skdMeldingTrans1, Vergemaal vergemaal) {

        String yyyyMMdd = ConvertDateToString.yyyyMMdd(vergemaal.getVedtaksdato());

        skdMeldingTrans1.setSaksid(vergemaal.getSaksid());
        skdMeldingTrans1.setEmbete(vergemaal.getEmbete());
        skdMeldingTrans1.setSakstype(vergemaal.getSakstype());
        skdMeldingTrans1.setVedtaksdato(yyyyMMdd);
        skdMeldingTrans1.setInternVergeId(vergemaal.getInternVergeId());

        skdMeldingTrans1.setVergeFnrDnr(vergemaal.getVergeFnr());
        skdMeldingTrans1.setVergetype(vergemaal.getVergetype());
        skdMeldingTrans1.setMandattype(vergemaal.getMandattype());
        skdMeldingTrans1.setMandatTekst(vergemaal.getMandattekst());

    }

    private void addDefaultParam(SkdMeldingTrans1 skdMeldingTrans1) {
        skdMeldingTrans1.setAarsakskode(AARSAKSKODE_FOR_VERGEMAAL);
        skdMeldingTrans1.setTranstype(TRANSTYPE_FOR_VERGEMAAL);
    }
}
