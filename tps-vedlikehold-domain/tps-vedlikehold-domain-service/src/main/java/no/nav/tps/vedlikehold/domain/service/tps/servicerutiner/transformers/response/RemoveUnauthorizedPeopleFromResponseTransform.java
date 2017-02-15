package no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.transformers.response;

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
public class RemoveUnauthorizedPeopleFromResponseTransform implements ResponseTransformer {

    private String totalHitsXmlElement;
    private String hitsInBufferXmlElement;

    public static ResponseTransformer removeUnauthorizedFnrFromResponse(String totalHitsXmlElement, String hitsInBufferXmlElement) {
        return new RemoveUnauthorizedPeopleFromResponseTransform(totalHitsXmlElement, hitsInBufferXmlElement);
    }
}
