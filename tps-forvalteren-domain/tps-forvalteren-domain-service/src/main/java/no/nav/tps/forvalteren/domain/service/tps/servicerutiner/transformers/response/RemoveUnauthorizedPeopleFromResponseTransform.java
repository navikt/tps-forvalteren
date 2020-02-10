package no.nav.tps.forvalteren.domain.service.tps.servicerutiner.transformers.response;

import com.fasterxml.jackson.annotation.JsonIgnoreType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
