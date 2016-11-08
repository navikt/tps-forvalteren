package no.nav.tps.vedlikehold.domain.service.command.tps.authorisation.strategies;

import java.util.ArrayList;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */
public class DiskresjonskodeAuthorisationStrategy implements AuthorisationStrategy {

    ArrayList<String> requiredParam = new ArrayList<>();

    public DiskresjonskodeAuthorisationStrategy(String fnrParamKeyName){
        requiredParam.add(fnrParamKeyName);
    }

    @Override
    public String getRequiredParamKeyName(){
       return requiredParam.get(0);
    }
}
