package no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.strategies;

import java.util.HashMap;
import java.util.Map;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Vergemaal;
import no.nav.tps.forvalteren.domain.service.tps.config.SkdConstants;
import no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters.SkdParametersCreator;
import no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters.VergemaalSkdParametere;
import no.nav.tps.forvalteren.repository.jpa.VergemaalRepository;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.SkdParametersStrategy;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.ConvertDateToString;
import org.springframework.beans.factory.annotation.Autowired;

public class VergemaalSkdParameterStrategy implements SkdParametersStrategy {

    private static final String AARSAKSKODE_FOR_VERGEMAAL = "37";
    private static final String TILDELINGSKODE_VERGEMAAL = "0";
    Vergemaal vergemaal = null;

    @Autowired
    private VergemaalRepository vergemaalRepository;

    public void settVergemaal(Vergemaal vergemaal) {
        this.vergemaal = vergemaal;
    }

    @Override
    public String hentTildelingskode() {
        return TILDELINGSKODE_VERGEMAAL;
    }

    @Override
    public boolean isSupported(SkdParametersCreator creator) {
        return creator instanceof VergemaalSkdParametere;
    }

    @Override
    public Map<String, String> execute(Person person) {
        String tildelingskodeForVergemaal = hentTildelingskode();

        HashMap<String, String> skdParams = new HashMap<>();
        skdParams.put(SkdConstants.TILDELINGSKODE, tildelingskodeForVergemaal);

        addSkdParameterExtractedFromPerson(skdParams, person);
        addSkdParameterExtractedFromVergemaal(skdParams, vergemaal);

        return skdParams;
    }


    private void addSkdParameterExtractedFromPerson(Map<String, String> skdParams, Person person) {

        skdParams.put(SkdConstants.FODSELSDATO, person.getIdent().substring(0, 6));
        skdParams.put(SkdConstants.PERSONNUMMER, person.getIdent().substring(6, 11));

        String yyyyMMdd = ConvertDateToString.yyyyMMdd(person.getRegdato());
        String hhMMss = ConvertDateToString.hhMMss(person.getRegdato());

        skdParams.put(SkdConstants.MASKINTID, hhMMss);
        skdParams.put(SkdConstants.MASKINDATO, yyyyMMdd);
        skdParams.put(SkdConstants.REG_DATO, yyyyMMdd);

        skdParams.put(SkdConstants.AARSAKSKODE, AARSAKSKODE_FOR_VERGEMAAL);

    }
    private void addSkdParameterExtractedFromVergemaal(Map<String, String> skdParams, Vergemaal vergemaal)
    {
        String yyyyMMdd = ConvertDateToString.yyyyMMdd(vergemaal.getVedtaksdato());

        skdParams.put(SkdConstants.SAKSID, vergemaal.getSaksid());
        skdParams.put(SkdConstants.EMBETE, vergemaal.getEmbete());
        skdParams.put(SkdConstants.SAKSTYPE, vergemaal.getSakstype());
        skdParams.put(SkdConstants.VEDTAKSDATO, yyyyMMdd);
        skdParams.put(SkdConstants.INTERN_VERGE_ID, vergemaal.getVergeid());
        skdParams.put(SkdConstants.VERGE_FNR_DNR, vergemaal.getVergefnr());
        skdParams.put(SkdConstants.VERGETYPE, vergemaal.getVergetype());
        skdParams.put(SkdConstants.MANDATTYPE, vergemaal.getMandattype());
        skdParams.put(SkdConstants.MANDATTEKST, vergemaal.getMandattekst());

        /*
        skdParams.put(SkdConstants.SAKS_ID, vergemaal.getSaksid());
        skdParams.put(SkdConstants.EMBETE, vergemaal.getEmbete());
        skdParams.put(SkdConstants.SAKSTYPE, vergemaal.getSakstype());

        String yyyyMMdd = ConvertDateToString.yyyyMMdd(vergemaal.getVedtaksdato());

        skdParams.put(SkdConstants.VEDTAKSDATO, yyyyMMdd);
        skdParams.put(SkdConstants.INTERNVERGE_ID, vergemaal.getVergeid());
        skdParams.put(SkdConstants.VERGE_FNR, vergemaal.getVergefnr());
        skdParams.put(SkdConstants.VERGETYPE, vergemaal.getVergetype());
        skdParams.put(SkdConstants.MANDATTYPE, vergemaal.getMandattype());
        skdParams.put(SkdConstants.MANDATTEKST, vergemaal.getMandattekst());
        */
        addDefaultParam(skdParams);
    }

    private void addDefaultParam(Map<String, String> skdParams) {

        skdParams.put(SkdConstants.TRANSTYPE, "1");
    }
}
