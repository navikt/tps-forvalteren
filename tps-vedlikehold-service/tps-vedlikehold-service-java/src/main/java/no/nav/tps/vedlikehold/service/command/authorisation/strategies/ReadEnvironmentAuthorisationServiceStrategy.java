package no.nav.tps.vedlikehold.service.command.authorisation.strategies;

import no.nav.tps.vedlikehold.domain.service.User;

import java.util.HashSet;
import java.util.Set;

import static org.springframework.util.ObjectUtils.isEmpty;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */
public class ReadEnvironmentAuthorisationServiceStrategy implements AuthorisationServiceStrategy {

    private static final String ENVIRONMENT_U = "u";
    private static final String ENVIRONMENT_T = "t";
    private static final String ENVIRONMENT_Q = "q";
    private static final String ENVIRONMENT_P = "p";
    private static final String ENVIRONMENT_O = "o";

    private User user;
    private String environment;

    private Set<String> readURoles;
    private Set<String> readTRoles;
    private Set<String> readQRoles;
    private Set<String> readPRoles;
    private Set<String> readORoles;

    
    @Override
    public Boolean isAuthorised() {
        Set<String> userRoles       = new HashSet<>(user.getRoles());           // retainAll() should not modify the users objects roles
        Set<String> authorisedRoles = new HashSet<>();

        if (environment.equals(ENVIRONMENT_U)) {
            authorisedRoles = readURoles;
        } else if (environment.equals(ENVIRONMENT_T)) {
            authorisedRoles = readTRoles;
        } else if (environment.equals(ENVIRONMENT_Q)) {
            authorisedRoles = readQRoles;
        } else if (environment.equals(ENVIRONMENT_P)) {
            authorisedRoles = readPRoles;
        } else if (environment.equals(ENVIRONMENT_O)) {
            authorisedRoles = readORoles;
        }

        /* Retain all roles present in both authorised roles, and the users roles */
        userRoles.retainAll(authorisedRoles);

        return !userRoles.isEmpty();
    }

    /* Setters */

    public void setUser(User user) {
        this.user = user;
    }

    public void setEnvironment(String environment) {
        if (isEmpty(environment)) {
            this.environment = "";
            return;
        }

        this.environment = Character.toString( environment.charAt(0) ).toLowerCase();
    }

    public void setReadURoles(Set<String> readURoles) {
        this.readURoles = readURoles;
    }

    public void setReadTRoles(Set<String> readTRoles) {
        this.readTRoles = readTRoles;
    }

    public void setReadPRoles(Set<String> readPRoles) {
        this.readPRoles = readPRoles;
    }

    public void setReadQRoles(Set<String> readQRoles) {
        this.readQRoles = readQRoles;
    }

    public void setReadORoles(Set<String> readORoles) {
        this.readORoles = readORoles;
    }
}
