package no.nav.tps.forvalteren.service.command.testdata.utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Jarl Øystein Samseth, Visma Consulting
 */
public class TpsRequestParameterCreator {
    
    public static Map<String, Object> opprettParametereForM201TpsRequest(Collection<String> identer, String aksjonskode) {
        Map<String, Object> tpsRequestParameters = new HashMap<>();
        tpsRequestParameters.put("serviceRutinenavn", "FS03-FDLISTER-DISKNAVN-M-TESTDATA");
        tpsRequestParameters.put("fnr", identer);
        tpsRequestParameters.put("antallFnr", identer.size());
        tpsRequestParameters.put("aksjonsKode", aksjonskode);
        return tpsRequestParameters;
    }
}
