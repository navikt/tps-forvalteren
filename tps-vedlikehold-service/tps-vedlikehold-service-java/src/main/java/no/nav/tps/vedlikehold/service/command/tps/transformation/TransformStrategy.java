package no.nav.tps.vedlikehold.service.command.tps.transformation;

import org.codehaus.jackson.annotate.JsonIgnoreType;

@JsonIgnoreType
public interface TransformStrategy {

    boolean isSupported(Object o);

}
