package no.nav.tps.forvalteren.domain.service.tps.servicerutiner.transformers.request;

import org.codehaus.jackson.annotate.JsonIgnoreType;

@JsonIgnoreType
public class EndringsmeldingRequestTransform implements RequestTransformer {
    public static RequestTransformer endringsmeldingXmlWrappingAppender() {
        return new EndringsmeldingRequestTransform();
    }
}
