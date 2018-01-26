package no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.strategies;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.service.tps.config.SkdConstants;
import no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters.InnvandringSkdParametere;
import no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters.SkdParametersCreator;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.SkdParametersStrategy;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.GetStringVersionOfLocalDateTime;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.SetAdresse;

@Service
public class InnvandringSkdParameterStrategy implements SkdParametersStrategy {

    private static final String TILDELINGSKODE_FOR_OPPRETT = "1";
    private static final String AARSAKSKODE_FOR_INNVANDRING = "02";

    @Autowired
    private SetAdresse setAdresse;

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

        String yyyyMMdd = GetStringVersionOfLocalDateTime.yyyyMMdd(person.getRegdato());
        String hhMMss = GetStringVersionOfLocalDateTime.hhMMss(person.getRegdato());

        skdParams.put(SkdConstants.MASKINTID, hhMMss);
        skdParams.put(SkdConstants.MASKINDATO, yyyyMMdd);
        skdParams.put(SkdConstants.REG_DATO, yyyyMMdd);
        skdParams.put(SkdConstants.REG_DATO_ADR, yyyyMMdd);
        skdParams.put(SkdConstants.FLYTTEDATO_ADR, yyyyMMdd);
        skdParams.put(SkdConstants.FRA_LAND_REGDATO, yyyyMMdd);
        skdParams.put(SkdConstants.FRA_LAND_FLYTTEDATO, yyyyMMdd);
        skdParams.put(SkdConstants.REG_DATO_FAM_NR, yyyyMMdd);

        setAdresse.execute(skdParams, person);
        addSpesreg(skdParams, person);
        addDefaultParam(skdParams);
    }

    private void addSpesreg(Map<String, String> skdParams, Person person) {
        if (person.getSpesreg() != null) {
            skdParams.put("spesRegType", person.getSpesreg());
        }
        LocalDateTime spesregDato = person.getSpesregDato();
        if (spesregDato != null) {
            skdParams.put("datoSpesRegType", String.format("%04d%02d%02d", spesregDato.getYear(), spesregDato.getMonthValue(), spesregDato.getDayOfMonth()));
        }
    }

    private void addDefaultParam(Map<String, String> skdParams) {
        skdParams.put(SkdConstants.AARSAKSKODE, AARSAKSKODE_FOR_INNVANDRING);

        skdParams.put("innvandretFraLand", "001");
        skdParams.put("familienummer", "08096740140");
        skdParams.put("personkode", "1");
        skdParams.put("sivilstand", "1");
        skdParams.put("transtype", "1");
        skdParams.put("statuskode", "1");
    }

}
