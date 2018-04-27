package no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.strategies;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Relasjon;
import no.nav.tps.forvalteren.domain.service.RelasjonType;
import no.nav.tps.forvalteren.domain.service.tps.config.SkdConstants;
import no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters.FoedselsmeldingSkdParametere;
import no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters.SkdParametersCreator;
import no.nav.tps.forvalteren.repository.jpa.PersonRepository;
import no.nav.tps.forvalteren.repository.jpa.RelasjonRepository;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.SkdParametersStrategy;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.ConvertDateToString;
import org.springframework.beans.factory.annotation.Autowired;

public class FoedselsmeldingSkdParameterStrategy implements SkdParametersStrategy {

    private static final String AARSAKSKODE_FOR_FOEDSELSMELDING = "01";
    private static final String TILDELINGSKODE_FOEDSELSMELDING = "0";

    @Autowired
    private RelasjonRepository relasjonRepository;

    @Autowired
    private PersonRepository personRepository;

    @Override
    public Map<String, String> execute(Person person) {

        HashMap<String, String> skdParams = new HashMap<>();
        skdParams.put(SkdConstants.TILDELINGSKODE, hentTildelingskode());
        addSkdParametersExtractedFromPerson(skdParams, person);
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


    private void addSkdParametersExtractedFromPerson(Map<String, String> skdParams, Person person){
        // her er person objektet barnet som er blitt født;
        skdParams.put(SkdConstants.FODSELSDATO, person.getIdent().substring(0, 6));
        skdParams.put(SkdConstants.PERSONNUMMER, person.getIdent().substring(6, 11));

        String yyyyMMdd = ConvertDateToString.yyyyMMdd(person.getRegdato());
        String hhMMss = ConvertDateToString.hhMMss(person.getRegdato());

        skdParams.put(SkdConstants.MASKINTID, hhMMss);
        skdParams.put(SkdConstants.MASKINDATO, yyyyMMdd);
        skdParams.put(SkdConstants.REG_DATO, yyyyMMdd);

        Person forelder = null;
        List<Relasjon> personRelasjon = relasjonRepository.findByPersonId(person.getId());
        for(Relasjon relasjon : personRelasjon){
            if(RelasjonType.MOR.getRelasjonTypeNavn().equals(relasjon.getRelasjonTypeNavn())){
                forelder = personRepository.findById(relasjon.getPersonRelasjonMed().getId());
                break;
            }
        }
        if(forelder == null) {
            return;
        }

        //
        skdParams.put(SkdConstants.FOEDEKOMM_LAND, "0301");
        skdParams.put(SkdConstants.MORS_FOEDSELSDATO, forelder.getIdent().substring(0, 6));
        skdParams.put(SkdConstants.MORS_PERSONNUMMER, forelder.getIdent().substring(6, 11));

        skdParams.put(SkdConstants.LEVENDE_DOED, "1");
        skdParams.put(SkdConstants.KJOENN, Integer.parseInt(person.getIdent().substring(8,9)) % 2 == 0 ? "K" : "M" );
    }

    private void addDefaultParam(Map<String, String> skdParams){
        skdParams.put(SkdConstants.TRANSTYPE, "1");
    }
}
