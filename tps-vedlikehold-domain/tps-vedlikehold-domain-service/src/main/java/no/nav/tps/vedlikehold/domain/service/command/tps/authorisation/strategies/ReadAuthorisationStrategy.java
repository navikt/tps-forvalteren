package no.nav.tps.vedlikehold.domain.service.command.tps.authorisation.strategies;

import java.util.ArrayList;


/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */
public class ReadAuthorisationStrategy implements AuthorisationStrategy {

    ArrayList<String> requiredParams = new ArrayList<>();

    public ReadAuthorisationStrategy(String param){
        requiredParams.add(param);
    }

    @Override
    public String getRequiredParamKeyName() {
        return requiredParams.get(0);
    }

}
