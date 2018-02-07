package no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.strategies;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.service.tps.config.SkdConstants;
import no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters.DoedsmeldingAnnulleringSkdParamtere;
import no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters.SkdParametersCreator;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.SkdParametersStrategy;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.GetStringVersionOfLocalDateTime;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.SetAdresse;

@Service
public class DoedsmeldingAnnulleringSkdParamterStrategy implements SkdParametersStrategy {

    private static final String AARSAKSKODE_FOR_DOEDSMELDING = "45";
    private static final String TRANSTYPE_FOR_DOEDSMELDING = "1";
    private static final String STATUSKODE_FOR_DOEDSMELDING = "1";

    @Autowired
    private SetAdresse setAdresse;
    
    @Override
    public boolean isSupported(SkdParametersCreator creator) {
        return creator instanceof DoedsmeldingAnnulleringSkdParamtere;
    }

    @Override
    public Map<String, String> execute(Person person) {
        HashMap<String, String> skdParams = new HashMap<>();
        
        addSkdParametersExtractedFromPerson(skdParams, person);
        setAdresse.execute(skdParams, person);
        addDefaultParam(skdParams);
        
        return skdParams;
    }

    private void addSkdParametersExtractedFromPerson(Map<String, String> skdParams, Person person) {
        skdParams.put(SkdConstants.FODSELSDATO, person.getIdent().substring(0, 6));
        skdParams.put(SkdConstants.PERSONNUMMER, person.getIdent().substring(6, 11));

        String yyyyMMdd = GetStringVersionOfLocalDateTime.yyyyMMdd(person.getRegdato());
        String hhMMss = GetStringVersionOfLocalDateTime.hhMMss(person.getRegdato());

        skdParams.put(SkdConstants.MASKINTID, hhMMss);
        skdParams.put(SkdConstants.MASKINDATO, yyyyMMdd);

        String doedsdatoStringVersion = GetStringVersionOfLocalDateTime.yyyyMMdd(LocalDateTime.now());

        skdParams.put(SkdConstants.REG_DATO, doedsdatoStringVersion);
    }

    private void addDefaultParam(Map<String, String> skdParams) {
        skdParams.put(SkdConstants.AARSAKSKODE, AARSAKSKODE_FOR_DOEDSMELDING);
        skdParams.put(SkdConstants.TRANSTYPE, TRANSTYPE_FOR_DOEDSMELDING);
        skdParams.put(SkdConstants.STATUSKODE, STATUSKODE_FOR_DOEDSMELDING);


    }

}
