package no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.strategies;

import java.util.List;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Relasjon;
import no.nav.tps.forvalteren.domain.service.RelasjonType;
import no.nav.tps.forvalteren.domain.service.Sivilstand;
import no.nav.tps.forvalteren.domain.service.tps.skdmelding.enums.Aarsakskode;
import no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters.EkteskapSkdParametere;
import no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters.SkdParametersCreator;
import no.nav.tps.forvalteren.repository.jpa.PersonRepository;
import no.nav.tps.forvalteren.repository.jpa.RelasjonRepository;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdMeldingTrans1;
import no.nav.tps.forvalteren.service.command.testdata.utils.HentKjoennFraIdent;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.SkdParametersStrategy;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.ConvertDateToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EkteskapSkdParameterStrategy implements SkdParametersStrategy {

    private static final String AARSAKSKODE_FOR_VIGSEL = Aarsakskode.VIGSEL.toString();
    private static final String AARSAKSKODE_FOR_INNGAAELSE_PARTNERSKAP = Aarsakskode.INNGAAELSE_PARTNERSKAP.toString();
    private static final String TILDELINGSKODE_PARTNERSKAP = "0";

//    @Autowired
//    private RelasjonRepository relasjonRepository;
//
//    @Autowired
//    private PersonRepository personRepository;

    @Autowired
    private HentKjoennFraIdent hentKjoennFraIdent;

    @Override
    public String hentTildelingskode() {
        return TILDELINGSKODE_PARTNERSKAP;
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

        String ektefelleIdent = null;
//        List<Relasjon> personRelasjoner = relasjonRepository.findByPersonId(person.getId());
        for (Relasjon relasjon : person.getRelasjoner()) {
            if (RelasjonType.EKTEFELLE.getRelasjonTypeNavn().equals(relasjon.getRelasjonTypeNavn())) {
//                ektefelle = personRepository.findById(relasjon.getPersonRelasjonMed().getId());
                ektefelleIdent = relasjon.getPersonRelasjonMed().getIdent();
                break;
            }
        }
        if (ektefelleIdent == null) {
            return;
        }

        if (person.getKjonn().equals(hentKjoennFraIdent.execute(ektefelleIdent))) {
            skdMeldingTrans1.setAarsakskode(AARSAKSKODE_FOR_INNGAAELSE_PARTNERSKAP);
            skdMeldingTrans1.setSivilstand(Integer.toString(Sivilstand.REGISTRERT_PARTNER.getRelasjonTypeKode()));
        } else {
            skdMeldingTrans1.setAarsakskode(AARSAKSKODE_FOR_VIGSEL);
            skdMeldingTrans1.setSivilstand(Integer.toString(Sivilstand.GIFT.getRelasjonTypeKode()));
        }

        skdMeldingTrans1.setFamilienummer(ektefelleIdent);

        skdMeldingTrans1.setEktefellePartnerFoedselsdato(ektefelleIdent.substring(0, 6));
        skdMeldingTrans1.setEktefellePartnerPersonnr(ektefelleIdent.substring(6, 11));
        skdMeldingTrans1.setEktefelleEkteskapPartnerskapNr(Integer.toString(1));
        skdMeldingTrans1.setEkteskapPartnerskapNr(Integer.toString(1));

        skdMeldingTrans1.setVigselstype(Integer.toString(1));

        skdMeldingTrans1.setTidligereSivilstand(Integer.toString(Sivilstand.UGIFT.getRelasjonTypeKode()));
        skdMeldingTrans1.setEktefelleTidligereSivilstand(Integer.toString(Sivilstand.UGIFT.getRelasjonTypeKode()));

        skdMeldingTrans1.setVigselskommune("0301"); // OSLO kommunenummer.
    }

}
