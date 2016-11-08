package no.nav.tps.vedlikehold.domain.service.command.tps.authorisation.strategies;

import java.util.ArrayList;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */
public class EgenAnsattAuthorisationStrategy implements AuthorisationStrategy {

    ArrayList<String> requiredParam = new ArrayList<>();

    public EgenAnsattAuthorisationStrategy(String fnrParamKeyName){
        requiredParam.add(fnrParamKeyName);
    }

    @Override
    public String getRequiredParamKeyName(){
        return requiredParam.get(0);
    }
}
