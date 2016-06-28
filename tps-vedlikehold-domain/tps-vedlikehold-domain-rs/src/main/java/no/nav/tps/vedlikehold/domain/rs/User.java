package no.nav.tps.vedlikehold.domain.rs;

import java.util.Set;

/**
 * Class representing a user
 *
 * @author Øyvind Grimnes, Visma Consulting AS
 */

public class User {
    private String name;
    private String username;
    private String token;
    private Set<String> roles;

    public User() {
        // Used by Jackson?
    }

    public User(String name, String username, Set<String> roles, String token) {
        this.name = name;
        this.username = username;
        this.roles = roles;
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getToken() {
        return token;
    }

    public Set<String> getRoles() {
        return roles;
    }
}
