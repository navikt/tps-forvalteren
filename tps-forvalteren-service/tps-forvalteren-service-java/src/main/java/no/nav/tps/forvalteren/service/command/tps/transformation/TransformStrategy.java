package no.nav.tps.forvalteren.service.command.tps.transformation;

import org.codehaus.jackson.annotate.JsonIgnoreType;

@JsonIgnoreType
@FunctionalInterface
public interface TransformStrategy {

    boolean isSupported(Object o);

}
