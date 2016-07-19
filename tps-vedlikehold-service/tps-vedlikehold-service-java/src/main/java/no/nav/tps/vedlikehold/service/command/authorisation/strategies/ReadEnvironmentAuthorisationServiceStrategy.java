package no.nav.tps.vedlikehold.service.command.authorisation.strategies;

import no.nav.tps.vedlikehold.domain.service.User;

import java.util.Set;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */
public class ReadEnvironmentAuthorisationServiceStrategy implements AuthorisationServiceStrategy {

    private static final String ROLE_READ_U = "0000-GA-NORG_Skriv";
    private static final String ROLE_READ_T = "0000-GA-NORG_Skriv";
    private static final String ROLE_READ_Q = "0000-GA-NORG_Skriv";

    private static final String ENVIRONMENT_U = "u";
    private static final String ENVIRONMENT_T = "t";
    private static final String ENVIRONMENT_Q = "q";

    private User user;
    private String environment;


    @Override
    public Boolean isAuthorised() {
        Set<String> roles = user.getRoles();

        if (environment.equals(ENVIRONMENT_U)) {
            return roles.contains(ROLE_READ_U);
        }

        if (environment.equals(ENVIRONMENT_T)) {
            return roles.contains(ROLE_READ_T);
        }

        if (environment.equals(ENVIRONMENT_Q)) {
            return roles.contains(ROLE_READ_Q);
        }

        return false;
    }

    /* Setters */

    public void setUser(User user) {
        this.user = user;
    }

    public void setEnvironment(String environment) {
        if (environment == null || environment.isEmpty()) {
            this.environment = "";
            return;
        }

        this.environment = Character.toString( environment.charAt(0) ).toLowerCase();
    }
}
