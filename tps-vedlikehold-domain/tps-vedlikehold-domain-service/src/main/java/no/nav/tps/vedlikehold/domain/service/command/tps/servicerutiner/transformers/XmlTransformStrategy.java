package no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.transformers;

import org.codehaus.jackson.annotate.JsonIgnoreType;

@JsonIgnoreType
public interface XmlTransformStrategy {

    String execute(String xml);

}
