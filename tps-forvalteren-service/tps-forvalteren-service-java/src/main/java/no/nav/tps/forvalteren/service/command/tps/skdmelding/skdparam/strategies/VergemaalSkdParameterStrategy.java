package no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.strategies;

import java.time.LocalDateTime;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Vergemaal;
import no.nav.tps.forvalteren.repository.jpa.PersonRepository;
import no.nav.tps.forvalteren.repository.jpa.VergemaalRepository;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdMeldingTrans1;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.ConvertDateToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VergemaalSkdParameterStrategy {

    private static final String AARSAKSKODE_FOR_VERGEMAAL = "37";
    private static final String TRANSTYPE_FOR_VERGEMAAL = "1";
    private static final String TILDELINGSKODE_VERGEMAAL = "0";

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private VergemaalRepository vergemaalRepository;

    public SkdMeldingTrans1 execute(Vergemaal vergemaal) {

        SkdMeldingTrans1 skdMeldingTrans1 = new SkdMeldingTrans1();
        skdMeldingTrans1.setTildelingskode(TILDELINGSKODE_VERGEMAAL);
        Person person = personRepository.findByIdent(vergemaal.getIdent());

        //
        if (vergemaal.getVergeFnr() == null) {
            skdMeldingTrans1.setRegDato(ConvertDateToString.yyyyMMdd(getYesterday()));
        } else {
            skdMeldingTrans1.setRegDato(ConvertDateToString.yyyyMMdd(person.getRegdato()));
        }

        addSkdParameterExtractedFromPerson(skdMeldingTrans1, person);
        addSkdParameterExtractedFromVergemaal(skdMeldingTrans1, vergemaal);
        addDefaultParam(skdMeldingTrans1);
        setVergemaalAsSendt(vergemaal);

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

        String yyyyMMdd = "";

        if (vergemaal.getVedtaksdato() != null) {
            yyyyMMdd = ConvertDateToString.yyyyMMdd(vergemaal.getVedtaksdato());
        }

        skdMeldingTrans1.setSaksid(vergemaal.getSaksid());
        skdMeldingTrans1.setEmbete(vergemaal.getEmbete());
        skdMeldingTrans1.setSakstype(vergemaal.getSakstype());
        skdMeldingTrans1.setVedtaksdato(yyyyMMdd);
        skdMeldingTrans1.setInternVergeid(vergemaal.getInternVergeId());

        skdMeldingTrans1.setVergeFnrDnr(vergemaal.getVergeFnr());
        skdMeldingTrans1.setVergetype(vergemaal.getVergetype());
        skdMeldingTrans1.setMandattype(vergemaal.getMandattype());
        skdMeldingTrans1.setMandatTekst(vergemaal.getMandattekst());

    }

    private void addDefaultParam(SkdMeldingTrans1 skdMeldingTrans1) {
        skdMeldingTrans1.setAarsakskode(AARSAKSKODE_FOR_VERGEMAAL);
        skdMeldingTrans1.setTranstype(TRANSTYPE_FOR_VERGEMAAL);
    }

    private void setVergemaalAsSendt(Vergemaal vergemaal) {
        vergemaal.setVergemaalSendt("S");
        vergemaalRepository.save(vergemaal);
    }

    private LocalDateTime getYesterday() {

        LocalDateTime today = LocalDateTime.now();
        return today.minusDays(1);

    }
}
