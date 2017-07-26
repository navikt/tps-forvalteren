package no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.strategies;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Relasjon;
import no.nav.tps.forvalteren.domain.service.RelasjonType;
import no.nav.tps.forvalteren.domain.service.tps.config.SkdConstants;
import no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters.SkdParametersCreator;
import no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters.VigselSkdParametere;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.SkdParametersStrategy;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.GetStringversionOfLocalDateTime;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class VigselSkdParameterStrategy implements SkdParametersStrategy {

    private static final String AARSAKSKODE_FOR_VIGSEL  = "11";

    @Override
    public boolean isSupported(SkdParametersCreator creator) {
        return creator instanceof VigselSkdParametere;
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

        String yyyyMMdd = GetStringversionOfLocalDateTime.yyyyMMdd(person.getRegdato());
        String hhMMss = GetStringversionOfLocalDateTime.hhMMss(person.getRegdato());

        skdParams.put(SkdConstants.MASKINTID, hhMMss);
        skdParams.put(SkdConstants.MASKINDATO, yyyyMMdd);
        skdParams.put(SkdConstants.REGDATO_SIVILSTAND, yyyyMMdd);
        skdParams.put(SkdConstants.REG_DATO, yyyyMMdd);

        Person ektefelle = null;
        for(Relasjon relasjon : person.getRelasjoner()){
            if(relasjon.getRelasjonTypeKode() == RelasjonType.GIFT.getRelasjonTypeKode()){
                ektefelle = relasjon.getPersonRelasjonMed();
            }
        }
        if(ektefelle == null ){
            return;
        }

        skdParams.put(SkdConstants.EKTEFELLE_PARTNER_FODSELSDATO, ektefelle.getIdent().substring(0, 6));
        skdParams.put(SkdConstants.EKTEFELLE_PARTNER_PERSONNUMMMER, ektefelle.getIdent().substring(6, 11));

        skdParams.put(SkdConstants.FAMILIENUMMER, ektefelle.getIdent());

        addDefaultParam(skdParams);
    }

    private void addDefaultParam(Map<String, String> skdParams) {
        skdParams.put(SkdConstants.AARSAKSKODE, AARSAKSKODE_FOR_VIGSEL);

        skdParams.put(SkdConstants.SIVILSTAND, ""+ RelasjonType.GIFT.getRelasjonTypeKode());
        skdParams.put(SkdConstants.TRANSTYPE, "1");
        skdParams.put("T1-STATUSKODE", "1");
    }
}
