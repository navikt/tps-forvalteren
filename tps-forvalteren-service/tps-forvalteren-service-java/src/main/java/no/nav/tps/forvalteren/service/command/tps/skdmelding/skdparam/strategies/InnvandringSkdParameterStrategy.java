package no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.strategies;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.service.tps.config.SkdConstants;
import no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters.InnvandringSkdParametere;
import no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters.SkdParametersCreator;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.SkdParametersStrategy;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.GetStringversionOfLocalDateTime;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class InnvandringSkdParameterStrategy implements SkdParametersStrategy {

    private static final String TILDELINGSKODE_FOR_ENDRING = "2";
    private static final String TILDELINGSKODE_FOR_OPPRETT = "1";
    private static final String AARSAKSKODE_FOR_INNVANDRING = "02";

    @Override
    public boolean isSupported(SkdParametersCreator creator) {
        return creator instanceof InnvandringSkdParametere;
    }

    @Override
    public Map<String, String> execute(Person person) {
        HashMap<String, String> skdParams = new HashMap<>();

        skdParams.put(SkdConstants.TILDELINGSKODE, TILDELINGSKODE_FOR_OPPRETT);

        addSkdParametersExtractedFromPerson(skdParams, person);

        return skdParams;
     }

    private void addSkdParametersExtractedFromPerson(Map<String, String> skdParams, Person person) {
        skdParams.put(SkdConstants.FODSELSDATO, person.getIdent().substring(0, 6));
        skdParams.put(SkdConstants.PERSONNUMMER, person.getIdent().substring(6, 11));
        skdParams.put(SkdConstants.FORNAVN, person.getFornavn());
        skdParams.put(SkdConstants.MELLOMNAVN, person.getMellomnavn());
        skdParams.put(SkdConstants.SLEKTSNAVN, person.getEtternavn());
        skdParams.put(SkdConstants.STATSBORGERSKAP, person.getStatsborgerskap());

        String yyyyMMdd = GetStringversionOfLocalDateTime.yyyyMMdd(person.getRegdato());
        String hhMMss = GetStringversionOfLocalDateTime.hhMMss(person.getRegdato());

        skdParams.put(SkdConstants.MASKINTID, hhMMss);
        skdParams.put(SkdConstants.MASKINDATO, yyyyMMdd);
        skdParams.put(SkdConstants.REG_DATO, yyyyMMdd);
        skdParams.put(SkdConstants.REG_DATO_ADR, yyyyMMdd);
        skdParams.put(SkdConstants.FLYTTEDATO_ADR, yyyyMMdd);
        skdParams.put(SkdConstants.FRA_LAND_REGDATO, yyyyMMdd);
        skdParams.put(SkdConstants.FRA_LAND_FLYTTEDATO, yyyyMMdd);
        skdParams.put(SkdConstants.REG_DATO_FAM_NR, yyyyMMdd);

        addDefaultParam(skdParams);
    }

    private void addDefaultParam(Map<String, String> skdParams) {
        skdParams.put("T1-AARSAKSKODE", AARSAKSKODE_FOR_INNVANDRING);

        skdParams.put("T1-INNVANDRET-FRA-LAND", "001");
        skdParams.put("T1-FAMILIENUMMER", "08096740140");
        skdParams.put("T1-PERSONKODE", "1");
        skdParams.put("T1-SIVILSTAND", "1");
        skdParams.put("T1-TRANSTYPE", "1");
        skdParams.put("T1-STATUSKODE", "1");
    }

    private String formaterDato(int tid) {
        if (tid < 10) {
            return "0" + tid;
        }
        return String.valueOf(tid);
    }
}
