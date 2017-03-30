package no.nav.tps.vedlikehold.domain.service.User;

import java.util.Set;

/**
 * Class representing a user
 */

public class User {
    private String name;
    private String username;
    private String token;
    private Set<String> roles;

    public User(String name, String username, Set<String> roles) {
        this.name = name;
        this.username = username;
        this.roles = roles;
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

    public void setToken(String token) {
        this.token = token;
    }
}
