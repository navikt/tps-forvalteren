package no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.strategies;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import no.nav.tps.forvalteren.domain.jpa.Gateadresse;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Relasjon;
import no.nav.tps.forvalteren.domain.service.RelasjonType;
import no.nav.tps.forvalteren.domain.service.tps.config.SkdConstants;
import no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters.FoedselsmeldingSkdParametere;
import no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters.SkdParametersCreator;
import no.nav.tps.forvalteren.repository.jpa.AdresseRepository;
import no.nav.tps.forvalteren.repository.jpa.PersonRepository;
import no.nav.tps.forvalteren.repository.jpa.RelasjonRepository;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.SkdParametersStrategy;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.ConvertDateToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FoedselsmeldingSkdParameterStrategy implements SkdParametersStrategy {

    private static final String AARSAKSKODE_FOR_FOEDSELSMELDING = "01";
    private static final String TILDELINGSKODE_FOEDSELSMELDING = "0";
    String yyyyMMdd;
    String hhMMss;

    @Autowired
    private RelasjonRepository relasjonRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private AdresseRepository adresseRepository;

    @Override
    public Map<String, String> execute(Person person) {

        HashMap<String, String> skdParams = new HashMap<>();
        skdParams.put(SkdConstants.TILDELINGSKODE, hentTildelingskode());
        addSkdParametersExtractedFromPerson(skdParams, person);
        addSkdParametersExtractedFromForeldre(skdParams, person);
        addDefaultParam(skdParams);

        return skdParams;
    }

    @Override
    public String hentTildelingskode() {
        return TILDELINGSKODE_FOEDSELSMELDING;
    }

    @Override
    public boolean isSupported(SkdParametersCreator creator) {
        return creator instanceof FoedselsmeldingSkdParametere;
    }

    private void addSkdParametersExtractedFromPerson(Map<String, String> skdParams, Person person) {
        // person er barnet som skal bli født.
        skdParams.put(SkdConstants.FODSELSDATO, person.getIdent().substring(0, 6));
        skdParams.put(SkdConstants.PERSONNUMMER, person.getIdent().substring(6, 11));

        yyyyMMdd = ConvertDateToString.yyyyMMdd(person.getRegdato());
        hhMMss = ConvertDateToString.hhMMss(person.getRegdato());

        skdParams.put(SkdConstants.MASKINTID, hhMMss);
        skdParams.put(SkdConstants.MASKINDATO, yyyyMMdd);
        skdParams.put(SkdConstants.REG_DATO, yyyyMMdd);

        skdParams.put(SkdConstants.FOEDEKOMM_LAND, "0301"); // MÅ KUNNE SETTES.
        skdParams.put(SkdConstants.FOEDESTED, "Sykehus");

        skdParams.put(SkdConstants.FORNAVN, person.getFornavn());
        skdParams.put(SkdConstants.KJOENN, Integer.parseInt(person.getIdent().substring(8, 9)) % 2 == 0 ? "K" : "M");

        skdParams.put(SkdConstants.REG_DATO_ADR, yyyyMMdd);

        skdParams.put(SkdConstants.PERSONKODE, "3");
        skdParams.put(SkdConstants.LEVENDE_DOED, "1");

    }

    private void addSkdParametersExtractedFromForeldre(Map<String, String> skdParams, Person person) {
        Person forelderMor = null;
        Person forelderFar = null;
        List<Relasjon> personRelasjon = relasjonRepository.findByPersonId(person.getId());
        for (Relasjon relasjon : personRelasjon) {
            if (RelasjonType.MOR.getRelasjonTypeNavn().equals(relasjon.getRelasjonTypeNavn())) {
                forelderMor = personRepository.findById(relasjon.getPersonRelasjonMed().getId());
            }
            if (RelasjonType.FAR.getRelasjonTypeNavn().equals(relasjon.getRelasjonTypeNavn())) {
                forelderFar = personRepository.findById(relasjon.getPersonRelasjonMed().getId());
            }
        }
        if (forelderMor == null) {
            return;
        }

        skdParams.put(SkdConstants.MORS_FOEDSELSDATO, forelderMor.getIdent().substring(0, 6));
        skdParams.put(SkdConstants.MORS_PERSONNUMMER, forelderMor.getIdent().substring(6, 11));
        skdParams.put(SkdConstants.SLEKTSNAVN, forelderMor.getEtternavn());
        skdParams.put(SkdConstants.FAMILIENUMMER, forelderMor.getIdent());
        skdParams.put(SkdConstants.REG_DATO_FAM_NR, yyyyMMdd);

        if (forelderFar != null && forelderMor != null) {
            skdParams.put(SkdConstants.FORELDREANSVAR, "D");
        } else {
            skdParams.put(SkdConstants.FORELDREANSVAR, "M");
        }
        skdParams.put(SkdConstants.DATO_FORELDREANSVAR, yyyyMMdd);

        if (forelderFar != null) {
            skdParams.put(SkdConstants.FARS_FOEDSELSDATO, forelderFar.getIdent().substring(0, 6));
            skdParams.put(SkdConstants.FARS_PERSONNUMMER, forelderFar.getIdent().substring(6, 11));
        }
        addAdresseParamsExtractedFromForelder(skdParams, forelderMor);
    }

    private void addAdresseParamsExtractedFromForelder(Map<String, String> skdParams, Person forelder) {
        Gateadresse adresse = adresseRepository.getAdresseByPersonId(forelder.getId());

        if (adresse != null) {
            skdParams.put(SkdConstants.KOMMUNENUMMER, adresse.getKommunenr());
            skdParams.put(SkdConstants.ADRESSENAVN, adresse.getAdresse());
            skdParams.put(SkdConstants.POSTNUMMER, adresse.getPostnr());
            skdParams.put(SkdConstants.HUSBRUK, adresse.getHusnummer());
            skdParams.put(SkdConstants.GATEGAARD, adresse.getGatekode());

            skdParams.put(SkdConstants.ADRESSETYPE, "O");
        } else {
            skdParams.put(SkdConstants.ADRESSETYPE, "M");

        }

    }

    private void addDefaultParam(Map<String, String> skdParams) {

        skdParams.put(SkdConstants.SIVILSTAND, "1");

        skdParams.put(SkdConstants.TRANSTYPE, "1");
        skdParams.put(SkdConstants.AARSAKSKODE, AARSAKSKODE_FOR_FOEDSELSMELDING);
        skdParams.put(SkdConstants.STATUSKODE, "1");
    }
}
