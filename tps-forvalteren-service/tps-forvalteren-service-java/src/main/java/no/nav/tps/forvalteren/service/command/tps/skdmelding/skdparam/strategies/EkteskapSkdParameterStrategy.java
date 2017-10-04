package no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.strategies;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Relasjon;
import no.nav.tps.forvalteren.domain.service.RelasjonType;
import no.nav.tps.forvalteren.domain.service.Sivilstand;
import no.nav.tps.forvalteren.domain.service.tps.config.SkdConstants;
import no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters.EkteskapSkdParametere;
import no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters.SkdParametersCreator;
import no.nav.tps.forvalteren.repository.jpa.PersonRepository;
import no.nav.tps.forvalteren.repository.jpa.RelasjonRepository;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.SkdParametersStrategy;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.GetStringVersionOfLocalDateTime;

@Service
public class EkteskapSkdParameterStrategy implements SkdParametersStrategy {

    private static final String AARSAKSKODE_FOR_VIGSEL = "11";
    private static final String AARSAKSKODE_FOR_INNGAAELSE_PARTNERSKAP = "61";

    @Autowired
    private RelasjonRepository relasjonRepository;

    @Autowired
    private PersonRepository personRepository;

    @Override
    public boolean isSupported(SkdParametersCreator creator) {
        return creator instanceof EkteskapSkdParametere;
    }

    @Override
    public Map<String, String> execute(Person person) {
        HashMap<String, String> skdParams = new HashMap<>();

        addSkdParametersExtractedFromPerson(skdParams, person);

        return skdParams;
    }

    private void addSkdParametersExtractedFromPerson(Map<String, String> skdParams, Person person) {
        skdParams.put(SkdConstants.FODSELSDATO, person.getIdent().substring(0, 6));
        skdParams.put(SkdConstants.PERSONNUMMER, person.getIdent().substring(6, 11));

        String yyyyMMdd = GetStringVersionOfLocalDateTime.yyyyMMdd(person.getRegdato());
        String hhMMss = GetStringVersionOfLocalDateTime.hhMMss(person.getRegdato());

        skdParams.put(SkdConstants.MASKINTID, hhMMss);
        skdParams.put(SkdConstants.MASKINDATO, yyyyMMdd);
        skdParams.put(SkdConstants.REGDATO_SIVILSTAND, yyyyMMdd);
        skdParams.put(SkdConstants.REG_DATO, yyyyMMdd);
        skdParams.put(SkdConstants.REG_DATO_FAM_NR, yyyyMMdd);

        Person ektefelle = null;
        List<Relasjon> personRelasjoner = relasjonRepository.findByPersonId(person.getId());
        for (Relasjon relasjon : personRelasjoner) {
            if (RelasjonType.EKTEFELLE.getRelasjonTypeNavn().equals(relasjon.getRelasjonTypeNavn())) {
                ektefelle = personRepository.findById(relasjon.getPersonRelasjonMed().getId());
                break;
            }
        }
        if (ektefelle == null) {
            return;
        }

        if (person.getKjonn().equals(ektefelle.getKjonn())) {
            skdParams.put(SkdConstants.AARSAKSKODE, AARSAKSKODE_FOR_INNGAAELSE_PARTNERSKAP);
            skdParams.put(SkdConstants.SIVILSTAND, Integer.toString(Sivilstand.REGISTRERT_PARTNER.getRelasjonTypeKode()));
        } else {
            skdParams.put(SkdConstants.AARSAKSKODE, AARSAKSKODE_FOR_VIGSEL);
            skdParams.put(SkdConstants.SIVILSTAND, Integer.toString(Sivilstand.GIFT.getRelasjonTypeKode()));
        }

        skdParams.put(SkdConstants.FAMILIENUMMER, ektefelle.getIdent());

        skdParams.put(SkdConstants.EKTEFELLE_PARTNER_FODSELSDATO, ektefelle.getIdent().substring(0, 6));
        skdParams.put(SkdConstants.EKTEFELLE_PARTNER_PERSONNUMMMER, ektefelle.getIdent().substring(6, 11));

        skdParams.put(SkdConstants.EKTEFELLE_EKTESKAP_PARTNERSKAP_NUMMER, Integer.toString(1));
        skdParams.put(SkdConstants.EKTESKAP_PARTNERSKAP_NUMMER, Integer.toString(1));

        skdParams.put(SkdConstants.VIGSELSTYPE, Integer.toString(1));

        skdParams.put(SkdConstants.TIDLIGERE_SIVILSTAND, Integer.toString(Sivilstand.UGIFT.getRelasjonTypeKode()));
        skdParams.put(SkdConstants.EKTEFELLE_TIDLIGERE_SIVILSTAND, Integer.toString(Sivilstand.UGIFT.getRelasjonTypeKode()));

        skdParams.put(SkdConstants.VIGSELSKOMMUNE, "0301"); // OSLO kommunenummer.

        addDefaultParam(skdParams);
    }

    private void addDefaultParam(Map<String, String> skdParams) {
        skdParams.put(SkdConstants.TILDELINGSKODE, "0");
        skdParams.put(SkdConstants.TRANSTYPE, "1");
    }

}
