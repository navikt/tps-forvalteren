package no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.strategies;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.service.tps.config.SkdConstants;
import no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters.DoedsmeldingSkdParametere;
import no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters.SkdParametersCreator;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.SkdParametersStrategy;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.ConvertDateToString;

@Service
public class DoedsmeldingSkdParameterStrategy implements SkdParametersStrategy {

    private static final String AARSAKSKODE_FOR_DOEDSMELDING = "43";
    private static final String TRANSTYPE_FOR_DOEDSMELDING = "1";
    private static final String STATUSKODE_FOR_DOEDSMELDING = "5";
    private static final String TILDELINGSKODE_DOEDSMELDING = "0";

    @Override public String hentTildelingskode() {
        return TILDELINGSKODE_DOEDSMELDING;
    }

    @Override
    public boolean isSupported(SkdParametersCreator creator) {
        return creator instanceof DoedsmeldingSkdParametere;
    }

    @Override
    public Map<String, String> execute(Person person) {
        String tildelingskodeForDodsmelding = hentTildelingskode();

        HashMap<String, String> skdParams = new HashMap<>();
        skdParams.put(SkdConstants.TILDELINGSKODE, tildelingskodeForDodsmelding);

        addSkdParametersExtractedFromPerson(skdParams, person);

        return skdParams;
    }

    private void addSkdParametersExtractedFromPerson(Map<String, String> skdParams, Person person) {
        skdParams.put(SkdConstants.FODSELSDATO, person.getIdent().substring(0, 6));
        skdParams.put(SkdConstants.PERSONNUMMER, person.getIdent().substring(6, 11));

        String yyyyMMdd = ConvertDateToString.yyyyMMdd(person.getRegdato());
        String hhMMss = ConvertDateToString.hhMMss(person.getRegdato());

        skdParams.put(SkdConstants.MASKINTID, hhMMss);
        skdParams.put(SkdConstants.MASKINDATO, yyyyMMdd);

        String doedsdatoStringVersion = ConvertDateToString.yyyyMMdd(person.getDoedsdato());

        // The specification for doedsmelding says reg-dato should be doedsdato
        skdParams.put(SkdConstants.REG_DATO, doedsdatoStringVersion);
        skdParams.put(SkdConstants.DOEDSDATO, doedsdatoStringVersion);

        addDefaultParam(skdParams);
    }

    private void addDefaultParam(Map<String, String> skdParams) {
        skdParams.put(SkdConstants.AARSAKSKODE, AARSAKSKODE_FOR_DOEDSMELDING);
        skdParams.put(SkdConstants.TRANSTYPE, TRANSTYPE_FOR_DOEDSMELDING);
        skdParams.put(SkdConstants.STATUSKODE, STATUSKODE_FOR_DOEDSMELDING);
    }
}
