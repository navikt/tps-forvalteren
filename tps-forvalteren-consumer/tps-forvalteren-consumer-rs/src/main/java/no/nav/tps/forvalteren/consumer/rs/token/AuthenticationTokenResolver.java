package no.nav.tps.forvalteren.consumer.rs.token;

public interface AuthenticationTokenResolver {
    String getTokenValue();

    boolean isClientCredentials();

    String getOid();

    void verifyAuthentication();
}
