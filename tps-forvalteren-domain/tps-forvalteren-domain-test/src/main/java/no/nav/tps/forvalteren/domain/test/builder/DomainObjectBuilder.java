package no.nav.tps.forvalteren.domain.test.builder;

public abstract class DomainObjectBuilder<T> {

    /**
     * Builds a new object based off of this builder's current state.
     */
    protected abstract T build();

    public static <T> T build(DomainObjectBuilder<T> builder) {
        return builder.build();
    }
}
