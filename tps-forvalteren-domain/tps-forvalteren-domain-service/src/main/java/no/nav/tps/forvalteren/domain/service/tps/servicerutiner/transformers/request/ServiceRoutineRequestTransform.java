package no.nav.tps.forvalteren.domain.service.tps.servicerutiner.transformers.request;

import com.fasterxml.jackson.annotation.JsonIgnoreType;

@JsonIgnoreType
public class ServiceRoutineRequestTransform implements RequestTransformer {

    public static RequestTransformer serviceRoutineXmlWrappingAppender() {
        return new ServiceRoutineRequestTransform();
    }
}