package no.nav.tps.vedlikehold.domain.service.command.tps;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.HashMap;

/**
 * Created by f148888 on 29.09.2016.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class TpsRequest {

    HashMap<String, Object> parameters = new HashMap<>();
    private String environment;

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
        addToParameterMap("environment", environment);
    }

    protected void addToParameterMap(String paramKey, String paramValue){
        parameters.put(paramKey, paramValue);
    }

    public String getParamValue(String paramKey){
        return parameters.get(paramKey).toString();
    }
}
