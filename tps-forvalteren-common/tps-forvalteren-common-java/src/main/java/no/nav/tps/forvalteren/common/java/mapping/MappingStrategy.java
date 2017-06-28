package no.nav.tps.forvalteren.common.java.mapping;

import ma.glasnost.orika.MapperFactory;

@FunctionalInterface
public interface MappingStrategy {

    void register(MapperFactory factory);

}
