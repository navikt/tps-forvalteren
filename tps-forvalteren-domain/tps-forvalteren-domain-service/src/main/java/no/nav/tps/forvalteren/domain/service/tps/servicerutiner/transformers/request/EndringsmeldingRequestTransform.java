package no.nav.tps.forvalteren.domain.service.tps.servicerutiner.transformers.request;

import com.fasterxml.jackson.annotation.JsonIgnoreType;

@JsonIgnoreType
public class EndringsmeldingRequestTransform implements RequestTransformer {
    public static RequestTransformer endringsmeldingXmlWrappingAppender() {
        return new EndringsmeldingRequestTransform();
    }
}
