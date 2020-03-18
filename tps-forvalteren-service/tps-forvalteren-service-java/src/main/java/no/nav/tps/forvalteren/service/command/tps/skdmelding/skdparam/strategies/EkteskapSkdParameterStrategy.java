package no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.strategies;

import static java.util.Objects.isNull;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Relasjon;
import no.nav.tps.forvalteren.domain.service.RelasjonType;
import no.nav.tps.forvalteren.domain.jpa.Sivilstatus;
import no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters.EkteskapSkdParametere;
import no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters.SkdParametersCreator;
import no.nav.tps.forvalteren.repository.jpa.PersonRepository;
import no.nav.tps.forvalteren.repository.jpa.RelasjonRepository;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdMeldingTrans1;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.SkdParametersStrategy;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.ConvertDateToString;

@Service
public class EkteskapSkdParameterStrategy implements SkdParametersStrategy {

    private static final String AARSAKS_KO_DE_FOR_VIGSEL = "11";
    private static final String AARSAKS_KO_DE_FOR_INNGAAELSE_PARTNERSKAP = "61";
    private static final String TILDELINGS_KO_DE_PARTNERSKAP = "0";

    @Autowired
    private RelasjonRepository relasjonRepository;

    @Autowired
    private PersonRepository personRepository;

    @Override
    public String hentTildelingskode() {
        return TILDELINGS_KO_DE_PARTNERSKAP;
    }

    @Override
    public boolean isSupported(SkdParametersCreator creator) {
        return creator instanceof EkteskapSkdParametere;
    }

    @Override
    public SkdMeldingTrans1 execute(Person person) {
        SkdMeldingTrans1 skdMeldingTrans1 = new SkdMeldingTrans1();
        skdMeldingTrans1.setTildelingskode(hentTildelingskode());

        addSkdParametersExtractedFromPerson(skdMeldingTrans1, person);
        skdMeldingTrans1.setTranstype("1");

        return skdMeldingTrans1;
    }

    private void addSkdParametersExtractedFromPerson(SkdMeldingTrans1 skdMeldingTrans1, Person person) {
        skdMeldingTrans1.setFodselsdato(person.getIdent().substring(0, 6));
        skdMeldingTrans1.setPersonnummer(person.getIdent().substring(6, 11));

        String yyyyMMdd = ConvertDateToString.yyyyMMdd(person.getRegdato());
        String hhMMss = ConvertDateToString.hhMMss(person.getRegdato());

        skdMeldingTrans1.setMaskintid(hhMMss);
        skdMeldingTrans1.setMaskindato(yyyyMMdd);
        skdMeldingTrans1.setRegdatoSivilstand(yyyyMMdd);
        skdMeldingTrans1.setRegDato(yyyyMMdd);
        skdMeldingTrans1.setRegdatoFamnr(yyyyMMdd);

        Optional<Person> ektefelle = null;
        List<Relasjon> personRelasjoner = relasjonRepository.findByPersonId(person.getId());
        for (Relasjon relasjon : personRelasjoner) {
            if (RelasjonType.EKTEFELLE.getName().equals(relasjon.getRelasjonTypeNavn())) {
                ektefelle = personRepository.findById(relasjon.getPersonRelasjonMed().getId());
                break;
            }
        }
        if (isNull(ektefelle) || !ektefelle.isPresent()) {
            return;
        }

        if (person.getKjonn().equals(ektefelle.get().getKjonn())) {
            skdMeldingTrans1.setAarsakskode(AARSAKS_KO_DE_FOR_INNGAAELSE_PARTNERSKAP);
            skdMeldingTrans1.setSivilstand(Sivilstatus.REGISTRERT_PARTNER.getRelasjonTypeKode());
        } else {
            skdMeldingTrans1.setAarsakskode(AARSAKS_KO_DE_FOR_VIGSEL);
            skdMeldingTrans1.setSivilstand(Sivilstatus.GIFT.getRelasjonTypeKode());
        }

        skdMeldingTrans1.setFamilienummer(ektefelle.get().getIdent());

        skdMeldingTrans1.setEktefellePartnerFoedselsdato(ektefelle.get().getIdent().substring(0, 6));
        skdMeldingTrans1.setEktefellePartnerPersonnr(ektefelle.get().getIdent().substring(6, 11));
        skdMeldingTrans1.setEktefelleEkteskapPartnerskapNr(Integer.toString(1));
        skdMeldingTrans1.setEkteskapPartnerskapNr(Integer.toString(1));

        skdMeldingTrans1.setVigselstype(Integer.toString(1));

        skdMeldingTrans1.setTidligereSivilstand(Sivilstatus.UGIFT.getRelasjonTypeKode());
        skdMeldingTrans1.setEktefelleTidligereSivilstand(Sivilstatus.UGIFT.getRelasjonTypeKode());

        skdMeldingTrans1.setVigselskommune("0301"); // OSLO kommunenummer.
    }

}
