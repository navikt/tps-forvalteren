package no.nav.tps.forvalteren.service.command.tps.transformation;

import com.fasterxml.jackson.annotation.JsonIgnoreType;

@JsonIgnoreType
@FunctionalInterface
public interface TransformStrategy {

    boolean isSupported(Object o);

}
