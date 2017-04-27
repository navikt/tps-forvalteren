package no.nav.tps.forvalteren.domain.service.user;

/**
 * Class representing a user
 */

public class User {
    private String name;
    private String username;
    private String token;

    public User(String name, String username) {
        this.name = name;
        this.username = username;
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

    public void setToken(String token) {
        this.token = token;
    }
}
