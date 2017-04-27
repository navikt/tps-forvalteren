package no.nav.tps.forvalteren.domain.service.tps.servicerutiner.transformers.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.codehaus.jackson.annotate.JsonIgnoreType;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreType
public class RemoveTakenFnrFromResponseTransform implements ResponseTransformer {

    private String antallFnrRequestTag;

    public static ResponseTransformer removeTakenFnrFromResponseTransform(String antallFnrRequestTag) {
        return new RemoveTakenFnrFromResponseTransform(antallFnrRequestTag);
    }
}

