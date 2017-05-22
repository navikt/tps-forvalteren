package no.nav.tps.forvalteren.repository.jpa;

@FunctionalInterface
public interface UsernameResolver {
    String getUsername();
}
