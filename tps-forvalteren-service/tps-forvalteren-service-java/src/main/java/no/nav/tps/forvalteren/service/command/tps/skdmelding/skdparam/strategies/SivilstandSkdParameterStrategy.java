package no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.strategies;

import static no.nav.tps.forvalteren.domain.service.Sivilstand.UGIFT;
import static no.nav.tps.forvalteren.domain.service.Sivilstand.lookup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.service.Sivilstand;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdMeldingTrans1;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.ConvertDateToString;

@Service
public class SivilstandSkdParameterStrategy {

    private static final String SAMBOER = "SAMB";
    private static final String GIFT_LEVER_ADSKILT = "GLAD";

    private static final String AARSAKSKODE_FOR_VIGSEL = "11";
    private static final String AARSAKSKODE_FOR_INNGAAELSE_PARTNERSKAP = "61";
    private static final String AARSAKSKODE_FOR_SEPARASJON = "14";
    private static final String AARSAKSKODE_FOR_PARTNERSKAP_SEPARASJON = "62";
    private static final String AARSAKSKODE_FOR_SKILSMISSE = "18";
    private static final String AARSAKSKODE_FOR_PARTNERSKAP_SKILSMISSE = "63";
    private static final String AARSAKSKODE_FOR_ENKE_OG_GJENLEVENDE_PARTNER = "85";
    private static final String TILDELINGS_KODE = "0";

    public List<SkdMeldingTrans1> execute(Person person) {

        List<SkdMeldingTrans1> skdMeldingstranser = new ArrayList(person.getSivilstander().size());
        Collections.sort(person.getSivilstander(), Comparator.comparing(no.nav.tps.forvalteren.domain.jpa.Sivilstand::getSivilstandRegdato));
        person.getSivilstander().forEach(sivilstand -> {

            if (!UGIFT.getKodeverkskode().equals(sivilstand.getSivilstand()) &&
                    !SAMBOER.equals(sivilstand.getSivilstand()) &&
                    !GIFT_LEVER_ADSKILT.equals(sivilstand.getSivilstand())) {

                skdMeldingstranser.add(addSkdParametersFromPerson(person, sivilstand));
            }
        });

        return skdMeldingstranser;
    }

    private SkdMeldingTrans1 addSkdParametersFromPerson(Person person, no.nav.tps.forvalteren.domain.jpa.Sivilstand sivilstatus) {
        String yyyyMMdd = ConvertDateToString.yyyyMMdd(person.getRegdato());
        String hhMMss = ConvertDateToString.hhMMss(person.getRegdato());

        return SkdMeldingTrans1.builder()
                .tildelingskode(TILDELINGS_KODE)
                .transtype("1")
                .maskindato(yyyyMMdd)
                .maskintid(hhMMss)
                .regDato(yyyyMMdd)
                .regdatoFamnr(yyyyMMdd)

                .aarsakskode(getAarsakskode(lookup(sivilstatus.getSivilstand())))

                .fodselsdato(getFoedselsdato(person.getIdent()))
                .personnummer(getPersonnummer(person.getIdent()))
                .personkode("1")

                .sivilstand(lookup(sivilstatus.getSivilstand()).getRelasjonTypeKode())
                .regdatoSivilstand(ConvertDateToString.yyyyMMdd(sivilstatus.getSivilstandRegdato()))

                .familienummer(person.getIdent())

                .ektefellePartnerFoedselsdato(getFoedselsdato(sivilstatus.getPersonRelasjonMed().getIdent()))
                .ektefellePartnerPersonnr(getPersonnummer(sivilstatus.getPersonRelasjonMed().getIdent()))

                .build();
    }

    private static String getFoedselsdato(String ident) {
        return ident.substring(0, 6);
    }

    private static String getPersonnummer(String ident) {
        return ident.substring(6, 11);
    }

    private static String getAarsakskode(Sivilstand sivilstand) {
        switch (sivilstand) {
        case GIFT:
            return AARSAKSKODE_FOR_VIGSEL;
        case REGISTRERT_PARTNER:
            return AARSAKSKODE_FOR_INNGAAELSE_PARTNERSKAP;
        case SEPARERT:
            return AARSAKSKODE_FOR_SEPARASJON;
        case SEPARERT_PARTNER:
            return AARSAKSKODE_FOR_PARTNERSKAP_SEPARASJON;
        case SKILT:
            return AARSAKSKODE_FOR_SKILSMISSE;
        case SKILT_PARTNER:
            return AARSAKSKODE_FOR_PARTNERSKAP_SKILSMISSE;
        case ENKE_ELLER_ENKEMANN:
        case GJENLEVENDE_PARTNER:
            return AARSAKSKODE_FOR_ENKE_OG_GJENLEVENDE_PARTNER;
        default:
            return "00";
        }
    }
}