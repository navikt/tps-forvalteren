package no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.strategies;

import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Vergemaal;
import no.nav.tps.forvalteren.repository.jpa.PersonRepository;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdMeldingTrans1;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.ConvertDateToString;

@Service
public class VergemaalSkdParameterStrategy {

    private static final String AARSAKS_KO_DE_FOR_VERGEMAAL = "37";
    private static final String TRANSTYPE_FOR_VERGEMAAL = "1";
    private static final String TILDELINGS_KO_DE_VERGEMAAL = "0";

    @Autowired
    private PersonRepository personRepository;

    public SkdMeldingTrans1 execute(Vergemaal vergemaal) {

        SkdMeldingTrans1 skdMeldingTrans1 = new SkdMeldingTrans1();
        skdMeldingTrans1.setTildelingskode(TILDELINGS_KO_DE_VERGEMAAL);
        Person person = personRepository.findByIdent(vergemaal.getIdent());

        if (vergemaal.getVergeFnr() == null) {
            skdMeldingTrans1.setRegDato(ConvertDateToString.yyyyMMdd(getYesterday()));
        } else {
            skdMeldingTrans1.setRegDato(ConvertDateToString.yyyyMMdd(person.getRegdato()));
        }

        addSkdParameterExtractedFromPerson(skdMeldingTrans1, person);
        addSkdParameterExtractedFromVergemaal(skdMeldingTrans1, vergemaal);
        addDefaultParam(skdMeldingTrans1);

        return skdMeldingTrans1;
    }

    private void addSkdParameterExtractedFromPerson(SkdMeldingTrans1 skdMeldingTrans1, Person person) {

        skdMeldingTrans1.setFodselsdato(person.getIdent().substring(0, 6));
        skdMeldingTrans1.setPersonnummer(person.getIdent().substring(6, 11));

        skdMeldingTrans1.setMaskindato(ConvertDateToString.yyyyMMdd(person.getRegdato()));
        skdMeldingTrans1.setMaskintid(ConvertDateToString.hhMMss(person.getRegdato()));
    }

    private void addSkdParameterExtractedFromVergemaal(SkdMeldingTrans1 skdMeldingTrans1, Vergemaal vergemaal) {

        skdMeldingTrans1.setSaksid(vergemaal.getSaksid());
        skdMeldingTrans1.setEmbete(vergemaal.getEmbete());
        skdMeldingTrans1.setSakstype(vergemaal.getSakstype());
        skdMeldingTrans1.setVedtaksdato(ConvertDateToString.yyyyMMdd(vergemaal.getVedtaksdato()));
        skdMeldingTrans1.setInternVergeid(vergemaal.getInternVergeId());
        //skdMeldingTrans1.setVergeFnrDnr(vergemaal.getVergeFnr());
        skdMeldingTrans1.setVergetype(vergemaal.getVergetype());
        skdMeldingTrans1.setMandattype(vergemaal.getMandattype());
        skdMeldingTrans1.setMandatTekst(vergemaal.getMandattekst());
    }

    private void addDefaultParam(SkdMeldingTrans1 skdMeldingTrans1) {
        skdMeldingTrans1.setAarsakskode(AARSAKS_KO_DE_FOR_VERGEMAAL);
        skdMeldingTrans1.setTranstype(TRANSTYPE_FOR_VERGEMAAL);
    }

    private LocalDateTime getYesterday() {

        LocalDateTime today = LocalDateTime.now();
        return today.minusDays(1);
    }
}
