package no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.transformers.response;

import org.codehaus.jackson.annotate.JsonIgnoreType;

@JsonIgnoreType
public class RemoveUnauthorizedPeopleFromResponseTransform implements ResponseTransformer {

    public static ResponseTransformer removeUnauthorizedFnrFromResponse() {
        return new RemoveUnauthorizedPeopleFromResponseTransform();
    }
}
